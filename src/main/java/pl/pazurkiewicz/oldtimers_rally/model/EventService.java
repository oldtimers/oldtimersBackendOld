package pl.pazurkiewicz.oldtimers_rally.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;

@Service
@ApplicationScope
public class EventService {
    @Autowired
    LanguageService languageService;

    public Event createNewEventForForm(){
        Event event = new Event();
        //    TODO not done
        return event;
    }
}