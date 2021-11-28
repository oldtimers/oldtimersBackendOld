package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CrewListModel implements ListWebModel<CrewModel> {
    @Valid
    private final List<CrewModel> crews;
    private final Set<Integer> deletedPrivileges = new HashSet<>();
    private final List<Category> possibleCategories;
    private final Event event;
    @Valid
    private CrewModel newCrew;


    public CrewListModel(List<Crew> crews, List<Category> possibleCategories, Event event) {
        this.possibleCategories = possibleCategories;
        this.event = event;
        this.crews = crews.stream().map(c -> new CrewModel(c, possibleCategories)).collect(Collectors.toList());
        this.newCrew = new CrewModel(new Crew(event), possibleCategories);
    }

    public List<CrewModel> getCrews() {
        return crews;
    }

    public Set<Integer> getDeletedCrews() {
        return deletedPrivileges;
    }

    public void removeCrew(Integer removeId) {
        removeFromList(removeId, crews, deletedPrivileges);
    }

    public void acceptNewCrew() {
        crews.add(newCrew);
        newCrew = new CrewModel(new Crew(event), possibleCategories);
    }

    public CrewModel getNewCrew() {
        return newCrew;
    }

    public void setNewCrew(CrewModel newCrew) {
        this.newCrew = newCrew;
    }
}
