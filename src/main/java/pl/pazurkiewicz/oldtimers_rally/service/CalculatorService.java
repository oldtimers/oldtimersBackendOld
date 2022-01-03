package pl.pazurkiewicz.oldtimers_rally.service;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.*;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewCategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.ScoreRepository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class CalculatorService {
    public static final HashMap<Integer, String> variableMapping = new HashMap<>() {{
        put(0, "a");
        put(1, "b");
        put(2, "c");
        put(3, "d");
        put(4, "e");
    }};
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CrewCategoryRepository crewCategoryRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    public static void calculateScoreResult(Score score, Competition competition) {
        int j = competition.getFields().size();
        Argument[] arguments = new Argument[j];
        for (int i = 0; i < j; i++) {
            Argument argument = new Argument(CalculatorService.variableMapping.get(i));
            argument.setArgumentValue(score.getValue(i));
            arguments[i] = argument;
        }
        Expression expression = new Expression(competition.getFunctionCode(), arguments);
        double result = expression.calculate();
        if (Double.isNaN(result)) {
            score.setResult(null);
            score.setInvalidResult(true);
        } else {
            score.setResult(result);
            score.setInvalidResult(false);
        }
    }

    @Transactional
    public void countGlobalPoints(Event event) {
        List<Competition> competitions = competitionRepository.getByEvent(event);
        List<Category> categories = categoryRepository.getByEvent(event);
        for (Category category : categories) {
            calculateGlobalPointsForCategory(competitions, category);
        }
        event.setStage(StageEnum.RESULTS);
    }

    public void calculateGlobalPointsForCategory(List<Competition> competitions, Category category) {
        Set<Crew> crews = category.getCrewCategories().stream().map(CrewCategory::getCrew).collect(Collectors.toSet());
        HashMap<Crew, Double> crewsValue = new HashMap<>();
        crews.forEach(c -> crewsValue.put(c, 0.0));
        for (Competition competition : competitions) {
            List<Score> scores = scoreRepository.getByCompetitionAndResultNotNullAndCrew_InOrderByResult(competition, crews);
            Set<Crew> crewsWithScore = scores.stream().map(Score::getCrew).collect(Collectors.toSet());
            switch (competition.getType()) {
                case REGULAR_DRIVE:
                case BEST_MIN:
                    calculateCompetitionForBestMin(crewsValue, scores, competition);
                    break;
                case BEST_MAX:
                    calculateCompetitionForBestMax(crewsValue, scores, competition);
                    break;
                case COUNTED:
                    calculateCompetitionForCounted(crewsValue, scores);
                    break;
            }
            for (Crew crew : crews) {
                if (!crewsWithScore.contains(crew)) {
                    crewsValue.put(crew, crewsValue.get(crew) + competition.getAbsencePoints());
                }
            }
        }
        for (CrewCategory crewCategory : category.getCrewCategories()) {
            crewCategory.setRankingPoints(crewsValue.get(crewCategory.getCrew()));
        }
        crewCategoryRepository.saveAllAndFlush(category.getCrewCategories());
    }

    public void calculateCompetitionForBestMin(HashMap<Crew, Double> crewsValue, List<Score> scores, Competition competition) {
        calculateCompetitionForBest(crewsValue, scores, competition, false);
    }

    public void calculateCompetitionForBestMax(HashMap<Crew, Double> crewsValue, List<Score> scores, Competition competition) {
        calculateCompetitionForBest(crewsValue, scores, competition, true);
    }

    public void calculateCompetitionForCounted(HashMap<Crew, Double> crewsValue, List<Score> scores) {
        for (Score score : scores) {
            crewsValue.put(score.getCrew(), crewsValue.get(score.getCrew()) + score.getResult());
        }
    }

    private void calculateCompetitionForBest(HashMap<Crew, Double> crewsValue, List<Score> scores, Competition competition, boolean isMax) {
        int subsets = competition.getNumberOfSubsets();
        if (!scores.isEmpty() && subsets > 1) {
            double minScore = scores.get(0).getResult();
            double maxScore = scores.get(scores.size() - 1).getResult();
            double pointMove = competition.getMaxRankingPoints() / (subsets - 1.0);
            List<Double> jumps = new LinkedList<>();
            for (int i = 0; i < subsets; i++) {
                jumps.add(minScore + ((i + 1) * (maxScore - minScore) / subsets));
            }
            int i = 0;
            if (!isMax) {
                for (Score score : scores) {
                    while (i < subsets && score.getResult() > jumps.get(i)) {
                        i++;
                    }
                    crewsValue.put(score.getCrew(), crewsValue.get(score.getCrew()) + Math.round(pointMove * i));
                }
            } else {
                int subMover = subsets - 1;
                for (Score score : scores) {
                    while (i < subMover && (score.getResult() > jumps.get(i) || minScore == maxScore)) {
                        i++;
                    }
                    float z = Math.round(pointMove * (subMover - i));
                    crewsValue.put(score.getCrew(), crewsValue.get(score.getCrew()) + z);
                }
            }
        }
    }
}
