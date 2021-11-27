package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Crew;

import java.util.List;

public class CrewsWrapper {
    private final List<Crew> crews;

    public CrewsWrapper(List<Crew> crews) {
        this.crews = crews;
    }

    public List<Crew> getCrews() {
        return crews;
    }

}
