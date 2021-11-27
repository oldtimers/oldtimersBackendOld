package pl.pazurkiewicz.oldtimers_rally.controller.event;

import org.springframework.cache.CacheManager;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.web.EventWriteModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.service.EventService;
import pl.pazurkiewicz.oldtimers_rally.service.LanguageService;

import javax.validation.Valid;

@Controller
@RequestMapping("/{url}/edit/advanced")
@SessionAttributes({"editEvent"})
public class AdvancedEditEventController {
    private final LanguageService languageService;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final CacheManager cacheManager;


    public AdvancedEditEventController(EventRepository eventRepository, LanguageService languageService, EventService eventService, CacheManager cacheManager) {
        this.languageService = languageService;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.cacheManager = cacheManager;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String showEditPage(Model model, @ModelAttribute("event") Event event) {
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        invalidateEventByUrl(event.getUrl());
        model.addAttribute("editEvent", EventWriteModel.generateByEvent(event, languageService));
        return "event/edit_advanced_event";
    }

    @PostMapping(params = "reload")
    @PreAuthorize("hasPermission(#url,'Event','" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String reloadEvent(@ModelAttribute("editEvent") @Valid EventWriteModel event, BindingResult bindingResult, String url) {
//        Reload is done during object validation
        return "event/edit_advanced_event";
    }

    @PostMapping
    @PreAuthorize("hasPermission(#url,'Event','" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String updateEvent(@ModelAttribute("editEvent") @Valid EventWriteModel event, BindingResult bindingResult, String url, Model model) {
        if (bindingResult.hasErrors()) {
            return "event/edit_advanced_event";
        }
        Event saved = eventService.saveNextTime(event.generateEvent());
        invalidateEventByUrl(saved.getUrl());
        invalidateEventByUrl(url);
        model.addAttribute("event", saved);
        return showEditPage(model, saved);
    }

    public void invalidateEventByUrl(String url) {
        cacheManager.getCache("eventsByUrl").evictIfPresent(url);
        cacheManager.getCache("eventsId").evictIfPresent(url);
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }
}
