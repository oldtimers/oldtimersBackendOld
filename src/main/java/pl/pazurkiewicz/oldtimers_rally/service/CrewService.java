package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewListModel;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;

import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class CrewService {
    @Autowired
    CrewRepository crewRepository;

    public void saveCrewsModel(CrewListModel crews) {
        crewRepository.deleteAllById(crews.getDeletedCrews());
        crewRepository.saveAll(crews.getCrews().stream().map(CrewModel::getCrew).collect(Collectors.toList()));
    }
}
