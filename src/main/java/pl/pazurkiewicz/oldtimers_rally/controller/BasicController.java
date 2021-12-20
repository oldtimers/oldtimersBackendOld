package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.security.service.UserDetailsImpl;

import java.util.List;

@Controller
@RequestMapping
public class BasicController {
    private final EventRepository eventRepository;


    public BasicController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String getIndex(Model model, @AuthenticationPrincipal UserDetailsImpl principal) {
        if (principal != null){
            List<?> events = eventRepository.getEventsWithGlobalJudgePrivilegesForId(principal.getUser().getId(), Event.class);
            if (events.isEmpty()){
                events = eventRepository.getEventsWithJudgePrivilegesForId(principal.getUser().getId(), Event.class);
            }
            model.addAttribute("events", events);
        } else {
            model.addAttribute("events", eventRepository.findAllSorted());
        }
        return "index";
    }
}
