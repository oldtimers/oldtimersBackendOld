package pl.pazurkiewicz.oldtimers_rally.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.web.EventWriteModel;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.security.MyUserDetails;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserGroupService userGroupService;

    public Event saveFirstTime(EventWriteModel eventWriteModel, MyUserDetails principal) {
        Event saved = eventRepository.save(eventWriteModel.generateEvent());
        userGroupService.addOwnerPrivileges(saved, principal);
        return saved;
    }
}
