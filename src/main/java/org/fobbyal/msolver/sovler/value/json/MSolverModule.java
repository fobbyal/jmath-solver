package org.fobbyal.msolver.sovler.value.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.fobbyal.msolver.sovler.value.MSolverResult;

/**
 * Created by fobbyal
 * Creation Time 8/31/16 11:18 PM
 * Project for jmath-solver.
 */
@SuppressWarnings("unchecked")
public class MSolverModule extends SimpleModule {
    public MSolverModule() {
        super("MSolver");
        addSerializer(MSolverResult.class, new MSolverResultSerializer(null));
        addDeserializer(MSolverResult.class, new MSolverResultDeSerializer<>(MSolverResultDeSerializer.bdExtractor));
    }
}
