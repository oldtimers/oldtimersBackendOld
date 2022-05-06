package pl.pazurkiewicz.oldtimers_rally.controller.crew;

import org.hibernate.Hibernate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.Language;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewModel;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewsModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.service.CrewService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/{url:" + MyConfigurationProperties.eventRegex + "}/edit/crews")
@SessionAttributes({"crews"})
public class EditCrewsController {
    private final CrewRepository crewRepository;
    private final SmartValidator validator;
    private final CrewService crewService;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;

    public EditCrewsController(EventRepository eventRepository, CrewRepository crewRepository, SmartValidator validator, CrewService crewService, CategoryRepository categoryRepository, LanguageRepository languageRepository) {
        this.crewRepository = crewRepository;
        this.validator = validator;
        this.crewService = crewService;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.languageRepository = languageRepository;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String getCrews(@PathVariable("url") String url, Model model, Event event) {
        event.getEventLanguages().forEach(eventLanguage -> Hibernate.initialize(eventLanguage.getLanguage()));
        event.getEventLanguages().sort(new EventLanguageComparator());
        List<Category> categories = categoryRepository.findByEvent_IdOrderById(event.getId());
        model.addAttribute("crews", new CrewsModel(crewRepository.getSortedByEventId(event.getId()), categories, event));
        return "crew/edit_crews";
    }

    @PostMapping(params = "reload")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String resetCrews(@PathVariable("url") String url, Model model, Event event) {
        return getCrews(url, model, event);
    }

    @PostMapping(params = "delete")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String deleteCrew(@PathVariable("url") String url, @ModelAttribute("crews") CrewsModel crews, @RequestParam(value = "delete") Integer deleteId) {
        crews.removeCrew(deleteId);
        return "crew/edit_crews";
    }

    @PostMapping(params = "add")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String addCrew(@PathVariable("url") String url, @ModelAttribute("crews") @Valid CrewsModel crews, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "crew/edit_crews";
        }
        crewService.assignCrewToYearCategory(crews.getNewCrew());
        crews.acceptNewCrew();
        return "crew/edit_crews";
    }

    @PostMapping
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    @Transactional
    String saveCrews(@ModelAttribute("crews") CrewsModel crews, BindingResult bindingResult, @PathVariable("url") String url, Event event, Model model) {
        CrewModel newCrew = crews.getNewCrew();
        crews.setNewCrew(null);
        validator.validate(crews, bindingResult);
        if (bindingResult.hasErrors()) {
            crews.setNewCrew(newCrew);
            return "crew/edit_crews";
        }
        crewService.saveCrewsModel(crews);
        return getCrews(url, model, event);
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
