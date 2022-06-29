package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.CrewCategory;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.web.CategoryPiece;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewModel;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewsModel;
import pl.pazurkiewicz.oldtimers_rally.repository.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.CrewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class CrewService {
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Transactional
    public void saveCrewsModel(CrewsModel crews) {
        crews.preUpdate(categoryRepository.getByEvent_IdAndModeYear(crews.getEvent().getId()));
        crewRepository.deleteAllById(crews.getDeletedCrews());
        crewRepository.saveAllAndFlush(crews.getCrews().stream().map(CrewModel::getCrewToSave).collect(Collectors.toList()));
    }

    public void saveCrewModel(CrewModel crewModel, Event event) {
        crewModel.preUpdate(categoryRepository.getByEvent_IdAndModeYear(event.getId()));
        crewRepository.saveAndFlush(crewModel.getCrewToSave());
    }

    @Transactional
    public void saveCrewsModelOnlyPresent(CrewsModel crews) {
        for (CrewModel crewModel : crews.getCrews()) {
            crewRepository.updatePresentByCrewId(crewModel.getId(), crewModel.getCrew().getPresent());
        }
    }

    //    returns added CrewCategory
    public List<CrewCategory> assignCrewToYearCategory(Crew crew) {
        return crew.assignToCategories(categoryRepository.getByEvent_IdAndModeYear(crew.getEvent().getId()));
    }

    public void assignCrewToYearCategory(CrewModel crew) {
        assignCrewToYearCategory(crew.getCrew()).forEach(c -> crew.getCategoryTable().add(new CategoryPiece(c.getCategory(), null)));
    }

//    @Transactional
//    public void assignAllCrewsToNumber(Event event) {
//        List<Crew> crews = crewRepository.getAllByEvent_UrlOrderByNumberAscYearOfProductionAsc(event.getUrl());
//        Set<Integer> usedNumbers = crews.stream().map(Crew::getNumber).filter(Objects::nonNull).collect(Collectors.toSet());
//        int number = 1;
//        for (Crew crew : crews) {
//            if (crew.getNumber() == null) {
//                while (usedNumbers.contains(number)) {
//                    number++;
//                }
//                crew.setNumber(number);
//                number++;
//            }
//        }
//    }
}
