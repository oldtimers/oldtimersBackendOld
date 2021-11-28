package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Category;

public class CategoryPiece {
    private Category category;
    private Boolean value;

    //    if value is null, then category is added and category is type of year
    public CategoryPiece(Category category, Boolean value) {
        this.category = category;
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
