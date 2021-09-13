package pl.pazurkiewicz.oldtimers_rally.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.Instant;

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

    @ManyToOne
    @JoinColumn(name = "name_id")
    private EventLanguageCode name;

    @ManyToOne
    @JoinColumn(name = "description_id")
    private EventLanguageCode description;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "main_photo", length = 128)
    private String mainPhoto;

    @Type(type = "json")
    @Column(name = "photos", columnDefinition = "json")
    private JsonNode photos;

    @ManyToOne(optional = false)
    @JoinColumn(name = "default_language_id", nullable = false)
    private Language defaultLanguage;

    @Column(name = "qr_code_template", length = 128)
    private String qrCodeTemplate;

    @Column(name = "nr_template", length = 128)
    private String nrTemplate;

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

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
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

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
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
