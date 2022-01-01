package pl.pazurkiewicz.oldtimers_rally.model.api.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;
import pl.pazurkiewicz.oldtimers_rally.model.Language;
import pl.pazurkiewicz.oldtimers_rally.model.StageEnum;
import pl.pazurkiewicz.oldtimers_rally.model.api.serializer.EventLanguageCodeSerializer;

import java.time.LocalDateTime;

public interface EventInfo {
    Integer getId();

    LocalDateTime getEndDate();

    String getMainPhoto();

    StageEnum getStage();

    LocalDateTime getStartDate();

    String getUrl();

    @JsonSerialize(using = EventLanguageCodeSerializer.class)
    EventLanguageCode getName();

    @JsonSerialize(using = EventLanguageCodeSerializer.class)
    EventLanguageCode getDescription();

//    List<EventLanguageInfo> getEventLanguages();


    interface EventLanguageInfo {
        Integer getId();

        Language getLanguage();

        Boolean isIsDefault();
    }
}
