package pl.pazurkiewicz.oldtimers_rally;

import pl.pazurkiewicz.oldtimers_rally.models.Competition;
import pl.pazurkiewicz.oldtimers_rally.event.EventLanguageCode;

import javax.persistence.*;

@Table(name = "competition_fields")
@Entity
public class CompetitionField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @ManyToOne(optional = false)
    @JoinColumn(name = "label_id", nullable = false)
    private EventLanguageCode label;

    @Enumerated(javax.persistence.EnumType.ORDINAL)
    @Column(name = "type", nullable = false, columnDefinition = "enum")
    private FieldTypeEnum type;

    public FieldTypeEnum getType() {
        return type;
    }

    public void setType(FieldTypeEnum type) {
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
}
enum FieldTypeEnum{
    floatField, intField, booleanField
}
