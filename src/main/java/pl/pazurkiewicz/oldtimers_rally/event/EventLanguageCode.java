package pl.pazurkiewicz.oldtimers_rally.event;

import pl.pazurkiewicz.oldtimers_rally.models.Dictionary;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "event_language_codes")
@Entity
public class EventLanguageCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "code", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Dictionary> dictionaries = new ArrayList<>();

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
}
