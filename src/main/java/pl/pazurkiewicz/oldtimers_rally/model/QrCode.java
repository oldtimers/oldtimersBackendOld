package pl.pazurkiewicz.oldtimers_rally.model;

import javax.persistence.*;

@Table(name = "qr_codes", indexes = {
        @Index(name = "qr_codes_qr_uindex", columnList = "qr", unique = true)
})
@Entity
public class QrCode implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "qr", nullable = false, length = 128)
    private String qr;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "number", nullable = false)
    private Integer number;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "qrCode")
    private Crew crew;

    public Crew getCrew() {
        return crew;
    }

    public void setCrew(Crew crew) {
        this.crew = crew;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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
