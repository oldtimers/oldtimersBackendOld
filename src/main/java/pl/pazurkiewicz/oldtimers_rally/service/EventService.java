package pl.pazurkiewicz.oldtimers_rally.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.web.EventWriteModel;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventLanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.security.MyUserDetails;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventLanguageRepository eventLanguageRepository;

    @Autowired
    private UserGroupService userGroupService;

    public Event saveFirstTime(EventWriteModel eventWriteModel, MyUserDetails principal) {
        Event saved = eventRepository.save(eventWriteModel.generateEvent());
        userGroupService.addOwnerPrivileges(saved, principal);
        return saved;
    }

    public Event saveNextTime(Event event) {
//        Set<Integer> languages = eventLanguageRepository.getIdsByEventId(event.getId());
//        Set<Integer> eventLanguages = event.getEventLanguages().stream().map(EventLanguage::getId).filter(Objects::nonNull).collect(Collectors.toSet());
//        for (Integer lang : languages){
//            if (!eventLanguages.contains(lang)){
//                eventLanguageRepository.deleteById(lang);
//            }
//        }

//        eventLanguageRepository;
        return eventRepository.save(event);
    }
}
