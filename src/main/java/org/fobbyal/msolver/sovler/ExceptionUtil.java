package org.fobbyal.msolver.sovler;

import org.fobbyal.msolver.sovler.value.InvalidResult;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ResultException;

import java.util.function.Supplier;

/**
 * Created by fobbyal
 * Creation Time 8/15/16 12:57 AM
 * Project for jmath-solver.
 */
public class ExceptionUtil {

    @SuppressWarnings("unchecked")
    public static <N> MSolverResult<N> guard(Supplier<MSolverResult<N>> supplier) {
        try {
            return supplier.get();
        } catch (ResultException e) {
            return InvalidResult.of(e.getMessage());
        } catch (Throwable e) {
            return InvalidResult.of(e);
        }
    }
}
