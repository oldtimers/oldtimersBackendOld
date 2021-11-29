package pl.pazurkiewicz.oldtimers_rally.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "crews", indexes = {
        @Index(name = "crews_event_id_number_uindex", columnList = "event_id, number", unique = true)
})
@Entity
public class Crew implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @Column(name = "number")
    private Integer number;
    @Column(name = "car", nullable = false, length = 64)
    @NotBlank
    private String car;
    @Column(name = "photo", length = 128)
    private String photo;
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "description", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Valid
    private EventLanguageCode description;
    @Column(name = "year_of_production", nullable = false)
    @NotNull
    private Integer yearOfProduction;
    @Column(name = "driver_name", nullable = false, length = 64)
    @NotBlank
    private String driverName;
    @Column(name = "phone", nullable = false, length = 16)
    @NotBlank
    private String phone;
    @Column(name = "accepted_reg", nullable = false)
    @NotNull
    @AssertTrue
    private Boolean acceptedReg = false;
    @Column(name = "accepted_rodo", nullable = false)
    @NotNull
    @AssertTrue
    private Boolean acceptedRodo = false;
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<CrewCategory> categories = new ArrayList<>();

    public Crew() {
    }

    public Crew(Event event) {
        this.event = event;
        this.description = EventLanguageCode.generateNewEventLanguageCode(event.getEventLanguages());
    }

    public Boolean getAcceptedRodo() {
        return acceptedRodo;
    }

    public void setAcceptedRodo(Boolean acceptedRodo) {
        this.acceptedRodo = acceptedRodo;
    }

    public Boolean getAcceptedReg() {
        return acceptedReg;
    }

    public void setAcceptedReg(Boolean acceptedReg) {
        this.acceptedReg = acceptedReg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(Integer yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public EventLanguageCode getDescription() {
        return description;
    }

    public void setDescription(EventLanguageCode description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CrewCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<CrewCategory> categories) {
        this.categories = categories;
    }

    @PreUpdate
    public void preUpdate() {
        description.preUpdate();
    }

    public List<CrewCategory> assignToCategories(Collection<Category> categories) {
        List<CrewCategory> result = new ArrayList<>();
        this.categories.removeIf(crewCategory -> {
            Category category = crewCategory.getCategory();
            if (category.getMode() == CategoryEnum.year && !category.containYear(this.yearOfProduction)) {
                crewCategory.setCrew(null);
                return true;
            }
            return false;
        });
        Set<Integer> yearCategoryIds = this.categories.stream()
                .map(CrewCategory::getCategory)
                .filter(category -> category.getMode() == CategoryEnum.year)
                .map(Category::getId).collect(Collectors.toSet());
        for (Category category : categories) {
            if (category.getMode() == CategoryEnum.year && category.containYear(this.yearOfProduction) && !yearCategoryIds.contains(category.getId())) {
                yearCategoryIds.add(category.getId());
                CrewCategory created = new CrewCategory(this, category);
                this.categories.add(created);
                result.add(created);
            }
        }
        return result;
    }

    public void preUpdate(Collection<Category> categories) {
        description.preUpdate();
        assignToCategories(categories);
    }
}
