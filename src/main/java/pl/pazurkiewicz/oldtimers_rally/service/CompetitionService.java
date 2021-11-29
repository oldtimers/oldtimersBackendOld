package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class CompetitionService {
    @Autowired
    CompetitionRepository competitionRepository;

}
