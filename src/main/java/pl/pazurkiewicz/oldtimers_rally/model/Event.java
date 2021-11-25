package pl.pazurkiewicz.oldtimers_rally.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.*;
import pl.pazurkiewicz.oldtimers_rally.error.InvalidEventConfiguration;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<EventLanguage> eventLanguages = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "name_id")
    private EventLanguageCode name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
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
    private JsonNode photos;

    @Column(name = "qr_code_template", length = 128)
    private String qrCodeTemplate;

    @Column(name = "nr_template", length = 128)
    private String nrTemplate;

    @Column(name = "url", nullable = false, length = 64)
    private String url = "";

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

    public String getNrTemplate() {
        return nrTemplate;
    }

    public void setNrTemplate(String nrTemplate) {
        this.nrTemplate = nrTemplate;
    }

    public String getQrCodeTemplate() {
        return qrCodeTemplate;
    }

    public void setQrCodeTemplate(String qrCodeTemplate) {
        this.qrCodeTemplate = qrCodeTemplate;
    }

    public JsonNode getPhotos() {
        return photos;
    }

    public void setPhotos(JsonNode photos) {
        this.photos = photos;
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

    public EventLanguage getDefaultLanguage() {
        return eventLanguages.stream().filter(EventLanguage::getIsDefault).findAny().orElseThrow(() -> new InvalidEventConfiguration("Event does not contain default language"));
    }

}
