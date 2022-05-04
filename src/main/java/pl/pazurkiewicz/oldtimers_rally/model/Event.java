package pl.pazurkiewicz.oldtimers_rally.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.*;
import pl.pazurkiewicz.oldtimers_rally.exception.InvalidEventConfiguration;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Table(name = "events")
@Entity
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
public class Event implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<EventLanguage> eventLanguages = new ArrayList<>();

    @OneToMany(mappedBy = "event")
    @Where(clause = "is_default = true")
    private List<EventLanguage> defaultLanguage;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "name_id")
    private EventLanguageCode name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "description_id")
    private EventLanguageCode description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "main_photo", length = 128)
    private String mainPhoto;

    @Type(type = "json")
    @Column(name = "photos", columnDefinition = "json")
    private List<String> photos = new LinkedList<>();

    @Column(name = "url", nullable = false, length = 64)
    private String url = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "stage", nullable = false, columnDefinition = "enum")
    private StageEnum stage = StageEnum.NEW;

    @Column(name = "max_crew_number", nullable = false)
    private Integer maxCrewNumber = 20;

    public Integer getMaxCrewNumber() {
        return maxCrewNumber;
    }

    public void setMaxCrewNumber(Integer maxCrewNumber) {
        this.maxCrewNumber = maxCrewNumber;
    }

    public StageEnum getStage() {
        return stage;
    }

    public void setStage(StageEnum stage) {
        this.stage = stage;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<EventLanguage> getEventLanguages() {
        return eventLanguages;
    }

    public void setEventLanguages(List<EventLanguage> eventLanguages) {
        this.eventLanguages = eventLanguages;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public EventLanguageCode getDescription() {
        return description;
    }

    public void setDescription(EventLanguageCode description) {
        this.description = description;
    }

    public EventLanguageCode getName() {
        return name;
    }

    public void setName(EventLanguageCode name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public EventLanguage getDefaultLanguage() {
//        return getEventLanguages().stream().filter(EventLanguage::getIsDefault).findAny().orElseThrow(() -> new InvalidEventConfiguration("Event does not contain default language"));
//    }


    public EventLanguage getDefaultLanguage() {
        if (defaultLanguage.isEmpty()) {
            throw new InvalidEventConfiguration("Event does not contain default language");
        } else if (defaultLanguage.size() > 1) {
            throw new InvalidEventConfiguration("Event contains multiple default languages");
        }
        return defaultLanguage.get(0);
    }

    public void setDefaultLanguage(List<EventLanguage> defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }
}
