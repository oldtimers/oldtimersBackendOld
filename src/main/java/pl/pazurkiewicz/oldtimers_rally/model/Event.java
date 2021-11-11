package pl.pazurkiewicz.oldtimers_rally.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "events")
@Entity
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    public List<EventLanguage> getEventLanguages() {
        return eventLanguages;
    }

    public void setEventLanguages(List<EventLanguage> eventLanguages) {
        this.eventLanguages = eventLanguages;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private List<EventLanguage> eventLanguages;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    private EventLanguageCode name;

    @ManyToOne(cascade = CascadeType.ALL)
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
    private String url;

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
}
