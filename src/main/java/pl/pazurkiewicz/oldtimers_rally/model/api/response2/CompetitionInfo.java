package pl.pazurkiewicz.oldtimers_rally.model.api.response2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.pazurkiewicz.oldtimers_rally.model.*;
import pl.pazurkiewicz.oldtimers_rally.model.api.serializer.DatabaseModelIdSerializer;
import pl.pazurkiewicz.oldtimers_rally.model.api.serializer.EventLanguageCodeAdvanceSerializer;

import java.util.List;

public interface CompetitionInfo {
    Integer getId();

    Integer getAbsencePoints();

    Double getAverageSpeed();

    Integer getMaxRankingPoints();

    CompetitionTypeEnum getType();

    @JsonSerialize(using = EventLanguageCodeAdvanceSerializer.class)
    EventLanguageCode getName();

    @JsonSerialize(using = EventLanguageCodeAdvanceSerializer.class)
    EventLanguageCode getDescription();

    List<CompetitionFieldInfo> getFields();

    @JsonSerialize(using = DatabaseModelIdSerializer.class)
    @JsonProperty("eventId")
    Event getEvent();

    Boolean getPossibleInvalid();


    interface CompetitionFieldInfo {
        Integer getId();

        @JsonSerialize(using = EventLanguageCodeAdvanceSerializer.class)
        EventLanguageCode getLabel();

        @JsonSerialize(using = DatabaseModelIdSerializer.class)
        @JsonProperty("competitionId")
        Competition getCompetition();

        Integer getOrder();

        CompetitionFieldTypeEnum getType();
    }
}
