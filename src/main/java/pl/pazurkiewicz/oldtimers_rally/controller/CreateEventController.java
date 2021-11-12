package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.projection.EventWriteModel;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiories.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiories.UserGroupRepository;
import pl.pazurkiewicz.oldtimers_rally.security.MyUserDetails;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping("/rally")
@SessionAttributes("newEvent")
public class CreateEventController {
    private final LanguageService languageService;
    private final LanguageRepository languageRepository;
    private final EventRepository eventRepository;
    private final UserGroupRepository userGroupRepository;

    CreateEventController(LanguageService languageService, LanguageRepository languageRepository, EventRepository eventRepository, UserGroupRepository userGroupRepository) {
        this.languageService = languageService;
        this.languageRepository = languageRepository;
        this.eventRepository = eventRepository;
        this.userGroupRepository = userGroupRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    String showCreationForm(Model model) {
        model.addAttribute("newEvent", EventWriteModel.generateNewEventWriteModel(languageService, languageRepository));
        return "event/create_event_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    @Transactional
    String createEvent(@ModelAttribute("newEvent") @Valid EventWriteModel event, BindingResult bindingResult, @AuthenticationPrincipal MyUserDetails principal) {
        if (bindingResult.hasErrors()) {
            return "event/create_event_form";
        }
        Event saved = eventRepository.save(event.generateEvent());
        userGroupRepository.save(new UserGroup(saved, principal.getUser(), UserGroupEnum.ROLE_OWNER));

        return "index";
    }

    @ModelAttribute("newEvent")
    EventWriteModel getEvent() {
        return EventWriteModel.generateNewEventWriteModel(languageService, languageRepository);
    }

    @PreAuthorize("hasPermission(#url,'Event','" + UserGroupEnum.Constants.ADMIN_VALUE + "')")
    @GetMapping("/{url}/edit")
    String test(@PathVariable String url) {
        return "index";
    }
}
