package pl.pazurkiewicz.oldtimers_rally.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import pl.pazurkiewicz.oldtimers_rally.validator.IsFieldRequired;

import javax.persistence.*;

@Table(name = "dictionaries", indexes = {
        @Index(name = "dictionaries_event_language_id_code_uindex", columnList = "event_language_id, code_id", unique = true)
})
@Entity
@IsFieldRequired
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dictionary implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_language_id", nullable = false, updatable = false)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private EventLanguage eventLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", nullable = false)
    private EventLanguageCode code;

    @Column(name = "value", nullable = false, length = 16777215, columnDefinition = "mediumtext")
    private String value = "";

    public static Dictionary generateNewDictionary(EventLanguage eventLanguage, EventLanguageCode code) {
        Dictionary dictionary = new Dictionary();
        dictionary.setCode(code);
        dictionary.setEventLanguage(eventLanguage);
        return dictionary;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EventLanguageCode getCode() {
        return code;
    }

    public void setCode(EventLanguageCode code) {
        this.code = code;
    }

    public EventLanguage getEventLanguage() {
        return eventLanguage;
    }

    public void setEventLanguage(EventLanguage eventLanguage) {
        this.eventLanguage = eventLanguage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
