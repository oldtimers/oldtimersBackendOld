package pl.pazurkiewicz.oldtimers_rally.model.web;

import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import pl.pazurkiewicz.oldtimers_rally.model.*;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;

import javax.validation.Valid;
import java.util.Hashtable;
import java.util.List;

public class CompetitionModel {
    private static final Hashtable<CompetitionTypeEnum, String> typeEnums = new Hashtable<>() {{
        put(CompetitionTypeEnum.REGULAR_DRIVE, "type.regular_drive");
        put(CompetitionTypeEnum.BEST_MIN, "type.best_min");
        put(CompetitionTypeEnum.BEST_MAX, "type.best_max");
        put(CompetitionTypeEnum.COUNTED, "type.counted");
    }};
    private static final Hashtable<CompetitionFieldTypeEnum, String> fieldEnums = new Hashtable<>() {{
        put(CompetitionFieldTypeEnum.BOOLEAN, "field.boolean");
        put(CompetitionFieldTypeEnum.FLOAT, "field.float");
        put(CompetitionFieldTypeEnum.INT, "field.int");
        put(CompetitionFieldTypeEnum.TIMER, "field.timer");
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

    public Hashtable<CompetitionFieldTypeEnum, String> getFieldEnums() {
        return fieldEnums;
    }

    public Competition getCompetition() {
        return competition;
    }

    public Hashtable<CompetitionTypeEnum, String> getTypeEnums() {
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

    public void removeField(Integer deleteId) {
        List<CompetitionField> fields = competition.getFields();
        if (fields.size() > deleteId && deleteId >= 0) {
            fields.remove(deleteId.intValue());
        }
    }
}
