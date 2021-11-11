package pl.pazurkiewicz.oldtimers_rally.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.pazurkiewicz.oldtimers_rally.model.Dictionary;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventLanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class EventService {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventLanguageRepository eventLanguageRepository;

}
