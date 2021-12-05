package pl.pazurkiewicz.oldtimers_rally.controller.category;


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
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;
import pl.pazurkiewicz.oldtimers_rally.model.web.CategoriesModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.service.CategoriesService;

import javax.validation.Valid;

@Controller
@RequestMapping("/{url:" + MyConfigurationProperties.eventRegex + "}/edit/categories")
@SessionAttributes({"categories"})
public class EditCategoriesController {
    private final CategoryRepository categoryRepository;
    private final SmartValidator validator;
    private final CategoriesService categoriesService;
    private final EventRepository eventRepository;

    public EditCategoriesController(EventRepository eventRepository, CategoryRepository categoryRepository, SmartValidator validator, CategoriesService categoriesService) {
        this.categoryRepository = categoryRepository;
        this.validator = validator;
        this.categoriesService = categoriesService;
        this.eventRepository = eventRepository;
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
    @Transactional
    String saveCategories(@ModelAttribute("categories") CategoriesModel categories, BindingResult result, Event event, Model model) {
        Category temp = categories.getNewCategory();
        categories.setNewCategory(null);
        validator.validate(categories, result);
        if (result.hasErrors()) {
            categories.setNewCategory(temp);
            return "event/categories";
        }
        categoriesService.saveCategoriesModel(categories, event.getId());
        return showCategoriesPage(event, model);
    }

    @PostMapping(params = "reload")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String resetCategories(Model model, Event event) {
        return showCategoriesPage(event, model);
    }

    @PostMapping(params = "delete2")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String deleteYearCategory(@ModelAttribute("categories") CategoriesModel categories, Event event, @RequestParam(value = "delete2") Integer deleteId) {
        categories.deleteYearCategory(deleteId);
        return "event/categories";
    }

    @PostMapping(params = "delete1")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String deleteOtherCategory(@ModelAttribute("categories") CategoriesModel categories, Event event, @RequestParam(value = "delete1") Integer deleteId) {
        categories.deleteOtherCategory(deleteId);
        return "event/categories";
    }

    @PostMapping(params = "add")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String addCategory(@ModelAttribute("categories") @Valid CategoriesModel categories, BindingResult bindingResult, Event event) {
        if (bindingResult.hasErrors() || categories.getNewCategory() == null) {
            return "event/categories";
        }
        categories.acceptNewModel(event);
        return "event/categories";
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
