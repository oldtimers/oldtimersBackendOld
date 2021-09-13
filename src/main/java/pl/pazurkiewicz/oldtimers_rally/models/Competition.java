package pl.pazurkiewicz.oldtimers_rally.models;

import javax.persistence.*;

@Table(name = "competitions")
@Entity
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "name_id", nullable = false)
    private EventLanguageCode name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "description_id", nullable = false)
    private EventLanguageCode description;

    @Enumerated(javax.persistence.EnumType.ORDINAL)
    @Column(name = "type", nullable = false, columnDefinition = "enum")
    private CompetitionTypeEnum type;

    @Column(name = "absence_points", nullable = false)
    private Integer absencePoints;

    @Column(name = "time", nullable = false)
    private Integer time;

    @Column(name = "max_ranking_points", nullable = false)
    private Integer maxRankingPoints;

    @Enumerated(javax.persistence.EnumType.ORDINAL)
    @Column(name = "current_state", nullable = false, columnDefinition = "enum")
    private CurrentStateEnum currentState;

    @Column(name = "column_8")
    private Integer column8;

    @Column(name = "number_of_subsets")
    private Integer numberOfSubsets;

    @Column(name = "might_be_invalid", nullable = false)
    private Boolean mightBeInvalid = false;

    @Column(name = "additiona1")
    private Double additiona1;

    @Column(name = "additional2")
    private Double additional2;

    @Column(name = "additional3")
    private Double additional3;

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

    public Double getAdditiona1() {
        return additiona1;
    }

    public void setAdditiona1(Double additiona1) {
        this.additiona1 = additiona1;
    }

    public Boolean getMightBeInvalid() {
        return mightBeInvalid;
    }

    public void setMightBeInvalid(Boolean mightBeInvalid) {
        this.mightBeInvalid = mightBeInvalid;
    }

    public Integer getNumberOfSubsets() {
        return numberOfSubsets;
    }

    public void setNumberOfSubsets(Integer numberOfSubsets) {
        this.numberOfSubsets = numberOfSubsets;
    }

    public Integer getColumn8() {
        return column8;
    }

    public void setColumn8(Integer column8) {
        this.column8 = column8;
    }

    public CurrentStateEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(CurrentStateEnum currentState) {
        this.currentState = currentState;
    }

    public Integer getMaxRankingPoints() {
        return maxRankingPoints;
    }

    public void setMaxRankingPoints(Integer maxRankingPoints) {
        this.maxRankingPoints = maxRankingPoints;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
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
enum CurrentStateEnum{
    before, during, after
}
enum CompetitionTypeEnum{
    regularDrive, timer, bestMin, bestMax, counted
}
