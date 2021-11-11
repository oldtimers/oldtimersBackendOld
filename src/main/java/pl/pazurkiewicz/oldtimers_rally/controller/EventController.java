package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.model.projection.EventWriteModel;
import pl.pazurkiewicz.oldtimers_rally.repositiories.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;

import javax.validation.Valid;

@Controller
@RequestMapping("/rally")
@SessionAttributes("event")
public class EventController {
    private final LanguageService languageService;
    private final LanguageRepository languageRepository;
    private final EventRepository eventRepository;

    EventController(LanguageService languageService, LanguageRepository languageRepository, EventRepository eventRepository) {
        this.languageService = languageService;
        this.languageRepository = languageRepository;
        this.eventRepository = eventRepository;
    }

    //    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    String showCreationForm() {
        return "event/create_event_form";
    }

    //    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    String createEvent(@ModelAttribute("event") @Valid EventWriteModel event, BindingResult bindingResult, Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "event/create_event_form";
        }
        return "index";
    }

    @ModelAttribute("event")
    EventWriteModel getEvent() {
        return EventWriteModel.generateNewEventWriteModel(languageService, languageRepository);
    }

// TODO dopisać do usera language
}
