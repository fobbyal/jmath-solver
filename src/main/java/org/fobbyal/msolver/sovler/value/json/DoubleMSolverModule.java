package org.fobbyal.msolver.sovler.value.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.fobbyal.msolver.sovler.value.MSolverResult;

/**
 * Created by fobbyal
 * Creation Time 8/31/16 11:23 PM
 * Project for jmath-solver.
 */
public class DoubleMSolverModule extends SimpleModule {
    @SuppressWarnings("unchecked")
    public DoubleMSolverModule() {
        super("MSolver");
        //addSerializer(MSolverResult.class, new MSolverResultSerializer<>(null));
        addSerializer(MSolverResult.class, new MSolverResultSerializer(null));
        addDeserializer(MSolverResult.class, new MSolverResultDeSerializer<>(MSolverResultDeSerializer.dblExtractor));
    }
}
