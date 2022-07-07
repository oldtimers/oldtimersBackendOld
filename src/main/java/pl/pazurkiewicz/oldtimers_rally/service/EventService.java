package pl.pazurkiewicz.oldtimers_rally.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;
import pl.pazurkiewicz.oldtimers_rally.model.web.EventModel;
import pl.pazurkiewicz.oldtimers_rally.repository.EventLanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.security.service.UserDetailsImpl;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventLanguageRepository eventLanguageRepository;

    @Autowired
    private UserGroupService userGroupService;

    public Event saveFirstTime(EventModel eventModel, UserDetailsImpl principal) {
        Event toSave = eventModel.generateEvent();
        EventLanguageCode name = toSave.getName();
        EventLanguageCode description = toSave.getDescription();
        toSave.setName(null);
        toSave.setDescription(null);
        Event firstSave = eventRepository.save(toSave);
        firstSave.setName(name);
        firstSave.setDescription(description);
        Event saved = eventRepository.save(firstSave);
        userGroupService.addOwnerPrivileges(saved, principal);
        return saved;
    }

    public Event saveEventAgain(Event event) {
        return eventRepository.save(event);
    }
}
