package org.fobbyal.msolver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by fobbyal
 * Creation Time 8/6/16 7:07 AM
 * Project for jmath-solver.
 */

public class MathExpressionGenerator {

    private static final Random randomGenerator = new Random();
    private int minElementsCount;
    private int maxElementCount;
    private int minWholeDigits;
    private int maxWholeDigits;

    public MathExpressionGenerator() {
        this(5, 15, 1, 10);
    }

    private MathExpressionGenerator(int minElementsCount, int maxElementCount, int minWholeDigits, int maxWholeDigits) {
        this.minElementsCount = minElementsCount;
        this.maxElementCount = maxElementCount;
        this.minWholeDigits = minWholeDigits;
        this.maxWholeDigits = maxWholeDigits;
    }


    public List<Expression> generate(int numberOfQuestions) {
        List<Expression> randomExpressions = new ArrayList<>(numberOfQuestions);
        for (int i = 0; i < numberOfQuestions; i++) {
            int randomQuestionElementsCapacity = getCapacity();
            Expression expression = new Expression();


            int openCount = 0;

            for (int j = 0; j < randomQuestionElementsCapacity; j++) {
                boolean isLastIteration = j + 1 == randomQuestionElementsCapacity;
                if (randomGenerator.nextInt(10) == 1) {
                    expression.addElement(new Delim("("));
                    openCount++;
                }

                expression.addElement(new Value(getRandomValue()));

                if (openCount > 0 && randomGenerator.nextInt(10) == 1) {
                    expression.addElement(new Delim(")"));
                    openCount--;
                }
                if (!isLastIteration) expression.addElement(new Delim(Operator.random().getDisplayValue()));
                if (isLastIteration) {
                    while (openCount > 0) {
                        expression.addElement(new Delim(")"));
                        openCount--;
                    }
                }
            }
            randomExpressions.add(expression);
        }
        return randomExpressions;
    }

    private int getCapacity() {
        return getRandomIntegerFromRange(minElementsCount, maxElementCount);
    }

    private double getRandomValue() {
        return randomGenerator.nextDouble() * Math.pow(10d, getRandomIntegerFromRange(minWholeDigits, maxWholeDigits)
                + 1);
        //return getRandomIntegerFromRange(1, 100) * (randomGenerator.nextInt(100)%2==0?-1:1);
    }

    private int getRandomIntegerFromRange(int min, int max) {
        return randomGenerator.nextInt(max - min + 1) + min;
    }

    private enum Operator {

        PLUS("+"), MINUS("-"), MULTIPLIER("*"), DIVIDER("/");
        private String displayValue;

        Operator(String displayValue) {
            this.displayValue = displayValue;
        }

        public static Operator random() {
            return Operator.values()[randomGenerator.nextInt(Operator.values().length)];
        }

        public String getDisplayValue() {
            return " " + displayValue + " ";
        }
    }

    public static class Expression {

        private List<QuestionElement> questions;

        private Expression() {
            questions = new ArrayList<>();
        }

        private void addElement(QuestionElement questionElement) {
            questions.add(questionElement);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            questions.forEach(sb::append);
            return sb.toString().trim();
        }
    }

    static class QuestionElement {

    }

    private static class Delim extends QuestionElement {
        String deli;

        Delim(String deli) {
            this.deli = deli;
        }

        @Override
        public String toString() {
            return deli;
        }
    }

    private static class Value extends QuestionElement {
        static final DecimalFormat format = new DecimalFormat("#.#########");


        private double value;

        private Value(double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return format.format(value);
        }
    }
}


