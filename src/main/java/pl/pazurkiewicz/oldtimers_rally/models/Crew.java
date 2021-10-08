package pl.pazurkiewicz.oldtimers_rally.models;

import com.vladmihalcea.hibernate.type.basic.YearType;
import org.hibernate.annotations.TypeDef;
import pl.pazurkiewicz.oldtimers_rally.event.Event;

import javax.persistence.*;
import java.time.Year;

@Table(name = "crews", indexes = {
        @Index(name = "crews_event_id_number_uindex", columnList = "event_id, number", unique = true)
})
@Entity
@TypeDef(
        name = "year",
        typeClass = YearType.class,
        defaultForType = Year.class
)
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "number")
    private Integer number;

    @Column(name = "car", nullable = false, length = 64)
    private String car;

    @Column(name = "photo", length = 128)
    private String photo;

    @Column(name = "description", length = 16777215, columnDefinition = "mediumtext")
    private String description;

    @Column(name = "year_of_production", nullable = false, columnDefinition = "year")
    private Year yearOfProduction;

    @Column(name = "driver_name", length = 64)
    private String driverName;

    @Column(name = "phone", nullable = false, length = 16)
    private String phone;

    @Column(name = "accepted_reg", nullable = false)
    private Boolean acceptedReg = false;

    @Column(name = "accepted_rodo", nullable = false)
    private Boolean acceptedRodo = false;

    public Boolean getAcceptedRodo() {
        return acceptedRodo;
    }

    public void setAcceptedRodo(Boolean acceptedRodo) {
        this.acceptedRodo = acceptedRodo;
    }

    public Boolean getAcceptedReg() {
        return acceptedReg;
    }

    public void setAcceptedReg(Boolean acceptedReg) {
        this.acceptedReg = acceptedReg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Year getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(Year yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
