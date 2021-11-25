package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.CategoryEnum;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

public class CategoriesModel implements ListWebModel<Category> {
    private static final Hashtable<String, CategoryEnum> categoriesEnum = new Hashtable<>() {{
        put("Category by year", CategoryEnum.year);
        put("Other category", CategoryEnum.other);
    }};
    @Valid
    private final List<Category> yearCategories = new ArrayList<>();
    @Valid
    private final List<Category> otherCategories = new ArrayList<>();
    private final Set<Integer> deletedCategories = new HashSet<>();
    private Category newOtherCategory;
    private Category newYearCategory;

    public CategoriesModel(List<Category> categories, Event event) {
        for (Category category : categories) {
            if (category.getMode() == CategoryEnum.year) {
                yearCategories.add(category);
            } else {
                otherCategories.add(category);
            }
            category.getName().prepareForLoad(event.getEventLanguages());
            category.getDescription().prepareForLoad(event.getEventLanguages());
        }
        newOtherCategory = new Category(CategoryEnum.other, event.getEventLanguages());
        newYearCategory = new Category(CategoryEnum.year, event.getEventLanguages());
    }

    public List<Category> getYearCategories() {
        return yearCategories;
    }

    public List<Category> getOtherCategories() {
        return otherCategories;
    }

    public Hashtable<String, CategoryEnum> getCategoriesEnum() {
        return categoriesEnum;
    }

    public Set<Integer> getDeletedCategories() {
        return deletedCategories;
    }

    public Category getNewOtherCategory() {
        return newOtherCategory;
    }

    public void setNewOtherCategory(Category newOtherCategory) {
        this.newOtherCategory = newOtherCategory;
    }

    public Category getNewYearCategory() {
        return newYearCategory;
    }

    public void setNewYearCategory(Category newYearCategory) {
        this.newYearCategory = newYearCategory;
    }

    public void removeYearCategory(Integer removeId) {
        removeFromList(removeId, yearCategories, deletedCategories);
    }

    public void removeDifferentCategory(Integer removeId) {
        removeFromList(removeId, otherCategories, deletedCategories);
    }

}
