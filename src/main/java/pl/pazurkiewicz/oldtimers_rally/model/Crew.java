package pl.pazurkiewicz.oldtimers_rally.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Table(name = "crews")
@Entity
public class Crew implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @Column(name = "car", nullable = false, length = 64)
    @NotBlank
    private String car;
    @Column(name = "photo", length = 128)
    private String photo;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "description", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Valid
    private EventLanguageCode description;
    @Column(name = "year_of_production", nullable = false)
    @NotNull
    private Integer yearOfProduction;
    @Column(name = "driver_name", nullable = false, length = 64)
    @NotBlank
    private String driverName;

    @Column(name = "pilot_name", length = 64)
    private String pilotName;

    @Column(name = "club_name", length = 128)
    private String clubName;

    @Column(name = "phone", length = 16)
    private String phone;
    @Column(name = "accepted_reg", nullable = false)
    @NotNull
    @AssertTrue
    private Boolean acceptedReg = false;
    @Column(name = "accepted_rodo", nullable = false)
    @NotNull
    @AssertTrue
    private Boolean acceptedRodo = false;
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrewCategory> categories = new ArrayList<>();
    @OneToMany(mappedBy = "crew")
    private Set<Score> scores = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "qr_code")
    private QrCode qrCode;

    @Column(name = "present", nullable = false)
    private Boolean present = false;

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public QrCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }


    public String getPilotName() {
        return pilotName;
    }

    public void setPilotName(String pilotName) {
        this.pilotName = pilotName;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Crew() {
    }

    public Crew(Event event) {
        this.event = event;
        this.description = EventLanguageCode.generateNewEventLanguageCode(event.getEventLanguages());
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

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

    public Integer getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(Integer yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public EventLanguageCode getDescription() {
        return description;
    }

    public void setDescription(EventLanguageCode description) {
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

    public List<CrewCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<CrewCategory> categories) {
        this.categories = categories;
    }

    @PreUpdate
    @PrePersist
    public void preUpdate() {
        description.preUpdate();
    }

    public List<CrewCategory> assignToCategories(Collection<Category> categories) {
        List<CrewCategory> result = new ArrayList<>();
        this.categories.removeIf(crewCategory -> {
            Category category = crewCategory.getCategory();
            if (category.getMode() == CategoryEnum.year && !category.containYear(this.yearOfProduction)) {
                crewCategory.setCrew(null);
                return true;
            }
            return false;
        });
        Set<Integer> yearCategoryIds = this.categories.stream()
                .map(CrewCategory::getCategory)
                .filter(category -> category.getMode() == CategoryEnum.year)
                .map(Category::getId).collect(Collectors.toSet());
        for (Category category : categories) {
            if (category.getMode() == CategoryEnum.year && category.containYear(this.yearOfProduction) && !yearCategoryIds.contains(category.getId())) {
                yearCategoryIds.add(category.getId());
                CrewCategory created = new CrewCategory(this, category);
                this.categories.add(created);
                result.add(created);
            }
        }
        return result;
    }

    public void preUpdate(Collection<Category> categories) {
        description.preUpdate();
        assignToCategories(categories);
    }

    @Override
    public String toString() {
        return String.format("%s%s, %s - %d", ((qrCode == null) ? "" : qrCode.getNumber() + ", "), driverName, car, yearOfProduction);
    }
}
