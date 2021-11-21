package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

import java.util.Locale;

@Controller
@RequestMapping
public class EventController {
    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping(path = {"/{url}", "/{url}/"})
    String getSelectedEvent(@PathVariable("url") String url, Model model, Locale locale) {
        Event event = eventRepository.getByUrl(url);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        model.addAttribute("event", event);
        System.out.println("Locale >>> " + locale);
        return "event/show_event";
    }



}
