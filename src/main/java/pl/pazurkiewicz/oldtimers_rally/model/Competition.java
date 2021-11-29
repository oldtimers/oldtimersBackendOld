package pl.pazurkiewicz.oldtimers_rally.model;

import javax.persistence.*;
import javax.validation.Valid;

@Table(name = "competitions")
@Entity
public class Competition implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "name_id", nullable = false)
    @Valid
    private EventLanguageCode name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "description_id", nullable = false)
    @Valid
    private EventLanguageCode description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "enum")
    private CompetitionTypeEnum type;

    @Column(name = "absence_points", nullable = false)
    private Integer absencePoints;

    @Column(name = "max_ranking_points", nullable = false)
    private Integer maxRankingPoints;

    @Column(name = "number_of_subsets")
    private Integer numberOfSubsets;

    @Column(name = "additional1")
    private Double additional1;

    @Column(name = "additional2")
    private Double additional2;

    @Column(name = "additional3")
    private Double additional3;

    public Competition() {
    }

    public Competition(Event event) {
        this.event = event;
        this.description = EventLanguageCode.generateNewEventLanguageCode(event.getEventLanguages());
        this.name = EventLanguageCode.generateNewEventLanguageCode(event.getEventLanguages());
        this.type = CompetitionTypeEnum.counted;
    }

    public Double getAdditional3() {
        return additional3;
    }

    public void setAdditional3(Double additional3) {
        this.additional3 = additional3;
    }

    public Double getAdditional2() {
        return additional2;
    }

    public void setAdditional2(Double additional2) {
        this.additional2 = additional2;
    }

    public Double getAdditional1() {
        return additional1;
    }

    public void setAdditional1(Double additional1) {
        this.additional1 = additional1;
    }

    public Integer getNumberOfSubsets() {
        return numberOfSubsets;
    }

    public void setNumberOfSubsets(Integer numberOfSubsets) {
        this.numberOfSubsets = numberOfSubsets;
    }

    public Integer getMaxRankingPoints() {
        return maxRankingPoints;
    }

    public void setMaxRankingPoints(Integer maxRankingPoints) {
        this.maxRankingPoints = maxRankingPoints;
    }

    public Integer getAbsencePoints() {
        return absencePoints;
    }

    public void setAbsencePoints(Integer absencePoints) {
        this.absencePoints = absencePoints;
    }

    public CompetitionTypeEnum getType() {
        return type;
    }

    public void setType(CompetitionTypeEnum type) {
        this.type = type;
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
