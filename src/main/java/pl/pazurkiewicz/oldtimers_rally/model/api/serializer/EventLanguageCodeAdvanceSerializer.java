package pl.pazurkiewicz.oldtimers_rally.model.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.context.i18n.LocaleContextHolder;
import pl.pazurkiewicz.oldtimers_rally.model.Dictionary;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguageCode;

import java.io.IOException;

public class EventLanguageCodeAdvanceSerializer extends StdSerializer<EventLanguageCode> {

    public EventLanguageCodeAdvanceSerializer() {
        this(null);
    }

    protected EventLanguageCodeAdvanceSerializer(Class<EventLanguageCode> t) {
        super(t);
    }

    @Override
    public void serialize(EventLanguageCode value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("value", value.getDictionary(LocaleContextHolder.getLocale()));
        jgen.writeArrayFieldStart("dictionaries");
        for (Dictionary dictionary : value.getDictionaries()) {
            jgen.writeStartObject();
            jgen.writeStringField("value", dictionary.getValue());
            jgen.writeStringField("language", dictionary.getEventLanguage().getLanguage().getCode());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }
}
