package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.User;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventLanguageCodeRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.model.projection.EventWriteModel;
import pl.pazurkiewicz.oldtimers_rally.repositiories.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiories.UserGroupRepository;
import pl.pazurkiewicz.oldtimers_rally.security.MyUserDetails;
import pl.pazurkiewicz.oldtimers_rally.service.EventService;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/rally")
@SessionAttributes("event")
public class EventController {
    private final LanguageService languageService;
    private final LanguageRepository languageRepository;
    private final EventRepository eventRepository;
    private final UserGroupRepository userGroupRepository;
    EventController(LanguageService languageService, LanguageRepository languageRepository, EventRepository eventRepository, UserGroupRepository userGroupRepository) {
        this.languageService = languageService;
        this.languageRepository = languageRepository;
        this.eventRepository = eventRepository;
        this.userGroupRepository = userGroupRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    String showCreationForm(Model model) {
        model.addAttribute("event",EventWriteModel.generateNewEventWriteModel(languageService, languageRepository));
        return "event/create_event_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    @Transactional
    String createEvent(@ModelAttribute("event") @Valid EventWriteModel event, BindingResult bindingResult,@AuthenticationPrincipal MyUserDetails principal) {
        if (bindingResult.hasErrors()) {
            return "event/create_event_form";
        }
        Event saved = eventRepository.save(event.generateEvent());
        userGroupRepository.save(new UserGroup(saved, principal.getUser(), UserGroupEnum.ROLE_OWNER));

        return "index";
    }

    @ModelAttribute("event")
    EventWriteModel getEvent() {
        return EventWriteModel.generateNewEventWriteModel(languageService, languageRepository);
    }

    @PreAuthorize("hasPermission('OWNER__6')")
    @GetMapping("/test")
    String test(){
        return "index";
    }
}
