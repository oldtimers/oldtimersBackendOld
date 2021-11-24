package pl.pazurkiewicz.oldtimers_rally.controller.event;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

import java.util.Locale;

@Controller
@RequestMapping("/{url}")
public class EventController extends AbstractEventController {
    public EventController(EventRepository eventRepository) {
        super(eventRepository);
    }

    @GetMapping
    String getSelectedEvent(Locale locale, Event event) {
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        System.out.println("Locale >>> " + locale);
        return "event/show_event";
    }

}
