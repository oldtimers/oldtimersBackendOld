package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.*;

import javax.validation.Valid;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CrewModel implements DatabaseModel {
    @Valid
    private final Crew crew;
    private List<Category> possibleCategories = new ArrayList<>();
    private List<Category> actualCategories;

    public CrewModel(Crew crew, List<Category> allCategories) {
        this.crew = crew;
        actualCategories = crew.getCategories().stream().map(CrewCategory::getCategory).collect(Collectors.toList());
        Set<Integer> crewCategoryIds = actualCategories.stream().map(Category::getId).collect(Collectors.toSet());
        for (Category category : allCategories) {
            if (!crewCategoryIds.contains(category.getId())) {
                possibleCategories.add(category);
            }
        }
    }

    public List<Category> getPossibleCategories() {
        return possibleCategories;
    }

    public void setPossibleCategories(List<Category> possibleCategories) {
        this.possibleCategories = possibleCategories;
    }

    public Crew getCrew() {
        return crew;
    }

    @Override
    public Integer getId() {
        return crew.getId();
    }

    public void setId(Integer id) {
        crew.setId(id);
    }

    public Boolean getAcceptedRodo() {
        return crew.getAcceptedRodo();
    }

    public void setAcceptedRodo(Boolean acceptedRodo) {
        crew.setAcceptedRodo(acceptedRodo);
    }

    public Boolean getAcceptedReg() {
        return crew.getAcceptedReg();
    }

    public void setAcceptedReg(Boolean acceptedReg) {
        crew.setAcceptedReg(acceptedReg);
    }

    public String getPhone() {
        return crew.getPhone();
    }

    public void setPhone(String phone) {
        crew.setPhone(phone);
    }

    public String getDriverName() {
        return crew.getDriverName();
    }

    public void setDriverName(String driverName) {
        crew.setDriverName(driverName);
    }

    public Year getYearOfProduction() {
        return crew.getYearOfProduction();
    }

    public void setYearOfProduction(Year yearOfProduction) {
        crew.setYearOfProduction(yearOfProduction);
    }

    public EventLanguageCode getDescription() {
        return crew.getDescription();
    }

    public void setDescription(EventLanguageCode description) {
        crew.setDescription(description);
    }

    public String getPhoto() {
        return crew.getPhoto();
    }

    public void setPhoto(String photo) {
        crew.setPhoto(photo);
    }

    public String getCar() {
        return crew.getCar();
    }

    public void setCar(String car) {
        crew.setCar(car);
    }

    public Integer getNumber() {
        return crew.getNumber();
    }

    public void setNumber(Integer number) {
        crew.setNumber(number);
    }

    public Event getEvent() {
        return crew.getEvent();
    }

    public void setEvent(Event event) {
        crew.setEvent(event);
    }

    public List<CrewCategory> getCategories() {
        return crew.getCategories();
    }


    public void setCategories(List<CrewCategory> categories) {
        crew.setCategories(categories);
    }

    public List<Category> getActualCategories() {
        return actualCategories;
    }

    public void setActualCategories(List<Category> actualCategories) {
        this.actualCategories = actualCategories;
    }
}
