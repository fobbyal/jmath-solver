package org.fobbyal.msolver;

import org.fobbyal.msolver.sovler.value.InvalidResult;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aliang
 * Date: 8/16/2016
 * Time: 3:03 PM
 */
public class MSolverMatches {

    public static <V> Matcher<MSolverResult<V>> validMatcher() {
        return new BaseMatcher<MSolverResult<V>>() {
            @Override
            public boolean matches(Object o) {
                MSolverResult<V> result = (MSolverResult<V>) o;
                return result.isValid();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" should be invalid");
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                MSolverResult<V> result = (MSolverResult<V>) item;
                description.appendValue(result);
            }
        };

    }

    public static <V> Matcher<Map<String, MSolverResult<V>>> validMatcher(String varName) {
        return new BaseMatcher<Map<String, MSolverResult<V>>>() {
            @Override
            public boolean matches(Object o) {
                //noinspection unchecked
                Map<String, MSolverResult<V>> map = (Map<String, MSolverResult<V>>) o;
                return map.get(varName).isValid();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(varName + " should be invalid");
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                //noinspection unchecked
                Map<String, MSolverResult<V>> map = (Map<String, MSolverResult<V>>) item;
                description.appendText(varName + " was ").appendValue(map.get(varName));
            }
        };
    }

//    public static Matcher<Map<String, MSolverResult<Double>>> validDouble(String varName) {
//        return new BaseMatcher<Map<String, MSolverResult<Double>>>() {
//            @Override
//            public boolean matches(Object o) {
//                //noinspection unchecked
//                Map<String, MSolverResult<Double>> map = (Map<String, MSolverResult<Double>>) o;
//                return map.get(varName).isValid();
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText(varName + " should be valid");
//            }
//
//            @Override
//            public void describeMismatch(Object item, Description description) {
//                //noinspection unchecked
//                Map<String, MSolverResult<Double>> map = (Map<String, MSolverResult<Double>>) item;
//                description.appendText(varName + " was ").appendValue(map.get(varName));
//            }
//        };
//    }


    public static Matcher<Map<String, MSolverResult<Double>>> varCloseTo(String varName, double value) {
        return varMarcher(varName, Matchers.closeTo(value, Math.abs(value * 0.000001)));
    }

    static <T> Matcher<Map<String, MSolverResult<T>>> varMarcher(String varName, Matcher<T> matcher) {
        return new BaseMatcher<Map<String, MSolverResult<T>>>() {
            @Override
            public boolean matches(Object o) {
                //noinspection unchecked
                Map<String, MSolverResult<T>> map = (Map<String, MSolverResult<T>>) o;
                MSolverResult<T> actual = map.get(varName);
                return actual != null && actual.isValid() && matcher.matches(actual.unwrap());
            }

            @Override
            public void describeTo(Description description) {
                matcher.describeTo(description.appendText(varName + " should  "));
            }

            @SuppressWarnings("unchecked")
            @Override
            public void describeMismatch(Object item, Description description) {
                Map<String, MSolverResult<T>> map = (Map<String, MSolverResult<T>>) item;
                description.appendText(varName + " was ").appendValue(map.getOrDefault(varName, InvalidResult.of
                        ("not in formula scope")));
            }
        };

    }

    public static Matcher<Map<String, MSolverResult<BigDecimal>>> varCloseTo(String varName, String bdStr) {
        return varCloseTo(varName, new BigDecimal(bdStr));
    }

    public static Matcher<Map<String, MSolverResult<BigDecimal>>> varCloseTo(String varName, BigDecimal bd) {
        return varMarcher(varName, Matchers.closeTo(bd, bd.multiply(new BigDecimal(0.0000000001)).abs()));
    }

    public static Matcher<Map<String, MSolverResult<BigDecimal>>> varEquals(String varName, String bdStr) {
        return varEquals(varName, new BigDecimal(bdStr));
    }

    public static Matcher<Map<String, MSolverResult<BigDecimal>>> varEquals(String varName, BigDecimal expected) {
        return varMarcher(varName, Matchers.equalTo(expected));

    }

}
