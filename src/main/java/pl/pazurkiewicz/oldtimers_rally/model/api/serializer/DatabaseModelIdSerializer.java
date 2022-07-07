package pl.pazurkiewicz.oldtimers_rally.model.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.pazurkiewicz.oldtimers_rally.model.DatabaseModel;

import java.io.IOException;

public class DatabaseModelIdSerializer extends StdSerializer<DatabaseModel> {
    public DatabaseModelIdSerializer() {
        this(null);
    }

    protected DatabaseModelIdSerializer(Class<DatabaseModel> t) {
        super(t);
    }

    @Override
    public void serialize(DatabaseModel model, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeNumber(model.getId());
    }
}
