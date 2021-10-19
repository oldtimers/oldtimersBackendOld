package pl.pazurkiewicz.oldtimers_rally.event;

import pl.pazurkiewicz.oldtimers_rally.models.dictionary.Dictionary;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "event_language_codes")
@Entity
public class EventLanguageCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "code", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
}
