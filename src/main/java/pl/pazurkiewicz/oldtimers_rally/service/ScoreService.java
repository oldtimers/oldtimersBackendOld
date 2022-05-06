package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.exception.InvalidScore;
import pl.pazurkiewicz.oldtimers_rally.model.*;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.RegScoreRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.ReqScoreEnum;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.ScoreRequest;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.ScoreRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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

    public void addRegScore(RegScoreRequest scoreRequest, Integer eventId) throws InvalidScore {
        Competition competition = competitionRepository.getByEvent_idAndId(eventId, scoreRequest.getCompetitionId());
        Crew crew = crewRepository.getById(scoreRequest.getCrewId());
        if (competition != null && competition.getType() == CompetitionTypeEnum.REGULAR_DRIVE) {
            Score score = scoreRepository.getByCompetitionAndCrew(competition, crew);
            if (score == null) {
                score = new Score();
                score.setCrew(crew);
                score.setCompetition(competition);
            }
            if (scoreRequest.getPosition() == ReqScoreEnum.START) {
                score.setAdditional1(scoreRequest.getTime());
                score.setAdditional2(null);
                score.setAdditional3(null);
                score.setResult(null);
            } else {
                score.setAdditional2(scoreRequest.getTime());
                calculateAverage(score, competition);
            }
            scoreRepository.save(score);
        } else {
            throw new EntityNotFoundException("Competition does not exist");
        }
    }

    public void addScore(ScoreRequest scoreRequest, Integer eventId) throws InvalidScore {
        Competition competition = competitionRepository.getByEvent_idAndId(eventId, scoreRequest.getCompetitionId());
        Crew crew = crewRepository.getById(scoreRequest.getCrewId());
        if (competition != null && competition.getType() != CompetitionTypeEnum.REGULAR_DRIVE) {
            Score score;
            if (configurationProperties.getDuplicateScores()) {
                score = null;
            } else {
                score = scoreRepository.getByCompetitionAndCrew(competition, crew);
            }
            if (score == null) {
                score = new Score();
                score.setCrew(crew);
                score.setCompetition(competition);
            }
            assignScoreRequestToFields(score, competition, scoreRequest);
            if (!score.isInvalidResult()) {
                CalculatorService.calculateScoreResult(score, competition);
            }
            scoreRepository.save(score);
        } else {
            throw new EntityNotFoundException("Competition does not exist");
        }
    }


    private void assignScoreRequestToFields(Score score, Competition competition, ScoreRequest scoreRequest) throws InvalidScore {
        List<CompetitionField> fields = competition.getFields();
        score.setInvalidResult(competition.getPossibleInvalid() && scoreRequest.getInvalidResult());
        if (score.isInvalidResult()) {
            return;
        }
        for (CompetitionField field : fields) {
            switch (field.getOrder()) {
                case 0:
                    score.setAdditional1(castScoreFieldToDouble(scoreRequest.getA(), field.getType()));
                    break;
                case 1:
                    score.setAdditional2(castScoreFieldToDouble(scoreRequest.getB(), field.getType()));
                    break;
                case 2:
                    score.setAdditional3(castScoreFieldToDouble(scoreRequest.getC(), field.getType()));
                    break;
                case 3:
                    score.setAdditional4(castScoreFieldToDouble(scoreRequest.getD(), field.getType()));
                    break;
                case 4:
                    score.setAdditional5(castScoreFieldToDouble(scoreRequest.getE(), field.getType()));
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
