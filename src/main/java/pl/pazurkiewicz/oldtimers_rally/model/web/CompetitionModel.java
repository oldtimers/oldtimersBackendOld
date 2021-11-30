package pl.pazurkiewicz.oldtimers_rally.model.web;

import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import pl.pazurkiewicz.oldtimers_rally.model.*;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;

import javax.validation.Valid;
import java.util.Hashtable;
import java.util.List;

public class CompetitionModel {
    private static final Hashtable<String, CompetitionTypeEnum> typeEnums = new Hashtable<>() {{
        put("Regular drive", CompetitionTypeEnum.REGULAR_DRIVE);
        put("Best min", CompetitionTypeEnum.BEST_MIN);
        put("Best max", CompetitionTypeEnum.BEST_MAX);
        put("Counted", CompetitionTypeEnum.COUNTED);
    }};
    private static final Hashtable<String, CompetitionFieldTypeEnum> fieldEnums = new Hashtable<>() {{
        put("Boolean", CompetitionFieldTypeEnum.BOOLEAN);
        put("Float", CompetitionFieldTypeEnum.FLOAT);
        put("Integer", CompetitionFieldTypeEnum.INT);
        put("Timer", CompetitionFieldTypeEnum.TIMER);

    }};
    @Valid
    private final Competition competition;

    private final List<EventLanguage> languages;

    public CompetitionModel(Event event) {
        languages = event.getEventLanguages();
        languages.sort(new EventLanguageComparator());
        competition = new Competition(event);
        competition.setType(CompetitionTypeEnum.COUNTED);
    }

    public CompetitionModel(Competition competition, Event event) {
        languages = event.getEventLanguages();
        languages.sort(new EventLanguageComparator());
        this.competition = competition;
        competition.getName().prepareForLoad(languages);
        competition.getDescription().prepareForLoad(languages);
        competition.getFields().forEach(f -> f.getLabel().prepareForLoad(languages));
    }

    public List<EventLanguage> getLanguages() {
        return languages;
    }

    public Hashtable<String, CompetitionFieldTypeEnum> getFieldEnums() {
        return fieldEnums;
    }

    public Competition getCompetition() {
        return competition;
    }

    public Hashtable<String, CompetitionTypeEnum> getTypeEnums() {
        return typeEnums;
    }

    public void addField() {
        List<CompetitionField> fields = competition.getFields();
        if (fields.size() < 5) {
            fields.add(new CompetitionField(competition, fields.size(), languages));
        }
    }

    public void preUpdate() {
        competition.preUpdate();
    }


    public void validate(SmartValidator smartValidator, BindingResult bindingResult) {
        switch (competition.getType()) {
            case BEST_MAX:
            case BEST_MIN:
                smartValidator.validate(competition, bindingResult, Competition.BestCategory.class);
                break;
            case REGULAR_DRIVE:
                smartValidator.validate(competition, bindingResult, Competition.RegularCategory.class);
                break;
            case COUNTED:
                smartValidator.validate(competition, bindingResult, Competition.CountedCategory.class);
        }
    }
}
