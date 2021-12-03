package pl.pazurkiewicz.oldtimers_rally.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "competition_fields", indexes = {
        @Index(name = "competition_fields_competition_id_label_id_uindex", columnList = "competition_id, order_info", unique = true)
})
@Entity
public class CompetitionField implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", nullable = false)
    @Valid
    private EventLanguageCode label;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "enum")
    @NotNull
    private CompetitionFieldTypeEnum type;

    @Column(name = "order_info", nullable = false)
    @Range(min = 0, max = 4)
    private Integer order;

    public CompetitionField() {
    }

    public CompetitionField(Competition competition, Integer order, List<EventLanguage> languages) {
        this.competition = competition;
        this.order = order;
        this.label = EventLanguageCode.generateNewEventLanguageCode(languages);
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public CompetitionFieldTypeEnum getType() {
        return type;
    }

    public void setType(CompetitionFieldTypeEnum type) {
        this.type = type;
    }

    public EventLanguageCode getLabel() {
        return label;
    }

    public void setLabel(EventLanguageCode label) {
        this.label = label;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void preUpdate() {
        label.preUpdate();
    }
}
