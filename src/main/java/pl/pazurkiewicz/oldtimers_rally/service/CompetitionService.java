package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.web.CompetitionModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class CompetitionService {
    @Autowired
    CompetitionRepository competitionRepository;


    public Competition saveCompetitionModel(CompetitionModel competitionModel) {
        competitionModel.preUpdate();
        Competition competition = competitionModel.getCompetition();
        switch (competition.getType()) {
            case COUNTED:
                competition.setMaxRankingPoints(null);
                competition.setNumberOfSubsets(null);
            case BEST_MAX:
            case BEST_MIN:
                competition.setAverageSpeed(null);
                competition.setDistance(null);
                break;
            case REGULAR_DRIVE:
                competition.setFunctionCode(null);
        }
        return competitionRepository.saveAndFlush(competition);
    }

    @Transactional
    public void removeCompetition(Integer competitionId) {
        competitionRepository.deleteById(competitionId);
    }
}
