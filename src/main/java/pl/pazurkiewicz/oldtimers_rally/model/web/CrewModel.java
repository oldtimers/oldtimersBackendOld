package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

public class CrewModel implements DatabaseModel {
    @Valid
    private final Crew crew;
    private final List<CategoryPiece> categoryTable;
    private final List<EventLanguage> languages;

    public CrewModel(Crew crew, List<EventLanguage> languages, List<Category> allCategories) {
        this.crew = crew;
        this.categoryTable = generateCategoryTable(allCategories);
        this.languages = languages;
        crew.getDescription().prepareForLoad(languages);
    }

    public Crew getCrew() {
        return crew;
    }

    public Crew getCrewToSave() {
        List<CrewCategory> crewCategories = crew.getCategories();
        for (CategoryPiece categoryPiece : categoryTable) {
            if (categoryPiece.getValue() == Boolean.TRUE) {
                if (crewCategories.stream().noneMatch(crewCategory -> Objects.equals(crewCategory.getCategory().getId(), categoryPiece.getCategory().getId()))) {
                    crewCategories.add(new CrewCategory(crew, categoryPiece.getCategory()));
                }
            } else {
                crewCategories.removeIf(crewCategory -> Objects.equals(crewCategory.getCategory().getId(), categoryPiece.getCategory().getId()));
            }
        }
        return crew;
    }

    @Override
    public Integer getId() {
        return crew.getId();
    }

    public List<CategoryPiece> getCategoryTable() {
        return categoryTable;
    }

    public void preUpdate(Collection<Category> categories) {
        crew.preUpdate(categories);
    }

    public List<CategoryPiece> generateCategoryTable(List<Category> allCategories) {
        List<CategoryPiece> categoryTable = new ArrayList<>();
        Set<Integer> crewCategoryIds = crew.getCategories().stream().map(CrewCategory::getCategory).map(Category::getId).collect(Collectors.toSet());
        for (Category category : allCategories) {
            if (crewCategoryIds.contains(category.getId())) {
                if (category.getMode() == CategoryEnum.other) {
                    categoryTable.add(new CategoryPiece(category, Boolean.TRUE));
                } else {
                    categoryTable.add(new CategoryPiece(category, null));
                }
            } else if (category.getMode() == CategoryEnum.other) {
                categoryTable.add(new CategoryPiece(category, Boolean.FALSE));
            }
        }
        return categoryTable;
    }
}
