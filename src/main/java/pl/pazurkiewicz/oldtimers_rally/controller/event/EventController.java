package pl.pazurkiewicz.oldtimers_rally.controller.event;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;

import java.util.Locale;

@Controller
//^(?!rally$)(?!api$).*$
@RequestMapping("/{url:^(?!rally$).*$}")
public class EventController {
    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping
    String getSelectedEvent(Locale locale, Event event) {
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        System.out.println("Locale >>> " + locale);
        return "event/show_event";
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }
}
