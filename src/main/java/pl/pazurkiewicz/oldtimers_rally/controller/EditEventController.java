package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.projection.EventWriteModel;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;

import javax.validation.Valid;

@Controller
@RequestMapping("/{url}/edit")
@SessionAttributes("editEvent")
public class EditEventController {
    private final EventRepository eventRepository;
    private final LanguageService languageService;

    public EditEventController(EventRepository eventRepository, LanguageService languageService) {
        this.eventRepository = eventRepository;
        this.languageService = languageService;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String showEditPage(Model model, @ModelAttribute("event") Event event) {
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        model.addAttribute("editEvent", EventWriteModel.generateByEvent(event, languageService));
        return "event/edit_event";
    }

    @PostMapping(params = "reload")
    @PreAuthorize("hasPermission(#url,'Event','" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String reloadEvent(@ModelAttribute("editEvent") @Valid EventWriteModel event, BindingResult bindingResult, String url) {
//        Reload is done during object validation
        return "event/edit_event";
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }

    @ModelAttribute("action")
    String getAction(@PathVariable("url") String url) {
        return "/" + url + "/edit";
    }
}
