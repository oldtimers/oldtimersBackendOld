package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrewsModel implements ListWebModel<Crew> {
    @Valid
    private final List<Crew> crews;
    private final Set<Integer> deletedPrivileges = new HashSet<>();
    @Valid
    private Crew newCrew;


    public CrewsModel(List<Crew> crews, Event event) {
        this.crews = crews;
        this.newCrew = new Crew(event);
    }

    public List<Crew> getCrews() {
        return crews;
    }

    public Set<Integer> getDeletedCrews() {
        return deletedPrivileges;
    }

    public void removeCrew(Integer removeId) {
        removeFromList(removeId, crews, deletedPrivileges);
    }

    public void acceptNewCrew(Event event) {
        crews.add(newCrew);
        newCrew = new Crew(event);
    }

    public Crew getNewCrew() {
        return newCrew;
    }

    public void setNewCrew(Crew newCrew) {
        this.newCrew = newCrew;
    }
}
