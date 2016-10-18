package org.fobbyal.msolver.sovler.tree;

import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ValueCache;


/**
 * Created with IntelliJ IDEA.
 * User: aliang
 * Date: 8/8/2016
 * Time: 11:58 AM
 */
public class IfOperation<N> extends DynamicNumberMember<N> {

    private final Predicate<N> predicate;
    private final NumberMSolverMember<N> thenMember;
    private final NumberMSolverMember<N> elseMember;

    public IfOperation(Predicate<N> predicate, NumberMSolverMember<N> thenMember, NumberMSolverMember<N> elseMember) {
        this.predicate = predicate;
        this.thenMember = thenMember;
        this.elseMember = elseMember;
    }

    @Override
    public MSolverResult<N> eval(ValueCache<N> context) {

        return predicate.eval(context).flatMap(
                boolValue -> boolValue ? thenMember.eval(context) : elseMember.eval(context)
        );
    }

    @Override
    protected String[] findVars() {
        return concat(concat(predicate.getVars(), thenMember.getVars()), elseMember.getVars());
    }

/*    @Override
    public String toString() {
        return "if(" + predicate + "," + thenMember + "," + elseMember + ")";
    }*/
}
