package pl.pazurkiewicz.oldtimers_rally.model;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.List;

@Table(name = "categories")
@Entity
public class Category implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "name_id", nullable = false)
    @Valid
    private EventLanguageCode name;

    @ManyToOne
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

    public Category(CategoryEnum mode, List<EventLanguage> languages) {
        this.mode = mode;
        this.setDescription(EventLanguageCode.generateNewEventLanguageCode(languages));
        this.setName(EventLanguageCode.generateNewEventLanguageCode(languages));
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
}

