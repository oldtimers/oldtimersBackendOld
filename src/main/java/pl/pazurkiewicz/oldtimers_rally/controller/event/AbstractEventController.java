package pl.pazurkiewicz.oldtimers_rally.controller.event;


import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;

import java.util.Objects;

public abstract class AbstractEventController {
    protected final EventRepository eventRepository;
    protected final CacheManager cacheManager;

    public AbstractEventController(EventRepository eventRepository, CacheManager cacheManager) {
        this.eventRepository = eventRepository;
        this.cacheManager = cacheManager;
    }


    @ModelAttribute("action")
    String getAction(@PathVariable("url") String url) {
        return "/" + url + "/edit";
    }

    public void invalidateEventByUrl(String url) {
        Objects.requireNonNull(cacheManager.getCache("eventsUrl")).evictIfPresent(url);
        Objects.requireNonNull(cacheManager.getCache("eventsExists")).evictIfPresent(url);
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }
}
