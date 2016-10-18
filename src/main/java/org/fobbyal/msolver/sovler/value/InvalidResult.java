package org.fobbyal.msolver.sovler.value;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.fobbyal.msolver.sovler.value.InvalidResult.InvalidType.*;

/**
 * Created by fobbyal
 * Creation Time 8/3/16 11:22 PM
 * Project for jmath-solver.
 */
public class InvalidResult extends MSolverResult {

    private String message;
    private InvalidType type;

    @SuppressWarnings("unchecked")
    private InvalidResult(String message, InvalidType type) {
        super(null);
        this.message = message;
        this.type = type;
    }

    public static InvalidResult of() {
        return new InvalidResult("NULL", NULL);
    }

    public static InvalidResult of(String message) {
        return new InvalidResult(message, OTHER);
    }

    public static InvalidResult of(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return new InvalidResult("Exception Reached: " + writer.toString(), EXCEPTION);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvalidResult that = (InvalidResult) o;

        return message.equals(that.message);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Object unwrap() {
        throw new RuntimeException(message);
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    @SuppressWarnings("unused")
    public InvalidType getType() {
        return type;
    }

    @Override
    public String toString() {
        return message;
    }

    enum InvalidType {
        NULL, EXCEPTION, OTHER
    }
}
