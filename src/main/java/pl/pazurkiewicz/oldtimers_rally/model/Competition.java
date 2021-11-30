package pl.pazurkiewicz.oldtimers_rally.model;

import pl.pazurkiewicz.oldtimers_rally.validator.IsFunctionValid;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Table(name = "competitions")
@Entity
@IsFunctionValid
public class Competition implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id", nullable = false)
    @Valid
    private EventLanguageCode name;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id", nullable = false)
    @Valid
    private EventLanguageCode description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "enum")
    @NotNull
    private CompetitionTypeEnum type;

    @Column(name = "absence_points", nullable = false)
    @NotNull
    private Integer absencePoints = 50;

    @Column(name = "max_ranking_points")
    @NotNull(groups = {BestCategory.class, RegularCategory.class})
    private Integer maxRankingPoints;

    @Column(name = "number_of_subsets")
    @Positive(groups = {BestCategory.class, RegularCategory.class})
    private Integer numberOfSubsets;

    @Column(name = "distance")
    @Positive(groups = {RegularCategory.class})
    private Double averageSpeed;

    @Column(name = "average_speed")
    @Positive(groups = {RegularCategory.class})
    private Double distance;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order asc")
    @Valid
    private List<CompetitionField> fields = new ArrayList<>();

    @Column(name = "function_code", length = 300)
    @NotBlank(groups = {BestCategory.class, CountedCategory.class})
    private String functionCode = "0";

    public Competition() {
    }

    public Competition(Event event) {
        this.event = event;
        this.description = EventLanguageCode.generateNewEventLanguageCode(event.getEventLanguages());
        this.name = EventLanguageCode.generateNewEventLanguageCode(event.getEventLanguages());
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public List<CompetitionField> getFields() {
        return fields;
    }

    public void setFields(List<CompetitionField> fields) {
        this.fields = fields;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
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

    public void preUpdate() {
        this.name.preUpdate();
        this.description.preUpdate();
        this.fields.forEach(CompetitionField::preUpdate);
    }
//
//    public List<CompetitionField> getFields() {
//        return fields;
//    }
//
//    public void setFields(List<CompetitionField> fields) {
//        this.fields = fields;
//    }

    public interface BestCategory {
    }

    public interface RegularCategory {
    }

    public interface CountedCategory {
    }

}
