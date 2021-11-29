package pl.pazurkiewicz.oldtimers_rally.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.web.EventModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventLanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.security.MyUserDetails;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventLanguageRepository eventLanguageRepository;

    @Autowired
    private UserGroupService userGroupService;

    public Event saveFirstTime(EventModel eventModel, MyUserDetails principal) {
        Event saved = eventRepository.save(eventModel.generateEvent());
        userGroupService.addOwnerPrivileges(saved, principal);
        return saved;
    }

    public Event saveEventAgain(Event event) {
        return eventRepository.save(event);
    }
}
