# Java Math Formula Solver
## Overview
1. Multi-expression math parser. (all intermediate values are saved in results)
2. Produces Tree based evaluator that can be reused
3. Supports custom number implementations BigDecimal, Double. Can be easily extended to use number classes
4. Supports per expression Rounding mode or MathContext
5. Supports simple logical If(boolean,truthy,falsy) expressions

## Project status.
1. BigDecimal trigs are not yet implemented actively looking for better approximation then Double to BigDecimal
2. Test Coverage at 91% however there still maybe edge cases that are not accounted for.

# Antrl4 Dependency
Antrl4 is utilized to create a flexible lexer and parser which is open for adding features in the future

#TODO
1. look into using immutables instead current value classes
2. need better docs
