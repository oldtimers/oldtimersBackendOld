package pl.pazurkiewicz.oldtimers_rally.event;

import pl.pazurkiewicz.oldtimers_rally.language.Language;

import javax.persistence.*;

@Table(name = "event_languages", indexes = {
        @Index(name = "event_languages_event_id_language_id_uindex", columnList = "event_id, language_id", unique = true)
})
@Entity
public class EventLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

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
}
