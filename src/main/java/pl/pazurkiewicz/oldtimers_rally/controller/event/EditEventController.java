package pl.pazurkiewicz.oldtimers_rally.controller.event;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewsWrapper;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{url}/edit")
@SessionAttributes({"crewsWrapper"})
public class EditEventController {
    private final EventRepository eventRepository;
    private final CrewRepository crewRepository;
    private final CompetitionRepository competitionRepository;

    public EditEventController(EventRepository eventRepository, CrewRepository crewRepository, CompetitionRepository competitionRepository) {
        this.eventRepository = eventRepository;
        this.crewRepository = crewRepository;
        this.competitionRepository = competitionRepository;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String showEditPage(@ModelAttribute("event") Event event) {
        return "event/edit_event";
    }


    @ModelAttribute("crewsWrapper")
    CrewsWrapper getCrews(@PathVariable("url") String url, @ModelAttribute("event") Event event) {
        return new CrewsWrapper(crewRepository.getAllByEvent_UrlOrderByNumberAscYearOfProductionAsc(url, Crew.class).stream().peek(crew -> {
            crew.getDescription().prepareForLoad(event.getEventLanguages());
        }).collect(Collectors.toList()));
    }

    @ModelAttribute("competitions")
    List<Competition> getCompetitions(@PathVariable("url") String url, @ModelAttribute("event") Event event) {
        return competitionRepository.getAllByEvent_UrlOrderById(url, Competition.class);
    }


    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        Event event = eventRepository.getByUrl(url);
        event.getEventLanguages().sort(new EventLanguageComparator());
        return event;
    }


}
