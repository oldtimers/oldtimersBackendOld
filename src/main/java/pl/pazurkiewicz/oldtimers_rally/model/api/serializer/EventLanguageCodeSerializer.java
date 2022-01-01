package pl.pazurkiewicz.oldtimers_rally.model.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.context.i18n.LocaleContextHolder;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;

import java.io.IOException;

public class EventLanguageCodeSerializer extends StdSerializer<EventLanguageCode> {

    public EventLanguageCodeSerializer() {
        this(null);
    }

    protected EventLanguageCodeSerializer(Class<EventLanguageCode> t) {
        super(t);
    }

    @Override
    public void serialize(EventLanguageCode value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.getDictionary(LocaleContextHolder.getLocale()));
    }
}
