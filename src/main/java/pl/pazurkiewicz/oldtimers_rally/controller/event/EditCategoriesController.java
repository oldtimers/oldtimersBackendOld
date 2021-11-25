package pl.pazurkiewicz.oldtimers_rally.controller.event;


import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.Dictionary;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;
import pl.pazurkiewicz.oldtimers_rally.model.web.CategoriesModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/{url}/edit/categories")
@SessionAttributes({"categories"})
public class EditCategoriesController extends AbstractEventController {
    private final SmartValidator validator;
    private final CategoryRepository categoryRepository;

    public EditCategoriesController(EventRepository eventRepository, CategoryRepository categoryRepository, CacheManager cacheManager, SmartValidator validator) {
        super(eventRepository, cacheManager);
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }


    @GetMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String showCategoriesPage(Event event, Model model) {
        event.getEventLanguages().sort(new EventLanguageComparator());
        model.addAttribute("categories", new CategoriesModel(categoryRepository.findByEvent_IdOrderById(event.getId()), event));
        return "event/categories";
    }

    @PostMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String saveCategories(@ModelAttribute("categories") @Valid CategoriesModel categories, BindingResult result, Event event) {
        validator.validate(categories.getNewOtherCategory(), result);
        return "event/categories";
    }
}
