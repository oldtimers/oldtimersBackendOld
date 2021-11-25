package pl.pazurkiewicz.oldtimers_rally.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.pazurkiewicz.oldtimers_rally.validator.IsFieldRequired;

import javax.persistence.*;
import java.util.Set;

@Table(name = "dictionaries", indexes = {
        @Index(name = "dictionaries_event_language_id_code_uindex", columnList = "event_language_id, code_id", unique = true)
})
@Entity
@IsFieldRequired(field = "value", isRequired = "eventLanguage.isDefault")
public class Dictionary implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "event_language_id", nullable = false)
    private EventLanguage eventLanguage;

    @ManyToOne(optional = false)
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
