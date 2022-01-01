package pl.pazurkiewicz.oldtimers_rally.model.api.request;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

public class RegScoreRequest {
    @NotNull
    @Transient
    private ReqScoreEnum position;
    @NotNull
    private Integer crewId;
    @NotNull
    private Integer competitionId;
    @NotNull
    private Double time;

    public RegScoreRequest() {
    }

    public ReqScoreEnum getPosition() {
        return position;
    }

    public void setPosition(ReqScoreEnum position) {
        this.position = position;
    }

    public Integer getCrewId() {
        return crewId;
    }

    public void setCrewId(Integer crewId) {
        this.crewId = crewId;
    }

    public Integer getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Integer competitionId) {
        this.competitionId = competitionId;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
