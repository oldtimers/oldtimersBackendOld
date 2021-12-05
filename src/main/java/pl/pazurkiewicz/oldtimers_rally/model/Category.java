package pl.pazurkiewicz.oldtimers_rally.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.pazurkiewicz.oldtimers_rally.validator.AreYearsValid;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Set;

@Table(name = "categories")
@Entity
@AreYearsValid
public class Category implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "name_id", nullable = false)
    @Valid
    private EventLanguageCode name;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "description_id", nullable = false)
    @Valid
    private EventLanguageCode description;

    @OneToMany(mappedBy = "category")
    private Set<CrewCategory> crewCategories;
    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false, columnDefinition = "enum")
    private CategoryEnum mode;
    @Column(name = "min_year")
    private Integer minYear;
    @Column(name = "max_year")
    private Integer maxYear;

    public Category() {
    }

    public Category(CategoryEnum mode, Event event) {
        this.mode = mode;
        this.event = event;
        this.setDescription(EventLanguageCode.generateNewEventLanguageCode(event.getEventLanguages()));
        this.setName(EventLanguageCode.generateNewEventLanguageCode(event.getEventLanguages()));
    }

    public Set<CrewCategory> getCrewCategories() {
        return crewCategories;
    }

    public void setCrewCategories(Set<CrewCategory> crewCategories) {
        this.crewCategories = crewCategories;
    }

    @PreUpdate
    public void preUpdate() {
        this.name.preUpdate();
        this.description.preUpdate();
    }

    public Integer getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(Integer maxYear) {
        this.maxYear = maxYear;
    }

    public Integer getMinYear() {
        return minYear;
    }

    public void setMinYear(Integer minYear) {
        this.minYear = minYear;
    }

    public CategoryEnum getMode() {
        return mode;
    }

    public void setMode(CategoryEnum mode) {
        this.mode = mode;
    }

    public EventLanguageCode getDescription() {
        return description;
    }

    public void setDescription(EventLanguageCode description) {
        this.description = description;
    }

    public EventLanguageCode getName() {
        return name;
    }

    public void setName(EventLanguageCode name) {
        this.name = name;
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

    public Boolean containYear(Integer year) {
        if (mode == CategoryEnum.year) {
            if (minYear != null && maxYear != null)
                return minYear <= year && year <= maxYear;
            else if (minYear != null) {
                return minYear <= year;
            } else if (maxYear != null) {
                return year <= maxYear;
            }
            return true;
        }
        return false;
    }
}

