package pl.pazurkiewicz.oldtimers_rally.event;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("rally/create")
public class CreateEventController {
    private final EventRepository repository;

    CreateEventController(EventRepository repository) {
        this.repository = repository;
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping
    String showCreationForm() {
        return "event/event_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    String createEvent(@ModelAttribute("event") @Valid Event event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "event/event_form";
        }
        repository.save(event);
        return "index";
    }

    @ModelAttribute("event")
    Event getEvent() {
        return new Event();
    }
}
