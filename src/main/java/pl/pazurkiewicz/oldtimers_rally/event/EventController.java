package pl.pazurkiewicz.oldtimers_rally.event;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pazurkiewicz.oldtimers_rally.language.DefaultLanguageSelector;
import pl.pazurkiewicz.oldtimers_rally.language.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.language.LanguageService;

import javax.validation.Valid;

@Controller
@RequestMapping("/rally")
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
    String createEvent(@ModelAttribute("event") @Valid EventWriteModel event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "event/create_event_form";
        }
        return "index";
    }

    @ModelAttribute("event")
    EventWriteModel getEvent() {
        return new EventWriteModel(languageService, languageRepository);
    }

    @GetMapping("/test")
    String test(Model model) {
        model.addAttribute("defaultLanguage", new DefaultLanguageSelector(languageService, languageRepository));
        model.addAttribute("name", "defaultLanguage");

        return "fragments/form_pieces";
    }

}
