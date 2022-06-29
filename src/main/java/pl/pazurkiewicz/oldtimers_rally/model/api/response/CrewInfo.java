package pl.pazurkiewicz.oldtimers_rally.model.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;
import pl.pazurkiewicz.oldtimers_rally.model.QrCode;
import pl.pazurkiewicz.oldtimers_rally.model.api.serializer.CrewNumberSerializer;
import pl.pazurkiewicz.oldtimers_rally.model.api.serializer.CrewQrSerializer;
import pl.pazurkiewicz.oldtimers_rally.model.api.serializer.DatabaseModelIdSerializer;
import pl.pazurkiewicz.oldtimers_rally.model.api.serializer.EventLanguageCodeSerializer;

public interface CrewInfo {
    Integer getId();

    String getCar();

    String getDriverName();

    @JsonIgnore
    QrCode getQrCode();

    @JsonSerialize(using = CrewNumberSerializer.class)
    default QrCode getNumber() {
        return getQrCode();
    }

    @JsonSerialize(using = CrewQrSerializer.class)
    default QrCode getQr() {
        return getQrCode();
    }

    String getPhone();

    Integer getYearOfProduction();

    @JsonSerialize(using = DatabaseModelIdSerializer.class)
    @JsonProperty("eventId")
    Event getEvent();

    @JsonSerialize(using = EventLanguageCodeSerializer.class)
    EventLanguageCode getDescription();
}
