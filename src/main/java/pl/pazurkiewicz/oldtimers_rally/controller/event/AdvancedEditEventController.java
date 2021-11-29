package pl.pazurkiewicz.oldtimers_rally.controller.event;

import org.hibernate.Hibernate;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.web.EventModel;
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
    @PreAuthorize("hasPermission(#url,'Event','" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String showEditPage(Model model, @PathVariable("url") String url) {
        Event event = eventRepository.getByUrl(url);
        invalidateEventByUrl(event.getUrl());
        event.getEventLanguages().forEach(eventLanguage -> Hibernate.initialize(eventLanguage.getLanguage()));
        model.addAttribute("editEvent", EventModel.generateByEvent(event, languageService));
        return "event/edit_advanced_event";
    }

    @PostMapping(params = "reload")
    @PreAuthorize("hasPermission(#url,'Event','" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String reloadEvent(@ModelAttribute("editEvent") @Valid EventModel event, BindingResult bindingResult, @PathVariable("url") String url) {
//        Reload is done during object validation
        return "event/edit_advanced_event";
    }

    @PostMapping
    @PreAuthorize("hasPermission(#url,'Event','" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String updateEvent(@ModelAttribute("editEvent") @Valid EventModel event, BindingResult bindingResult, @PathVariable("url") String url, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "event/edit_advanced_event";
        }
        Event saved = eventService.saveEventAgain(event.generateEvent());
        invalidateEventByUrl(saved.getUrl());
        invalidateEventByUrl(url);
        model.addAttribute("event", saved);
        redirectAttributes.addAttribute("url", saved.getUrl());
        return "redirect:/{url}/edit";
    }

    public void invalidateEventByUrl(String url) {
        cacheManager.getCache("eventsByUrl").evictIfPresent(url);
        cacheManager.getCache("eventsId").evictIfPresent(url);
    }
}
