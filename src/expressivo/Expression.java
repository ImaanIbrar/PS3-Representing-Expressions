/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import java.util.Map;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
	
	// Datatype definition:
    //   Expression = Value(num:double)
    //                + Variable(id:String)
    //                + Addition(left:Expression, right:Expression)
    //                + Multiplication(left:Expression, right:Expression)
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        throw new RuntimeException("unimplemented");
    }
    
    /**
     * Appends an expression to the end of this expression using addition.
     * 
     * If the provided expression, e, is equal to Expression.emptyExpression() corrected to 5 decimal places, 
     * the empty expression is returned.
     * If e is equal to this expression, a new expression equivalent to
     *      this * 2 is returned.
     *      
     * @param e a non-null, non-empty string representing a valid expression syntax.
     * @return a simplified expression equivalent to:
     *           this + e
     *      Note that neither this nor e are modified.
     */
    public Expression addExpr(Expression e);

    
    
    /**
     * Concatenates an expression to the end of this expression using multiplication.
     * 
     * If the provided expression, e, is equal to Expression.emptyExpression() corrected to 5 decimal places, 
     * the empty expression is returned.
     * If e is equal to Expression.parse("1") corrected to 5 decimal places, this
     * expression is returned.
     * For any other expression except the two mentioned above, the resulting expression is not simplified,
     * and it is equivalent to:
     *      (this)*(e)
     * Note: This lack of simplification is not applicable during parsing, where an expression is simplified
     * as much as possible.
     * 
     * @param e a non-null, non-empty string representing a valid expression syntax.
     * @return a new expression equivalent to:
     *           this * e
     *      The returned expression is NOT simplified.
     *      Neither this nor e are modified.
     */
    public Expression multiplyExpr(Expression e);
    
    
    /**
     * Prepends a variable to the beginning of this expression using addition.
     * 
     * @param variable a non-null, non-empty case-sensitive string consisting of letters, a-zA-Z.
     * @return a new expression resulting from the insertion of a variable at the start
     *         of this expression with an addition.
     *         The expression is not simplified.
     */
    public Expression addVariable(String variable);
    
    /**
     * Appends a variable as a multiplicative factor to start of this expression
     * 
     * @param variable non-null non-empty case-sensitive string of letters, a-zA-Z
     * @return the product expression of this and variable, variable being at
     *         the head of the expression. The expression is not simplified
     */
    public Expression multiplyVariable(String variable);
    
    /**
     * Incorporates a number at the beginning of this expression.
     * 
     * @param num a non-negative integer or floating-point number.
     * @return the result of adding num to the start of this expression.
     *      If num is equal to Expression.emptyExpression(), corrected to 5 decimal places, 
     *      the empty expression is returned;
     *      The expression is not simplified.
     */
    public Expression addConstant(double num);
    
    
    /**
     * Adds a number as a multiplicative factor to the beginning of this expression.
     * 
     * @param num a non-negative integer or floating-point number.
     * @return the product expression where num serves as the coefficient at the start of this expression.
     *      - If num is equal to Expression.emptyExpression(), corrected to 5 decimal places, 
     *        the empty expression is returned;
     *      - If num is equal to Expression.parse("1"), corrected to 5 decimal places, this
     *        expression is returned.
     *      The expression is simplified.
     */
    public Expression appendCoefficient(double num);
 
    
    /**
     * Provides a string representation of this expression.
     * 
     * The returned string adheres to the following conventions:
     *   - For additions, there is exactly one space between
     *     operands and the operator:
     *          operand + operand 
     *   - For multiplications, no space exists between operands
     *     and the operator, and operands are enclosed in parentheses:
     *          (factor)*(factor)
     *     Factors of products are naturally grouped from left to right:
     *          x*x*x -> ((x)*(x))*(x)
     * Numbers in the string are truncated and adjusted to 5 decimal places.
     * 
     * @return a parseable representation of this expression, ensuring that
     *         for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * Checks if an object is equal to this addition expression.
     * Two expressions are considered equal if and only if: 
     *   - The expressions contain the same variables, numbers, and operators;
     *   - Those variables, numbers, and operators are in the same order, read left-to-right;
     *   - And they are grouped in the same manner.
     * Two sums are equal even if they have different groupings but the same mathematical meaning. For example, 
     *     (3 + 4) + 5 and 3 + (4 + 5) are equal.
     * However, two products are NOT equal if they have different groupings, regardless
     * of mathematical meaning. For example:
     *     x*(2*y) is not equal to (x*2)*y 
     * 
     * @param thatObject any object.
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, following the definition in the PS3 handout.
     */

    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
}
