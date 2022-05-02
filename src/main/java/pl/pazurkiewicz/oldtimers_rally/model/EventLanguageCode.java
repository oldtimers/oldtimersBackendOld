package pl.pazurkiewicz.oldtimers_rally.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.DictionaryComparator;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Table(name = "event_language_codes")
@Entity
//@Cacheable
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventLanguageCode implements DatabaseModel {
    private final static Logger logger = LoggerFactory.getLogger(EventLanguageCode.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Valid
    @OneToMany(mappedBy = "code", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
        List<Dictionary> toRemove = dictionaries.stream()
                .filter(dictionary -> !eventLanguages.contains(dictionary.getEventLanguage()))
                .peek(dictionary -> dictionary.setEventLanguage(null))
                .collect(Collectors.toList());
        dictionaries.removeAll(toRemove);
        for (EventLanguage eventLanguage : eventLanguages) {
            if (dictionaries.stream().filter(dictionary -> dictionary.getEventLanguage().equals(eventLanguage)).findFirst().isEmpty()) {
                dictionaries.add(Dictionary.generateNewDictionary(eventLanguage, this));
            }
        }
        dictionaries.sort(new DictionaryComparator());
    }

    public String getDictionary(Locale locale) {
        for (Dictionary dictionary : dictionaries) {
            if (Objects.equals(dictionary.getEventLanguage().getLanguage().getCode(), locale.getLanguage())) {
                return dictionary.getValue();
            }
        }
        return dictionaries.stream().filter(dictionary -> dictionary.getEventLanguage().getIsDefault())
                .findFirst()
                .map(Dictionary::getValue).orElseGet(this::getAnyDictionary);
    }

    private String getAnyDictionary() {
        if (dictionaries.isEmpty()) {
            logger.error("Invalid configuration for code: " + this.id);
            return "No dictionary!!!";
        } else {
            return dictionaries.get(0).getValue();
        }
    }

    @PreUpdate
    public void preUpdate() {
        dictionaries.removeIf(dictionary -> dictionary.getValue().isBlank());
    }


    public void prepareForLoad(List<EventLanguage> possibleLanguages) {
        for (EventLanguage possible : possibleLanguages) {
            if (dictionaries.stream()
                    .map(dictionary -> dictionary.getEventLanguage().getId())
                    .noneMatch(integer -> Objects.equals(integer, possible.getId()))) {
                dictionaries.add(Dictionary.generateNewDictionary(possible, this));
            }
        }
        dictionaries.sort(new DictionaryComparator());
    }
}
