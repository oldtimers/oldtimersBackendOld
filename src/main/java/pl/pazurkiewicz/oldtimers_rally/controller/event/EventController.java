package pl.pazurkiewicz.oldtimers_rally.controller.event;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.Language;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
//^(?!rally$)(?!api$).*$
@RequestMapping("/{url:" + MyConfigurationProperties.eventRegex + "}")
public class EventController {
    private final EventRepository eventRepository;
    private final CrewRepository crewRepository;
    private final LocaleResolver localeResolver;
    private final CompetitionRepository competitionRepository;
    private final CategoryRepository categoryRepository;

    public EventController(EventRepository eventRepository, CrewRepository crewRepository, LocaleResolver localeResolver, CompetitionRepository competitionRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.crewRepository = crewRepository;
        this.localeResolver = localeResolver;
        this.competitionRepository = competitionRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    String getSelectedEvent(Model model, @PathVariable("url") String url, Locale locale, HttpServletRequest request, HttpServletResponse response) {
        Event event = getEvent(url);
        List<Language> languages = languages(event);
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        if (languages.stream().map(Language::getCode).noneMatch(s -> s.equals(locale.getLanguage()))) {
            localeResolver.setLocale(request, response, new Locale(event.getSingleDefaultLanguage().getLanguage().getCode()));
        }
        model.addAttribute("event", event);
        model.addAttribute("languages", languages);
        model.addAttribute("crews", crewRepository.getAllByEvent_UrlAndPresentIsTrueOrderByQrCode_NumberAscYearOfProductionAsc(url));
        model.addAttribute("competitions", competitionRepository.getByEvent(event));
        model.addAttribute("categories", categoryRepository.findByEvent_IdOrderById(event.getId()));
        return "event/show_event";
    }


    @GetMapping("/crew/{crewId}")
    String getSelectedCrew(@PathVariable("url") String url, @PathVariable("crewId") Integer crewId, Model model, Locale locale, HttpServletRequest request, HttpServletResponse response) {
        Event event = getEvent(url);
        List<Language> languages = languages(event);
        Optional<Crew> crew = crewRepository.getByIdWithLanguages(crewId, event);
        if (crew.isEmpty()) {
            throw new ResourceNotFoundException();
        } else {
            if (languages.stream().map(Language::getCode).noneMatch(s -> s.equals(locale.getLanguage()))) {
                localeResolver.setLocale(request, response, new Locale(event.getSingleDefaultLanguage().getLanguage().getCode()));
            }
            model.addAttribute("crew", crew.get());
        }
        model.addAttribute("event", event);
        model.addAttribute("languages", languages);
        return "crew/show_crew";
    }

    private Event getEvent(String url) {
        return eventRepository.getByUrl(url);
    }

    private List<Language> languages(Event event) {
        if (event != null) {
            return event.getEventLanguages().stream().map(EventLanguage::getLanguage).collect(Collectors.toList());
        }
        return new LinkedList<>();
    }
}
