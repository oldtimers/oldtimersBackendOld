package pl.pazurkiewicz.oldtimers_rally.model.projection;

import pl.pazurkiewicz.oldtimers_rally.model.CompetitionCurrentStateEnum;
import pl.pazurkiewicz.oldtimers_rally.model.CompetitionTypeEnum;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;

public interface CompetitionShortInfo {
    Integer getId();

    CompetitionCurrentStateEnum getCurrentState();

    CompetitionTypeEnum getType();

    EventLanguageCode getDescription();

    EventLanguageCode getName();
}
