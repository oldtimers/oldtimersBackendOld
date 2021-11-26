package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewsModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CrewService {
    @Autowired
    CrewRepository crewRepository;

    public void saveCrewsModel(CrewsModel crews) {
        crewRepository.deleteAllById(crews.getDeletedCrews());
        crewRepository.saveAll(crews.getCrews());
    }
}
