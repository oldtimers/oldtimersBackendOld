package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.CategoryEnum;

import java.util.*;

public class CategoriesModel implements ListWebModel<Category> {
    private static final Hashtable<String, CategoryEnum> categoriesEnum = new Hashtable<>() {{
        put("Category by year", CategoryEnum.year);
        put("Other category", CategoryEnum.other);
    }};
    private final List<Category> yearCategories = new ArrayList<>();
    private final List<Category> differentCategories = new ArrayList<>();
    private final Set<Integer> deletedCategories = new HashSet<>();
    private Category newCategory = new Category();

    public CategoriesModel(List<Category> categories) {
        for (Category category : categories) {
            if (category.getMode() == CategoryEnum.year) {
                yearCategories.add(category);
            } else {
                differentCategories.add(category);
            }
        }
    }

    public List<Category> getYearCategories() {
        return yearCategories;
    }

    public List<Category> getDifferentCategories() {
        return differentCategories;
    }

    public Hashtable<String, CategoryEnum> getCategoriesEnum() {
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

    public void removeYearCategory(Integer removeId) {
        removeFromList(removeId, yearCategories, deletedCategories);
    }

    public void removeDifferentCategory(Integer removeId) {
        removeFromList(removeId, differentCategories, deletedCategories);
    }


}
