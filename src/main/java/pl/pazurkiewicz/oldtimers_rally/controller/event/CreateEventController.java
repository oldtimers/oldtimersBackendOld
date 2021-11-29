package pl.pazurkiewicz.oldtimers_rally.controller.event;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.web.EventModel;
import pl.pazurkiewicz.oldtimers_rally.security.MyUserDetails;
import pl.pazurkiewicz.oldtimers_rally.service.EventService;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping("/rally")
@SessionAttributes("newEvent")
public class CreateEventController {
    private final LanguageService languageService;
    private final EventService eventService;

    CreateEventController(LanguageService languageService, EventService eventService) {
        this.languageService = languageService;
        this.eventService = eventService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    String showCreationForm(Model model) {
        model.addAttribute("newEvent", EventModel.generateNewEventWriteModel(languageService));
        return "event/create_event_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    @Transactional
    String createEvent
            (@ModelAttribute("newEvent") @Valid EventModel event,
             BindingResult bindingResult,
             @AuthenticationPrincipal MyUserDetails principal,
             RedirectAttributes redirectAttributes
            ) {
        if (bindingResult.hasErrors()) {
            return "event/create_event_form";
        }
        Event saved = eventService.saveFirstTime(event, principal);
        redirectAttributes.addAttribute("url", saved.getUrl());
        return "redirect:/{url}/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/create", params = "reload")
    String reloadEvent(@ModelAttribute("newEvent") @Valid EventModel event, BindingResult bindingResult) {
//        Reload is done during object validation
        return "event/create_event_form";
    }

    @ModelAttribute("newEvent")
    EventModel getEvent() {
        return EventModel.generateNewEventWriteModel(languageService);
    }
}
