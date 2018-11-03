package org.fobbyal.msolver.sovler.value.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.fobbyal.msolver.sovler.value.MSolverResult;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by fobbyal
 * Creation Time 8/24/16 12:18 AM
 * Project for jmath-solver.
 */
public class MSolverResultSerializer extends StdSerializer<MSolverResult> {

    public MSolverResultSerializer(Class<MSolverResult> t) {
        super(t);
    }

    @Override
    public void serialize(MSolverResult vCalcResult, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (vCalcResult.isValid()) {
            Object unwrap = vCalcResult.unwrap();
            if (unwrap instanceof BigDecimal) {
                jsonGenerator.writeString(((BigDecimal) unwrap).stripTrailingZeros().toPlainString());
            } else {
                jsonGenerator.writeString(unwrap.toString());
            }
        } else {
            jsonGenerator.writeString(vCalcResult.toString());
        }

    }

}
