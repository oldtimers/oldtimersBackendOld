package pl.pazurkiewicz.oldtimers_rally.controller.event;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;

import java.util.Locale;
import java.util.Optional;

@Controller
//^(?!rally$)(?!api$).*$
@RequestMapping("/{url:" + MyConfigurationProperties.eventRegex + "}")
public class EventController {
    private final EventRepository eventRepository;
    private final CrewRepository crewRepository;

    public EventController(EventRepository eventRepository, CrewRepository crewRepository) {
        this.eventRepository = eventRepository;
        this.crewRepository = crewRepository;
    }

    @GetMapping
    String getSelectedEvent(Locale locale, Event event) {
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        System.out.println("Locale >>> " + locale);
        return "event/show_event";
    }


    @GetMapping("/crew/{crewId}")
    String getSelectedCrew(Event event, @PathVariable("crewId") Integer crewId, Model model) {
        Optional<Crew> crew = crewRepository.getByIdWithLanguages(crewId, event);
        if (crew.isEmpty()) {
            throw new ResourceNotFoundException();
        } else {
            model.addAttribute("crew", crew.get());
        }
        return "crew/show_crew";
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }
}
