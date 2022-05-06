package pl.pazurkiewicz.oldtimers_rally.model.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.pazurkiewicz.oldtimers_rally.model.QrCode;

import java.io.IOException;

public class CrewNumberSerializer extends StdSerializer<QrCode> {
    public CrewNumberSerializer() {
        this(null);
    }

    protected CrewNumberSerializer(Class<QrCode> t) {
        super(t);
    }

    @Override
    public void serialize(QrCode qrCode, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeNumber(qrCode.getNumber());
    }
}
