package pl.pazurkiewicz.oldtimers_rally.service;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.exception.InvalidScore;
import pl.pazurkiewicz.oldtimers_rally.model.*;
import pl.pazurkiewicz.oldtimers_rally.repository.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.CrewCategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.ScoreRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class CalculatorService {
    Logger log = LoggerFactory.getLogger(CalculatorService.class);


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
//        TODO check if it works
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
    public void countGlobalPoints(Event event) throws InvalidScore {
        List<List<Integer>> duplicates = scoreRepository.getDuplicates(event);
        if (!duplicates.isEmpty()) {
            for (List<Integer> duplicate : duplicates) {
                log.error(String.format("Duplicate result for competition id: %d, crew id: %d", duplicate.get(0), duplicate.get(1)));
            }
            throw new InvalidScore("Duplicate scores, impossible to generate results - see logs");
        }
        List<Competition> competitions = competitionRepository.getByEvent(event);
        Set<Category> categories = categoryRepository.getByEvent(event);

        for (Category category : categories) {
            calculateGlobalPointsForCategory(competitions, category);
        }
        event.setStage(StageEnum.RESULTS);
    }


    @Transactional
    public void evaluateYearMultiplier(Category category) {
        if (category.getYearMultiplierFunction() != null) {
            List<CrewCategory> crewCategories = category.getCrewCategories();
            if (crewCategories != null) {
                Optional<Integer> min = crewCategories.stream().map(CrewCategory::getCrew).filter(Crew::getPresent).map(Crew::getYearOfProduction).min(Integer::compareTo);
                Optional<Integer> max = crewCategories.stream().map(CrewCategory::getCrew).filter(Crew::getPresent).map(Crew::getYearOfProduction).max(Integer::compareTo);
                if (max.isPresent() && min.isPresent()) {
                    Argument y = new Argument("y");
                    Argument[] arguments = {new Argument("x", min.get()),
                            y,
                            new Argument("z", max.get())};
                    Expression expression = new Expression(category.getYearMultiplierFunction(), arguments);
                    crewCategories.forEach(crewCategory -> {
                        y.setArgumentValue(crewCategory.getCrew().getYearOfProduction());
                        crewCategory.setYearMultiplier(expression.calculate());
                    });
                }
            }
        }
    }

    @Transactional
    protected void calculateGlobalPointsForCategory(List<Competition> competitions, Category category) {
        Set<CrewCategory> crewCategories = crewCategoryRepository.getCrewCategoriesByCategoryAndPresent(category);
        Set<Crew> crews = new HashSet<>();
        Map<Crew, Double> multipliers = new HashMap<>();
        Map<Crew, Double> crewsValue = new HashMap<>();
        crewCategories.forEach(cc -> {
            Crew c = cc.getCrew();
            crews.add(c);
            crewsValue.put(c, 0.0);
            multipliers.put(c, cc.getYearMultiplier());
        });
        for (Competition competition : competitions) {
            List<Score> validScores = scoreRepository.getValidScoresForCompetition(competition, crews);
            List<Score> invalidScores = scoreRepository.getInvalidScoresForCompetition(competition, crews);
            Set<Crew> crewsWithScore = Stream.concat(validScores.stream(), invalidScores.stream()).map(Score::getCrew).collect(Collectors.toSet());
            calculateInvalidScores(crewsValue, invalidScores, competition);
            switch (competition.getType()) {
                case REGULAR_DRIVE:
                case BEST_MIN:
                    calculateCompetitionForBestMin(crewsValue, validScores, competition, multipliers);
                    break;
                case BEST_MAX:
                    calculateCompetitionForBestMax(crewsValue, validScores, competition, multipliers);
                    break;
                case COUNTED:
                    calculateCompetitionForCounted(crewsValue, validScores, competition, multipliers);
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

    private void calculateInvalidScores(Map<Crew, Double> crewsValue, List<Score> scores, Competition competition) {
        for (Score score : scores) {
            crewsValue.put(score.getCrew(), crewsValue.get(score.getCrew()) + competition.getMaxRankingPoints());
        }
    }

    public void calculateCompetitionForBestMin(Map<Crew, Double> crewsValue, List<Score> scores, Competition competition, Map<Crew, Double> multipliers) {
        calculateCompetitionForBest(crewsValue, scores, competition, multipliers, false);
    }

    public void calculateCompetitionForBestMax(Map<Crew, Double> crewsValue, List<Score> scores, Competition competition, Map<Crew, Double> multipliers) {
        calculateCompetitionForBest(crewsValue, scores, competition, multipliers, true);
    }

    public void calculateCompetitionForCounted(Map<Crew, Double> crewsValue, List<Score> scores, Competition competition, Map<Crew, Double> multipliers) {
        for (Score score : scores) {
            appendScore(crewsValue, score.getCrew(), score.getResult(), competition, multipliers);
        }
    }

    private void appendScore(Map<Crew, Double> crewsValue, Crew crew, Double score, Competition competition, Map<Crew, Double> multipliers) {
        int earned = (int) Math.round((multipliers.get(crew) != null ? Math.max(multipliers.get(crew), 0.0) : 1.0) * score);
        crewsValue.put(crew, crewsValue.get(crew) + Math.min(earned, competition.getType() == CompetitionTypeEnum.COUNTED ? Integer.MAX_VALUE : competition.getMaxRankingPoints()));
    }

    private void calculateCompetitionForBest(Map<Crew, Double> crewsValue, List<Score> scores, Competition competition, Map<Crew, Double> multipliers, boolean isMax) {
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
                    appendScore(crewsValue, score.getCrew(), pointMove * i, competition, multipliers);
                }
            } else {
                int subMover = subsets - 1;
                for (Score score : scores) {
                    while (i < subMover && (score.getResult() > jumps.get(i) || minScore == maxScore)) {
                        i++;
                    }
                    appendScore(crewsValue, score.getCrew(), pointMove * (subMover - i), competition, multipliers);
                }
            }
        }
    }
}
