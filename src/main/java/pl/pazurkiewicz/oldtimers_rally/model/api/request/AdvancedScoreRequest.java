package pl.pazurkiewicz.oldtimers_rally.model.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.pazurkiewicz.oldtimers_rally.model.api.request2.ScoreRequest;

import javax.validation.constraints.NotNull;

public class AdvancedScoreRequest {
    @JsonProperty("0")
    private Object a;
    @JsonProperty("1")
    private Object b;
    @JsonProperty("2")
    private Object c;
    @JsonProperty("3")
    private Object d;
    @JsonProperty("4")
    private Object e;
    @NotNull
    private Integer competitionId;
    @NotNull
    private Integer crewId;

    private Boolean invalidResult = false;

    public AdvancedScoreRequest() {
    }

    public AdvancedScoreRequest(ScoreRequest scoreRequest, Integer competitionId, Integer crewId) {
        this.a = scoreRequest.getA();
        this.b = scoreRequest.getB();
        this.c = scoreRequest.getC();
        this.d = scoreRequest.getD();
        this.e = scoreRequest.getE();
        this.competitionId = competitionId;
        this.crewId = crewId;
    }

    public Object getA() {
        return a;
    }

    public void setA(Object a) {
        this.a = a;
    }

    public Object getB() {
        return b;
    }

    public void setB(Object b) {
        this.b = b;
    }

    public Object getC() {
        return c;
    }

    public void setC(Object c) {
        this.c = c;
    }

    public Object getD() {
        return d;
    }

    public void setD(Object d) {
        this.d = d;
    }

    public Object getE() {
        return e;
    }

    public void setE(Object e) {
        this.e = e;
    }

    public Integer getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Integer competitionId) {
        this.competitionId = competitionId;
    }

    public Integer getCrewId() {
        return crewId;
    }

    public void setCrewId(Integer crewId) {
        this.crewId = crewId;
    }

    public Boolean getInvalidResult() {
        return invalidResult;
    }

    public void setInvalidResult(Boolean invalidResult) {
        this.invalidResult = invalidResult;
    }
}
