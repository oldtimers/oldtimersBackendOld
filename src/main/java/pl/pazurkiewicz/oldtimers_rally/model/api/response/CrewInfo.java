package pl.pazurkiewicz.oldtimers_rally.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;
import pl.pazurkiewicz.oldtimers_rally.model.QrCode;
import pl.pazurkiewicz.oldtimers_rally.model.api.serializer.CrewNumberSerializer;
import pl.pazurkiewicz.oldtimers_rally.model.api.serializer.EventLanguageCodeSerializer;

public interface CrewInfo {
    Integer getId();

    String getCar();

    String getDriverName();

    @JsonSerialize(using = CrewNumberSerializer.class)
    @JsonProperty("number")
    QrCode getQrCode();

    String getPhone();

    Integer getYearOfProduction();

    @JsonSerialize(using = EventLanguageCodeSerializer.class)
    EventLanguageCode getDescription();
}
