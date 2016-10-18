package org.fobbyal.msolver.sovler.value.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.fobbyal.msolver.sovler.value.InvalidResult;
import org.fobbyal.msolver.sovler.value.MSolverResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Created by fobbyal
 * Creation Time 8/31/16 10:26 PM
 * Project for jmath-solver.
 */
public class MSolverResultDeSerializer<V> extends StdDeserializer<MSolverResult<V>> {
    public static final Function<String, MSolverResult<BigDecimal>> bdExtractor = str -> {
        try {
            return MSolverResult.of(new BigDecimal(str));
        } catch (Exception e) {
            return InvalidResult.of(str);
        }
    };

    public static final Function<String, MSolverResult<Double>> dblExtractor = str -> {
        try {
            return MSolverResult.of(new Double(str));
        } catch (Exception e) {
            return InvalidResult.of(str);
        }
    };
    Function<String, MSolverResult<V>> extractor;


    public MSolverResultDeSerializer(Function<String, MSolverResult<V>> extractor) {
        this(null, extractor);
    }

    public MSolverResultDeSerializer(Class<?> vc, Function<String, MSolverResult<V>> extractor) {
        super(vc);
        this.extractor = extractor;
    }

    @Override
    public MSolverResult<V> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode value = jsonParser.getCodec().readTree(jsonParser);
        return extractor.apply(value.asText());
    }
}
