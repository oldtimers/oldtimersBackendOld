package pl.pazurkiewicz.oldtimers_rally.model.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.pazurkiewicz.oldtimers_rally.model.QrCode;

import java.io.IOException;

public class CrewQrSerializer extends StdSerializer<QrCode> {
    public CrewQrSerializer() {
        this(null);
    }

    protected CrewQrSerializer(Class<QrCode> t) {
        super(t);
    }

    @Override
    public void serialize(QrCode qrCode, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(qrCode.getQr());
    }
}
