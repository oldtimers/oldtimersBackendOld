package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.exception.InvalidScore;
import pl.pazurkiewicz.oldtimers_rally.model.*;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.AdvancedRegScoreRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.AdvancedScoreRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.ReqScoreEnum;
import pl.pazurkiewicz.oldtimers_rally.repository.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.ScoreRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class ScoreService {
    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    ScoreRepository scoreRepository;
    @Autowired
    MyConfigurationProperties configurationProperties;

    public void addRegScore(AdvancedRegScoreRequest scoreRequest, Integer eventId, User user) throws InvalidScore {
        Optional<Competition> competition = competitionRepository.getByEvent_idAndId(eventId, scoreRequest.getCompetitionId(), Competition.class);
        Crew crew = crewRepository.getById(scoreRequest.getCrewId());
        if (competition.isPresent() && competition.get().getType() == CompetitionTypeEnum.REGULAR_DRIVE) {
            Score score = scoreRepository.getByCompetitionAndCrew(competition.get(), crew);
            if (score == null) {
                score = new Score();
                score.setCrew(crew);
                score.setCompetition(competition.get());
            }
            if (scoreRequest.getPosition() == ReqScoreEnum.START) {
                score.setAdditional1(scoreRequest.getTime());
                score.setAdditional2(null);
                score.setAdditional3(null);
                score.setResult(null);
            } else {
                score.setAdditional2(scoreRequest.getTime());
                if (score.getAdditional1() != null) {
                    calculateAverage(score, competition.get());
                }
            }
            score.setAuthor(user);
            scoreRepository.save(score);
        } else {
            throw new EntityNotFoundException("Competition does not exist");
        }
    }

    public void addScore(AdvancedScoreRequest advancedScoreRequest, Integer eventId, User user) throws InvalidScore {
        Optional<Competition> competition = competitionRepository.getByEvent_idAndId(eventId, advancedScoreRequest.getCompetitionId(), Competition.class);
        Crew crew = crewRepository.getById(advancedScoreRequest.getCrewId());
        if (competition.isPresent() && competition.get().getType() != CompetitionTypeEnum.REGULAR_DRIVE) {
            Score score;
            if (configurationProperties.getDuplicateScores()) {
                score = null;
            } else {
                score = scoreRepository.getByCompetitionAndCrew(competition.get(), crew);
            }
            if (score == null) {
                score = new Score();
                score.setCrew(crew);
                score.setCompetition(competition.get());
            }
            assignScoreRequestToFields(score, competition.get(), advancedScoreRequest);
            if (!score.isInvalidResult()) {
                CalculatorService.calculateScoreResult(score, competition.get());
            }
            score.setAuthor(user);
            scoreRepository.save(score);
        } else {
            throw new EntityNotFoundException("Competition does not exist");
        }
    }


    private void assignScoreRequestToFields(Score score, Competition competition, AdvancedScoreRequest advancedScoreRequest) throws InvalidScore {
        List<CompetitionField> fields = competition.getFields();
        score.setInvalidResult(competition.getPossibleInvalid() && advancedScoreRequest.getInvalidResult());
        if (score.isInvalidResult()) {
            return;
        }
        for (CompetitionField field : fields) {
            switch (field.getOrder()) {
                case 0:
                    score.setAdditional1(castScoreFieldToDouble(advancedScoreRequest.getA(), field.getType()));
                    break;
                case 1:
                    score.setAdditional2(castScoreFieldToDouble(advancedScoreRequest.getB(), field.getType()));
                    break;
                case 2:
                    score.setAdditional3(castScoreFieldToDouble(advancedScoreRequest.getC(), field.getType()));
                    break;
                case 3:
                    score.setAdditional4(castScoreFieldToDouble(advancedScoreRequest.getD(), field.getType()));
                    break;
                case 4:
                    score.setAdditional5(castScoreFieldToDouble(advancedScoreRequest.getE(), field.getType()));
                    break;
            }
        }
    }

    private double castScoreFieldToDouble(Object value, CompetitionFieldTypeEnum fieldTypeEnum) throws InvalidScore {
        switch (fieldTypeEnum) {
            case FLOAT:
            case TIMER:
                if (value instanceof Double)
                    return (Double) value;
                else if (value instanceof Integer)
                    return Double.valueOf((Integer) value);
                break;
            case INT:
                if (value instanceof Integer)
                    return Double.valueOf((Integer) value);
                break;
            case BOOLEAN:
                if (value instanceof Boolean)
                    return (((Boolean) value) ? 1.0 : 0.0);
                break;
            case DATETIME:
                if (value instanceof Integer) {
                    int p = (Integer) value;
                    if (p >= 0 && p < 1440) {
                        return p;
                    }
                }
        }
        throw new InvalidScore("");
    }

    private void calculateAverage(Score score, Competition competition) throws InvalidScore {
        if (score.getAdditional1() == null || score.getAdditional2() == null || score.getAdditional1() > score.getAdditional2()) {
            throw new InvalidScore(score.getId().toString());
        }
        double divInMillis = score.getAdditional2() - score.getAdditional1();
        double average = competition.getDistance() / (divInMillis / 3600000f);
        score.setAdditional3(average);
        score.setResult(Math.abs(average - competition.getAverageSpeed()));
    }
}
