package pl.pazurkiewicz.oldtimers_rally.event;

import javax.persistence.*;

@Table(name = "event_language_codes")
@Entity
public class EventLanguageCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
