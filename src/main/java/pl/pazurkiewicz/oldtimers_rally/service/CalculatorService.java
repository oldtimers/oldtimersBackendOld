package pl.pazurkiewicz.oldtimers_rally.service;

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

    @Transactional
    public void countGlobalPoints(Event event) {
        List<Competition> competitions = competitionRepository.getByEvent(event);
        List<Category> categories = categoryRepository.getByEvent(event);
        for (Category category : categories) {
            calculateGlobalPointsForCategory(competitions, category);
        }
    }

    public void calculateGlobalPointsForCategory(List<Competition> competitions, Category category) {
        Set<Crew> crews = category.getCrewCategories().stream().map(CrewCategory::getCrew).collect(Collectors.toSet());
        HashMap<Crew, Float> crewsValue = new HashMap<>();
        crews.forEach(c -> crewsValue.put(c, 0.f));
        for (Competition competition : competitions) {
            List<Score> scores = scoreRepository.getByCompetitionAndCrew_InOrderByResult(competition, crews);
            Set<Crew> crewsWithScore = scores.stream().map(Score::getCrew).collect(Collectors.toSet());
            switch (competition.getType()) {
                case REGULAR_DRIVE:
                case BEST_MIN:
                    calculateCompetitionForBestMin(crewsValue, scores, competition);
                case BEST_MAX:
                    calculateCompetitionForBestMax(crewsValue, scores, competition);
                case COUNTED:
                    calculateCompetitionForCounted(crewsValue, scores);
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

    public void calculateCompetitionForBestMin(HashMap<Crew, Float> crewsValue, List<Score> scores, Competition competition) {
        int subsets = competition.getNumberOfSubsets();
        if (!scores.isEmpty() && subsets > 1) {
            float minScore = scores.get(0).getResult();
            float maxScore = scores.get(scores.size() - 1).getResult();
            float pointMove = competition.getMaxRankingPoints() / (subsets - 1.f);
            List<Float> jumps = new LinkedList<>();
            for (int i = 0; i < subsets; i++) {
                jumps.add(minScore + ((i + 1) * (maxScore - minScore) / subsets));
            }
            int i = 0;
            for (Score score : scores) {
                while (score.getResult() > jumps.get(i)) {
                    i++;
                }
                crewsValue.put(score.getCrew(), crewsValue.get(score.getCrew()) + Math.round(pointMove * i));
            }
        }
    }

    public void calculateCompetitionForCounted(HashMap<Crew, Float> crewsValue, List<Score> scores) {
        for (Score score : scores) {
            crewsValue.put(score.getCrew(), crewsValue.get(score.getCrew()) + score.getResult());
        }
    }

    public void calculateCompetitionForBestMax(HashMap<Crew, Float> crewsValue, List<Score> scores, Competition competition) {
//TODO
    }
}
