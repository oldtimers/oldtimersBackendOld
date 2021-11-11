package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventLanguageCodeRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.model.projection.EventWriteModel;
import pl.pazurkiewicz.oldtimers_rally.repositiories.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.service.EventService;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/rally")
@SessionAttributes("event")
public class EventController {
    private final LanguageService languageService;
    private final LanguageRepository languageRepository;
    private final EventRepository eventRepository;
    private final EventLanguageCodeRepository eventLanguageCodeRepository;
    private final EventService eventService;
    EventController(LanguageService languageService, LanguageRepository languageRepository, EventRepository eventRepository, EventLanguageCodeRepository eventLanguageCodeRepository, EventService eventService) {
        this.languageService = languageService;
        this.languageRepository = languageRepository;
        this.eventRepository = eventRepository;
        this.eventLanguageCodeRepository = eventLanguageCodeRepository;
        this.eventService = eventService;
    }

    //    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    String showCreationForm() {
        return "event/create_event_form";
    }

    //    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    @Transactional
    String createEvent(@ModelAttribute("event") @Valid EventWriteModel event, BindingResult bindingResult, Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "event/create_event_form";
        }
        Event created = event.generateEvent();
        eventRepository.save(created);
        return "index";
    }

    @ModelAttribute("event")
    EventWriteModel getEvent() {
        return EventWriteModel.generateNewEventWriteModel(languageService, languageRepository);
    }

// TODO dopisać do usera language
}
