package pl.pazurkiewicz.oldtimers_rally.model;

import javax.persistence.*;

@Table(name = "scores", indexes = {
        @Index(name = "scores_result_index", columnList = "result")
})
@Entity
public class Score implements DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @Column(name = "invalid_result", nullable = false)
    private Boolean invalidResult = false;

    @Column(name = "error_occurred", nullable = false)
    private Boolean errorOccurred = false;

    @Column(name = "additional1")
    private Double additional1;

    @Column(name = "additional2")
    private Double additional2;

    @Column(name = "additional3")
    private Double additional3;

    @Column(name = "additional4")
    private Double additional4;

    @Column(name = "additional5")
    private Double additional5;

    @Column(name = "result")
    private Float result;

    public Float getResult() {
        return result;
    }

    public void setResult(Float result) {
        this.result = result;
    }

    public Double getAdditional5() {
        return additional5;
    }

    public void setAdditional5(Double additional5) {
        this.additional5 = additional5;
    }

    public Double getAdditional4() {
        return additional4;
    }

    public void setAdditional4(Double additional4) {
        this.additional4 = additional4;
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

    public Boolean getErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorOccurred(Boolean errorOccurred) {
        this.errorOccurred = errorOccurred;
    }

    public Boolean getInvalidResult() {
        return invalidResult;
    }

    public void setInvalidResult(Boolean invalidResult) {
        this.invalidResult = invalidResult;
    }

    public Crew getCrew() {
        return crew;
    }

    public void setCrew(Crew crew) {
        this.crew = crew;
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
