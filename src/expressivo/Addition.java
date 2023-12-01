package expressivo;

import java.util.Map;

/**
 * An immutable type representing an addition expression.
 * 
 * <p>This class encapsulates the concept of an addition expression made up of two subexpressions.
 * The operands, represented by the left and right fields, are non-null and immutable expressions.
 * The class ensures safety from exposure by making all fields private and final. While the
 * representation invariant requires non-null operands, it is checked by the private method checkRep().
 * Instances of Addition share their representation with other implementations but do not modify it.</p>
 */
public class Addition implements Expression {
    private final Expression left;
    private final Expression right;

    // Abstraction Function:
    //   Represents an addition expression made up of two subexpressions.
    //
    // Representation Invariant:
    //   The left and right operands are non-null immutable expressions.
    //
    // Safety From Exposure:
    //   - All fields are private and final.
    //   - Left and right operands are immutable.
    //   - Addition shares its representation with other implementations, but they do not modify it.

    /**
     * Constructs an Addition expression with the specified left and right operands.
     * 
     * @param left  the left operand of the addition expression.
     * @param right the right operand of the addition expression.
     * 
     * @throws IllegalArgumentException if either left or right is null.
     */
    public Addition(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        checkRep();
    }

    /**
     * Combines this expression with another expression using addition.
     * If the other expression is zero, returns this expression.
     * If this and the other expression are the same, returns a new expression
     * equivalent to 2 * this.
     * If the left operand of this expression is the same as the other expression,
     * returns a new expression with the left operand multiplied by 2.
     * If the right operand of this expression is the same as the other expression,
     * returns a new expression with the right operand multiplied by 2.
     * Otherwise, returns a new addition expression of this and the other expression.
     * 
     * @param e an expression to add to this expression.
     * @return a simplified expression.
     */
    @Override
    public Expression addExpr(Expression e) {
        if (e.equals(new Value(0))) {
            return this;
        }
        Value two = new Value(2);
        if (this.equals(e)) {
            Expression newLeft = this.left.multiplyExpr(two);
            Expression newRight = this.right.multiplyExpr(two);
            return new Addition(newLeft, newRight);
        }
        if (this.left.equals(e)) {
            Expression newLeft = this.left.multiplyExpr(two);
            return new Addition(newLeft, this.right);
        }
        if (this.right.equals(e)) {
            Expression newRight = this.right.multiplyExpr(two);
            return new Addition(this.left, newRight);
        }
        return new Addition(this, e);
    }

    /**
     * Returns the result of combining this and another expression with a multiplication.
     * If the other expression is zero, returns zero.
     * If the other expression is one, returns this expression.
     * Otherwise, returns a new multiplication expression of this and the other expression.
     * 
     * @param e an expression to multiply with this expression.
     * @return a simplified expression.
     */
    @Override
    public Expression multiplyExpr(Expression e) {
        Value zero = new Value(0);
        if (e.equals(zero)) {
            return zero;
        }
        if (e.equals(new Value(1))) {
            return this;
        }
        return new Multiplication(this, e);
    }

    /**
     * Adds a variable to the beginning of this expression using addition.
     * 
     * @param variable a non-null, non-empty string representing a variable.
     * @return a new expression resulting from the insertion of a variable at the start
     *         of this expression with an addition.
     */
    @Override
    public Expression addVariable(String variable) {
        assert variable != null && !variable.isEmpty();
        return new Addition(new Variable(variable), this);
    }

    /**
     * Appends a variable as a multiplicative factor to the start of this expression.
     * 
     * @param variable a non-null, non-empty string representing a variable.
     * @return the product expression of this and the variable, with the variable at
     *         the head of the expression.
     */
    @Override
    public Expression multiplyVariable(String variable) {
        assert variable != null && !variable.isEmpty();
        return new Multiplication(new Variable(variable), this);
    }

    /**
     * Adds a constant number to the start of this expression.
     * 
     * @param num a non-negative integer or floating-point number.
     * @return the result of adding num to the start of this expression.
     */
    @Override
    public Expression addConstant(double num) {
        assert num >= 0 && Double.isFinite(num);
        Value valNum = new Value(num);
        if (valNum.equals(new Value(0))) {
            return this;
        }
        if (this.left.equals(valNum)) {
            Expression newLeft = this.left.addConstant(num);
            return new Addition(newLeft, this.right);
        }
        if (this.right.equals(valNum)) {
            Expression newRight = this.right.addConstant(num);
            return new Addition(this.left, newRight);
        }
        return new Addition(valNum, this);
    }

    /**
     * Appends a constant coefficient to this expression.
     * 
     * @param num a non-negative integer or floating-point number.
     * @return the result of appending num as a coefficient to this expression.
     */
    @Override
    public Expression appendCoefficient(double num) {
        assert num >= 0 && Double.isFinite(num);
        Value valNum = new Value(num);
        Value zero = new Value(0);
        if (valNum.equals(zero)) {
            return zero;
        }
        if (valNum.equals(new Value(1))) {
            return this;
        }
        return this.left.appendCoefficient(num)
                .addExpr(this.right.appendCoefficient(num));
    }

    /**
     * Returns a string representation of this addition expression.
     * 
     * @return a string representation of the expression.
     */
    @Override
    public String toString() {
        return left.toString() + " + " + right.toString();
    }

    /**
     * Checks if an object is equal to this addition expression.
     * Two expressions are equal if and only if: 
     *   - The expressions contain the same variables, numbers, and operators;
     *   - those variables, numbers, and operators are in the same order, read left-to-right;
     *   - and they are grouped in the same way.
     * Two addition objects are equal if having different groupings with 
     * the same mathematical meaning. For example, 
     *     (3 + 4) + 5 and 3 + (4 + 5) are equal.
     * 
     * @param thatObject Object to compare equality with.
     * @return true if two addition expressions are equal.
     */
    @Override
    public boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (!(thatObject instanceof Addition)) {
            return false;
        }
        Addition thatAdd = (Addition) thatObject;
        return this.toString().equals(thatAdd.toString());
    }

    /**
     * Returns a hash code value for this addition expression.
     * 
     * @return a hash code value for this expression.
     */
    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;
        result = prime * result + left.hashCode();
        result = prime * result + right.hashCode();
        return result;
    }

    private void checkRep() {
        assert left != null;
        assert right != null;
    }
}
