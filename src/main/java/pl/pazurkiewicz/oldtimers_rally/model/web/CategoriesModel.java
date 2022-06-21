package pl.pazurkiewicz.oldtimers_rally.model.web;

import net.minidev.json.annotate.JsonIgnore;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.CategoryEnum;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;

import javax.validation.Valid;
import java.util.*;

public class CategoriesModel implements ListWebModel<Category> {
    private static final Hashtable<CategoryEnum, String> categoriesEnum = new Hashtable<>() {{
        put(CategoryEnum.year, "categories.year");
        put(CategoryEnum.other, "categories.other");
    }};
    @Valid
    private final List<Category> yearCategories = new ArrayList<>();
    @Valid
    private final List<Category> otherCategories = new ArrayList<>();
    @JsonIgnore
    private final Set<Integer> deletedCategories = new HashSet<>();
    private final List<EventLanguage> languages;
    @Valid
    private Category newCategory;

    public CategoriesModel(List<Category> categories, Event event) {
        languages = event.getEventLanguages();
        languages.sort(new EventLanguageComparator());
        for (Category category : categories) {
            if (category.getMode() == CategoryEnum.year) {
                yearCategories.add(category);
            } else {
                otherCategories.add(category);
            }
            category.getName().prepareForLoad(event.getEventLanguages());
            category.getDescription().prepareForLoad(event.getEventLanguages());
        }
        newCategory = new Category(CategoryEnum.other, event);
    }

    public List<EventLanguage> getLanguages() {
        return languages;
    }

    public void acceptNewModel(Event event) {
        if (newCategory.getMode() == CategoryEnum.year) {
            yearCategories.add(newCategory);
        } else {
            newCategory.setMaxYear(null);
            newCategory.setMinYear(null);
            otherCategories.add(newCategory);
        }
        newCategory = new Category(newCategory.getMode(), event);
    }

    public List<Category> getYearCategories() {
        return yearCategories;
    }

    public List<Category> getOtherCategories() {
        return otherCategories;
    }

    public Hashtable<CategoryEnum, String> getCategoriesEnum() {
        return categoriesEnum;
    }

    public Set<Integer> getDeletedCategories() {
        return deletedCategories;
    }

    public Category getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(Category newCategory) {
        this.newCategory = newCategory;
    }


    public void deleteYearCategory(Integer removeId) {
        removeFromList(removeId, yearCategories, deletedCategories);
    }

    public void deleteOtherCategory(Integer removeId) {
        removeFromList(removeId, otherCategories, deletedCategories);
    }

    public void preUpdate() {
        yearCategories.forEach(Category::preUpdate);
        otherCategories.forEach(Category::preUpdate);
    }
}
