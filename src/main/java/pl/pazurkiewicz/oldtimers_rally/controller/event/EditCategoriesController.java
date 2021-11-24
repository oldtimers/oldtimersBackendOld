package pl.pazurkiewicz.oldtimers_rally.controller.event;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.web.CategoriesModel;
import pl.pazurkiewicz.oldtimers_rally.repositiories.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

@Controller
@RequestMapping("/{url}/edit/categories")
@SessionAttributes({"categories"})
public class EditCategoriesController extends AbstractEventController {
    private final CategoryRepository categoryRepository;

    public EditCategoriesController(EventRepository eventRepository, CategoryRepository categoryRepository) {
        super(eventRepository);
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String showCategoriesPage(Event event, Model model) {
        model.addAttribute("categories", new CategoriesModel(categoryRepository.findByEventOrderById(event)));
        return "event/categories";
    }
}
