package pl.pazurkiewicz.oldtimers_rally.model.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pl.pazurkiewicz.oldtimers_rally.model.CompetitionField;
import pl.pazurkiewicz.oldtimers_rally.model.CompetitionTypeEnum;
import pl.pazurkiewicz.oldtimers_rally.model.Score;

import java.io.IOException;

public class ScoreSerializer extends StdSerializer<Score> {
    public ScoreSerializer() {
        this(null);
    }

    protected ScoreSerializer(Class<Score> t) {
        super(t);
    }

    @Override
    public void serialize(Score value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeNumberField("competitionId", value.getCompetition().getId());
        jgen.writeNumberField("crewId", value.getCompetition().getId());
        jgen.writeObjectField("result", value.getResult());
        if (value.getCompetition().getType() == CompetitionTypeEnum.REGULAR_DRIVE) {
            jgen.writeObjectField("timeStart", value.getAdditional1());
            jgen.writeObjectField("timeEnd", value.getAdditional2());
            jgen.writeObjectField("average", value.getAdditional3());
        } else {
            jgen.writeBooleanField("invalidResult", value.isInvalidResult());
            if (!value.isInvalidResult()) {
                for (CompetitionField competitionField : value.getCompetition().getFields()) {
                    String fieldName = competitionField.getOrder().toString();
                    jgen.writeObjectField(fieldName, castToObject(competitionField, value));
                }
            }
        }
        jgen.writeEndObject();
    }

    private Object castToObject(CompetitionField field, Score value) {
        Double fieldValue;
        switch (field.getOrder()) {
            case 0:
                fieldValue = value.getAdditional1();
                break;
            case 1:
                fieldValue = value.getAdditional2();
                break;
            case 2:
                fieldValue = value.getAdditional3();
                break;
            case 3:
                fieldValue = value.getAdditional4();
                break;
            case 4:
                fieldValue = value.getAdditional5();
                break;
            default:
                fieldValue = null;
        }
        if (fieldValue == null)
            throw new RuntimeException("Invalid ScoreSerializer 1");
        switch (field.getType()) {
            case FLOAT:
            case TIMER:
                return fieldValue;
            case INT:
            case DATETIME:
                return fieldValue.intValue();
            case BOOLEAN:
                return fieldValue == 1.0;
        }
        throw new RuntimeException("Invalid ScoreSerializer 2");
    }


}
