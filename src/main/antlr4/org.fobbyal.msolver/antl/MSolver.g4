grammar MSolver;

/*
@header {
package org.fobbyal.msolver;
}
*/

/* Grammar rules */

mSolver : declaration* EOF;


declaration: var_name ASSIGN value_expr LB?;

meta_info
:DECIMAL#SigDigits
|IDENTIFIER #MathContext
;

var_name: IDENTIFIER (LBRACKET meta_info RBRACKET)?;


if_expr : IF LPAREN predicate_expr COMMA value_expr COMMA value_expr RPAREN ;

/*func_expr
    : IDENTIFIER LPAREN param_value RPAREN # UniaryFunction
    | IDENTIFIER LPAREN param_value COMMA param_value  RPAREN # BinaryFunction
    | IDENTIFIER LPAREN param_value COMMA param_value COMMA param_value RPAREN # TenaryFunction
    | IDENTIFIER LPAREN param_value COMMA param_value COMMA param_value COMMA param_value RPAREN # QuaternionFunction;

param_value: value_expr;*/

func_expr : IDENTIFIER LPAREN param_value* RPAREN;

param_value: value_expr COMMA?;



predicate_expr
 : NOT predicate_expr                # NotPredicate
 | comparison_predicate              # ComparisonPredicate
 | LPAREN predicate_expr RPAREN      # GroupedPredicate
 | predicate_expr AND predicate_expr # And
 | predicate_expr OR predicate_expr  # Or
 | simple_predicate                  # TrueOrFalse
 ;

comparison_predicate : value_expr comp_operator value_expr;

comp_operator
 : GT # GreaterThen
 | GE # GreaterOrEqual
 | LT # LessThen
 | LE # LessOrEqual
 | EQ # Equal
 | NEQ # NotEqual;


value_expr
 : LPAREN value_expr RPAREN    # GroupedArithmetic
 | if_expr                     # If
 | func_expr                   # Function
 | value_expr POWER value_expr # Power
 | MINUS value_expr # Negation
 | value_expr DIV value_expr   # Division
 | value_expr MULT value_expr  # Multiplication
 | value_expr MINUS value_expr # Subtraction
 | value_expr PLUS value_expr  # Addition
 | numeric_entity              # Constant
 ;


simple_predicate
: TRUE  # True
| FALSE # False;

numeric_entity : DECIMAL              # NumericConst
               | IDENTIFIER           # NumericVariable
               | PI                   # PiValue
               | E                    # EValue
               ;

/* Lexical rules */

IF   : 'if' ;

AND : 'and' | '&&' | 'AND' ;
OR  : 'or' | '||' | 'OR' ;

TRUE  : 'true' ;
FALSE : 'false' ;

MULT  : '*' ;
DIV   : '/' ;
PLUS  : '+' ;
MINUS : '-' ;
POWER : '^' ;


GT : '>' ;
GE : '>=' ;
LT : '<' ;
LE : '<=' ;
EQ : '==' ;
NEQ : '!=' | '<>' ;
NOT : '!' ;

LPAREN : '(' ;
RPAREN : ')' ;

LBRACKET : '{';
RBRACKET : '}';

PI: 'pi' | 'PI';
E: 'e' | 'E';

// DECIMAL, IDENTIFIER, COMMENTS, WS are set using regular expression

DECIMAL : [0-9]+('.'[0-9]+('E'('-')?[0-9]+)?)? ;

INTEGER : [0-9]+;


IDENTIFIER : [a-zA-Z_][a-zA-Z_0-9]*;

COMMA : ',' ;

ASSIGN : '=';

LB: '\n';

// COMMENT and WS are stripped from the output token stream by sending
// to a different channel 'skip'

COMMENT : '//' .+? ('\n'|EOF) -> skip ;



WS : [ \r\t\u000C\n]+ -> skip ;

