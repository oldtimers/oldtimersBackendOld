package pl.pazurkiewicz.oldtimers_rally.controller.event;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.Language;
import pl.pazurkiewicz.oldtimers_rally.model.Score;
import pl.pazurkiewicz.oldtimers_rally.repository.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
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

    private final LanguageRepository languageRepository;

    public EventController(EventRepository eventRepository, CrewRepository crewRepository, LocaleResolver localeResolver, CompetitionRepository competitionRepository, CategoryRepository categoryRepository, LanguageRepository languageRepository) {
        this.eventRepository = eventRepository;
        this.crewRepository = crewRepository;
        this.localeResolver = localeResolver;
        this.competitionRepository = competitionRepository;
        this.categoryRepository = categoryRepository;
        this.languageRepository = languageRepository;
    }

    @GetMapping
    String getSelectedEvent(Model model, @PathVariable("url") String url, Locale locale, @ModelAttribute("event") Event event, @ModelAttribute("languages") List<Language> languages, HttpServletRequest request, HttpServletResponse response) {
        if (event == null) {
            throw new ResourceNotFoundException();
        }
        if (languages.stream().map(Language::getCode).noneMatch(s -> s.equals(locale.getLanguage()))) {
            localeResolver.setLocale(request, response, new Locale(event.getSingleDefaultLanguage().getLanguage().getCode()));
        }
        model.addAttribute("crews", crewRepository.getAllByEvent_UrlAndPresentIsTrueOrderByQrCode_NumberAscYearOfProductionAsc(url));
        model.addAttribute("competitions", competitionRepository.getByEvent(event));
        model.addAttribute("categories", categoryRepository.findByEvent_IdOrderById(event.getId()));
        if (event.getCustomMainHtml() != null)
            return event.getCustomMainHtml();
        return "event/show_event";
    }


    @GetMapping("/crew/{crewId}")
    String getSelectedCrew(@ModelAttribute("event") Event event, @PathVariable("crewId") Integer crewId, Model model, @ModelAttribute("languages") List<Language> languages, Locale locale, HttpServletRequest request, HttpServletResponse response) {
        Optional<Crew> crew = crewRepository.getByIdWithLanguages(crewId, event);
        if (crew.isEmpty()) {
            throw new ResourceNotFoundException();
        } else {
            if (languages.stream().map(Language::getCode).noneMatch(s -> s.equals(locale.getLanguage()))) {
                localeResolver.setLocale(request, response, new Locale(event.getSingleDefaultLanguage().getLanguage().getCode()));
            }
            List<Score> scores = crew.get().getScores().stream().sorted(Comparator.comparingInt(o -> o.getCompetition().getId())).collect(Collectors.toList());
            model.addAttribute("crew", crew.get());
            model.addAttribute("scores", scores);
        }
        if (event.getCustomCrewHtml() != null)
            return event.getCustomCrewHtml();
        return "crew/show_crew";
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }

    @ModelAttribute("languages")
    List<Language> languages(@PathVariable("url") String url) {
        return languageRepository.getLanguagesByUrl(url);
    }
}
