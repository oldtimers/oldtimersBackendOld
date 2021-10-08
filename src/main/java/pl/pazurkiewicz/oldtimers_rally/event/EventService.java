package pl.pazurkiewicz.oldtimers_rally.event;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.pazurkiewicz.oldtimers_rally.language.LanguageService;

@Service
@ApplicationScope
public class EventService {
    @Autowired
    LanguageService languageService;

    public Event createNewEventForForm(){
        Event event = new Event();
        event.setDefaultLanguage(languageService.getDefaultSystemLanguage());
        //    TODO not done
        return event;
    }
}
