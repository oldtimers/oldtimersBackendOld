package pl.pazurkiewicz.oldtimers_rally.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Table(name = "event_languages", indexes = {
        @Index(name = "event_languages_event_id_language_id_uindex", columnList = "event_id, language_id", unique = true)
})
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventLanguage implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false, updatable = false)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @Fetch(FetchMode.SELECT)
    private Language language;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @OneToMany(mappedBy = "eventLanguage", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Dictionary> dictionaries;

    public EventLanguage() {
    }

    public EventLanguage(Event event, Language language, Boolean isDefault) {
        this.event = event;
        this.language = language;
        this.isDefault = isDefault;
    }

    public Set<Dictionary> getDictionaries() {
        return dictionaries;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Language getLanguage() {
        return language;
    }


    public void setLanguage(Language language) {
        this.language = language;
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


    public Integer getPriority() {
        if (this.getIsDefault()) {
            return Integer.MIN_VALUE;
        } else if (this.id == null) {
            return Integer.MAX_VALUE;
        } else {
            return this.getLanguage().getId();
        }
    }
}
