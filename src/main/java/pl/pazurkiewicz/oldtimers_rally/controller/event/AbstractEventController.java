package pl.pazurkiewicz.oldtimers_rally.controller.event;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

public abstract class AbstractEventController {
    protected final EventRepository eventRepository;

    public AbstractEventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @ModelAttribute("action")
    String getAction(@PathVariable("url") String url) {
        return "/" + url + "/edit";
    }

    @CacheEvict(value = "eventsUrl", key = "#event.url")
    public void invalidateEvent(Event event) {
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }
}
