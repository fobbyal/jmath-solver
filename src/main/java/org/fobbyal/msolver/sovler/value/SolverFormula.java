package org.fobbyal.msolver.sovler.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/**
 * Created by fobbyal
 * Creation Time 10/19/16 7:09 PM
 * Project for jmath-solver.
 */
public class SolverFormula {

    @JsonProperty("varName")
    final String varName;
    @JsonProperty("formula")
    final String formula;
    @JsonProperty("dependencies")
    final Set<String> dependencies;

    @JsonCreator
    public SolverFormula(
            @JsonProperty("varName")
                    String varName,
            @JsonProperty("formula")
                    String formula,
            @JsonProperty("dependencies")
                    Set<String> dependencies) {
        this.varName = varName;
        this.formula = formula;
        this.dependencies = dependencies;
    }
}
