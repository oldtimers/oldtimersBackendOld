package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.CrewCategory;
import pl.pazurkiewicz.oldtimers_rally.model.web.CategoriesModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class CategoriesService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    CalculatorService calculatorService;

    @Transactional
    public void saveCategoriesModel(CategoriesModel categories, Integer eventId) {
        categories.preUpdate();
        categoryRepository.deleteAllByIdsAndEvent_Id(categories.getDeletedCategories(), eventId);
        categoryRepository.saveAll(categories.getOtherCategories());
        categoryRepository.saveAll(categories.getYearCategories());
        categories.getYearCategories().forEach(this::assignToCrews);
        calculateYearMultipliers(eventId);
    }

    @Transactional
    public void assignToCrews(Category category) {
        for (Crew crew : crewRepository.getByEvent(category.getEvent())) {
            Optional<CrewCategory> crewCategory = crew.getCategories().stream().filter(c -> Objects.equals(c.getCategory().getId(), category.getId())).findAny();
            if (crewCategory.isPresent()) {
                if (!category.containYear(crew.getYearOfProduction())) {
                    crew.getCategories().remove(crewCategory.get());
                }
            } else {
                if (category.containYear(crew.getYearOfProduction())) {
                    CrewCategory newCrewCategory = new CrewCategory(crew, category);
                    crew.getCategories().add(newCrewCategory);
                }
            }
            crewRepository.save(crew);
        }
    }

    @Transactional
    public void calculateYearMultipliers(Integer eventId) {
        categoryRepository.getByEvent_Id(eventId).forEach(category -> calculatorService.evaluateYearMultiplier(category));
    }
}
