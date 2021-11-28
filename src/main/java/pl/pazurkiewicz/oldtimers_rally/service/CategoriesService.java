package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.web.CategoriesModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CategoryRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CategoriesService {
    @Autowired
    CategoryRepository categoryRepository;

    public void saveCategoriesModel(CategoriesModel categories, Integer eventId) {
//        TODO
        categories.preUpdate();
        categoryRepository.deleteAllById(categories.getDeletedCategories());
        categoryRepository.saveAll(categories.getOtherCategories());
        categoryRepository.saveAll(categories.getYearCategories());
    }
}
