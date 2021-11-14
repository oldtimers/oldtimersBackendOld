package pl.pazurkiewicz.oldtimers_rally.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Table(name = "event_language_codes")
@Entity
public class EventLanguageCode {
    private final static Logger logger = LoggerFactory.getLogger(EventLanguageCode.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Valid
    @OneToMany(mappedBy = "code", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.JOIN)
    private List<Dictionary> dictionaries = new ArrayList<>();

    public static EventLanguageCode generateNewEventLanguageCode(List<EventLanguage> eventLanguages) {
        EventLanguageCode eventLanguageCode = new EventLanguageCode();
        List<Dictionary> dictionaries = eventLanguages
                .stream().map(eventLanguage -> Dictionary.generateNewDictionary(eventLanguage, eventLanguageCode))
                .collect(Collectors.toList());
        eventLanguageCode.setDictionaries(dictionaries);
        return eventLanguageCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Dictionary> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(List<Dictionary> dictionaries) {
        this.dictionaries = dictionaries;
    }

    //    used for reload during Event creation
    public void reload(List<EventLanguage> eventLanguages) {
        dictionaries.removeIf(dictionary -> !eventLanguages.contains(dictionary.getEventLanguage()));
        for (EventLanguage eventLanguage : eventLanguages) {
            if (dictionaries.stream().filter(dictionary -> dictionary.getEventLanguage().equals(eventLanguage)).findFirst().isEmpty()) {
                dictionaries.add(Dictionary.generateNewDictionary(eventLanguage, this));
            }
        }
    }

    public String getDictionary(Locale locale) {
        for (Dictionary dictionary : dictionaries) {
            EventLanguage d = dictionary.getEventLanguage();

            if (Objects.equals(dictionary.getEventLanguage().getLanguage().getCode(), locale.getLanguage())) {
                return dictionary.getValue();
            }
        }
        return dictionaries.stream().filter(dictionary -> dictionary.getEventLanguage().getIsDefault())
                .findFirst()
                .map(Dictionary::getValue)
                .orElse(getAnyDictionary());
    }

    private String getAnyDictionary() {
        logger.error("Invalid configuration for code: " + this.id);
        if (dictionaries.isEmpty()) {
            return "No dictionary!!!";
        } else {
            return dictionaries.get(0).getValue();
        }
    }
}
