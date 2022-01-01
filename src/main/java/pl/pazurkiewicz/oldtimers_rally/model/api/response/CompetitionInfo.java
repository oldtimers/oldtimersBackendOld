package pl.pazurkiewicz.oldtimers_rally.model.api.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.pazurkiewicz.oldtimers_rally.model.CompetitionFieldTypeEnum;
import pl.pazurkiewicz.oldtimers_rally.model.CompetitionTypeEnum;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;
import pl.pazurkiewicz.oldtimers_rally.model.api.serializer.EventLanguageCodeSerializer;

import java.util.List;

public interface CompetitionInfo {
    Integer getId();

    Integer getAbsencePoints();

    Double getAverageSpeed();

    Integer getMaxRankingPoints();

    CompetitionTypeEnum getType();

    @JsonSerialize(using = EventLanguageCodeSerializer.class)
    EventLanguageCode getName();

    @JsonSerialize(using = EventLanguageCodeSerializer.class)
    EventLanguageCode getDescription();

    List<CompetitionFieldInfo> getFields();


    interface CompetitionFieldInfo {
        Integer getId();

        @JsonSerialize(using = EventLanguageCodeSerializer.class)
        EventLanguageCode getLabel();

        Integer getOrder();

        CompetitionFieldTypeEnum getType();
    }
}
