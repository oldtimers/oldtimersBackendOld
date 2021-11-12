package pl.pazurkiewicz.oldtimers_rally.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventLanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

@Service
@ApplicationScope
public class EventService {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventLanguageRepository eventLanguageRepository;

}
