package pl.pazurkiewicz.oldtimers_rally.models;

import pl.pazurkiewicz.oldtimers_rally.event.Event;

import javax.persistence.*;

@Table(name = "qr_codes", indexes = {
        @Index(name = "qr_codes_qr_uindex", columnList = "qr", unique = true)
})
@Entity
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "qr", nullable = false, length = 128)
    private String qr;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "crew_id")
    private Crew crew;

    public Crew getCrew() {
        return crew;
    }

    public void setCrew(Crew crew) {
        this.crew = crew;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
