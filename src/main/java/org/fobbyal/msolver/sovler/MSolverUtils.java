package org.fobbyal.msolver.sovler;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.fobbyal.msolver.antl.MSolverLexer;
import org.fobbyal.msolver.antl.MSolverParser;
import org.fobbyal.msolver.sovler.tree.NumberMSolverMember;
import org.fobbyal.msolver.sovler.value.SolverFormula;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created with IntelliJ IDEA.
 * User: aliang
 * Date: 8/16/2016
 * Time: 3:19 PM
 */
public class MSolverUtils<N> {

    private static Logger log = getLogger(MSolverUtils.class);

    private final NumSpec<N> spec;

    public MSolverUtils(NumSpec<N> spec) {
        this.spec = spec;
    }

    public Map<String, NumberMSolverMember<N>> compile(String input) {
        return compile(new ANTLRInputStream(input));
    }

    public Map<String, NumberMSolverMember<N>> compile(InputStream input) throws IOException {
        return compile(new ANTLRInputStream(input));
    }

   public List<SolverFormula> parseFormula(InputStream stream) {
        ArrayList<SolverFormula> fList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0 && line.indexOf('=') > 0) {
                    SolverFormula formula = toFormula(line);
                    if (formula != null)
                        fList.add(formula);
                }

            }
        } catch (IOException e) {
            log.error("error reading formulas", e);
        }
        return fList;
    }

    SolverFormula toFormula(String value) {
        Map<String, NumberMSolverMember<N>> compiledFormula = compile(value);
        if (compiledFormula.size() > 0) {
            String body = value.substring(value.indexOf('=') + 1).trim();
            Map.Entry<String, NumberMSolverMember<N>> next = compiledFormula.entrySet().iterator().next();
            String varName = next.getKey();
            return new SolverFormula(varName, body, next.getValue().getVarSet());
        }
        return null;
    }


    public Map<String, NumberMSolverMember<N>> compile(ANTLRInputStream input) {
        MSolverLexer lexer = new MSolverLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MSolverParser parser = new MSolverParser(tokens);
        MSolverParser.MSolverContext tree = parser.mSolver();
        MSolverCompiler<N> compiler = new MSolverCompiler<>(spec);
        return compiler.compile(tree);
    }

    public MSolver<N> createSolver(String ident, InputStream inputStream) throws IOException {
        return new MSolver<>(ident, compile(inputStream));
    }

    public MSolver<N> createSolver(String formula) {
        return new MSolver<>("anonymous", compile(formula));
    }

    public MSolver<N> createSolver(String ident, String formula) {
        return new MSolver<>(ident, compile(formula));
    }
}
