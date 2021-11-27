package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;

@Controller
@RequestMapping
public class BasicController {
    private final EventRepository eventRepository;


    public BasicController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String getIndex(Model model) {
        model.addAttribute("events", eventRepository.findAllSorted());
        return "index";
    }
}
