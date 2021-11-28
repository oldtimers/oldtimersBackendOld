package pl.pazurkiewicz.oldtimers_rally.controller.crew;

import org.hibernate.Hibernate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewListModel;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.service.CrewService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/{url}/edit/crews")
@SessionAttributes({"crews"})
public class EditCrewsController {
    private final CrewRepository crewRepository;
    private final SmartValidator validator;
    private final CrewService crewService;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public EditCrewsController(EventRepository eventRepository, CrewRepository crewRepository, SmartValidator validator, CrewService crewService, CategoryRepository categoryRepository) {
        this.crewRepository = crewRepository;
        this.validator = validator;
        this.crewService = crewService;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String getCrews(Model model, Event event) {
        event.getEventLanguages().forEach(eventLanguage -> Hibernate.initialize(eventLanguage.getLanguage()));
        event.getEventLanguages().sort(new EventLanguageComparator());
        List<Category> categories = categoryRepository.findByEvent_IdOrderById(event.getId());
        model.addAttribute("crews", new CrewListModel(crewRepository.getSortedByEventId(event.getId()), categories, event));
        return "event/crews";
    }

    @PostMapping(params = "reload")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String resetCrews(Model model, Event event) {
        return getCrews(model, event);
    }

    @PostMapping(params = "delete")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String deleteCrew(@ModelAttribute("crews") CrewListModel crews, Event event, @RequestParam(value = "delete") Integer deleteId) {
        crews.removeCrew(deleteId);
        return "event/crews";
    }

    @PostMapping(params = "add")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String addCrew(@ModelAttribute("crews") @Valid CrewListModel crews, BindingResult bindingResult, Event event) {
        if (bindingResult.hasErrors()) {
            return "event/crews";
        }
        crewService.assignCrewToYearCategory(crews.getNewCrew());
        crews.acceptNewCrew();
        return "event/crews";
    }

    @PostMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    @Transactional
    String saveCrews(@ModelAttribute("crews") CrewListModel crews, BindingResult bindingResult, Event event, Model model) {
        CrewModel newCrew = crews.getNewCrew();
        crews.setNewCrew(null);
        validator.validate(crews, bindingResult);
        if (bindingResult.hasErrors()) {
            crews.setNewCrew(newCrew);
            return "event/crews";
        }
        crewService.saveCrewsModel(crews);
        return getCrews(model, event);
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
