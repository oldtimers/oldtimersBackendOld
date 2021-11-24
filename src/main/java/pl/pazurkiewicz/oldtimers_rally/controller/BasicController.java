package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

@Controller
@RequestMapping
public class BasicController {
    private final EventRepository eventRepository;

    public BasicController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping
    String getIndex(Model model) {
        model.addAttribute("events", eventRepository.findAllSorted());
        return "index";
    }

}
