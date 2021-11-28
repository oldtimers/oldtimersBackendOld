package pl.pazurkiewicz.oldtimers_rally.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.pazurkiewicz.oldtimers_rally.validator.AreYearsTyped;
import pl.pazurkiewicz.oldtimers_rally.validator.AreYearsValid;

import javax.persistence.*;
import javax.validation.Valid;

@Table(name = "categories")
@Entity
@AreYearsValid
@AreYearsTyped
public class Category implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "name_id", nullable = false)
    @Valid
    private EventLanguageCode name;

    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "description_id", nullable = false)
    @Valid
    private EventLanguageCode description;

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
            } else {
                return year <= maxYear;
            }
        }
        return false;
    }
}

