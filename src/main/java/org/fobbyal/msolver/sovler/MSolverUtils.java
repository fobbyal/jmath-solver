package org.fobbyal.msolver.sovler;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.fobbyal.msolver.antl.MSolverLexer;
import org.fobbyal.msolver.antl.MSolverParser;
import org.fobbyal.msolver.sovler.tree.NumberMSolverMember;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aliang
 * Date: 8/16/2016
 * Time: 3:19 PM
 */
public class MSolverUtils<N> {

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


    public Map<String, NumberMSolverMember<N>> compile(ANTLRInputStream input) {
        MSolverLexer lexer = new MSolverLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MSolverParser parser = new MSolverParser(tokens);
        MSolverParser.MSolverContext tree = parser.mSolver();
        MSolverCompiler<N> compiler = new MSolverCompiler<>(spec);
        return compiler.compile(tree);
    }

    public MSolver<N> createSolver(InputStream inputStream) throws IOException {
        return new MSolver<>(compile(inputStream));
    }

    public MSolver<N> createSolver(String formula) {
        return new MSolver<>(compile(formula));
    }
}
