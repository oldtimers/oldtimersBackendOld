package pl.pazurkiewicz.oldtimers_rally.model.web;

import net.minidev.json.annotate.JsonIgnore;
import pl.pazurkiewicz.oldtimers_rally.model.Category;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CrewsModel implements ListWebModel<CrewModel> {
    @Valid
    private final List<CrewModel> crews;
    private final Set<Integer> deletedPrivileges = new HashSet<>();
    private final List<Category> possibleCategories;
    @JsonIgnore
    private final Event event;
    private final List<EventLanguage> languages;
    @Valid
    private CrewModel newCrew;

    public CrewsModel(List<Crew> crews, List<Category> possibleCategories, Event event) {
        languages = event.getEventLanguages();
        languages.sort(new EventLanguageComparator());
        this.possibleCategories = possibleCategories;
        this.event = event;
        this.crews = crews.stream().map(c -> new CrewModel(c, languages, possibleCategories)).collect(Collectors.toList());
        this.newCrew = new CrewModel(generateNewCrew(event), languages, possibleCategories);
    }

    private Crew generateNewCrew(Event event) {
        Crew crew = new Crew(event);
        crew.setAcceptedReg(true);
        crew.setAcceptedRodo(true);
        crew.setPhone("123465798");
        return crew;
    }

    public List<EventLanguage> getLanguages() {
        return languages;
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
        newCrew = new CrewModel(generateNewCrew(event), languages, possibleCategories);
    }

    public CrewModel getNewCrew() {
        return newCrew;
    }

    public void setNewCrew(CrewModel newCrew) {
        this.newCrew = newCrew;
    }

    public void preUpdate(Collection<Category> categories) {
        crews.forEach(c -> c.preUpdate(categories));
    }

    public Event getEvent() {
        return event;
    }
}
