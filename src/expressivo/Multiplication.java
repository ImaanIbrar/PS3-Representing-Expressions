package expressivo;

/**
 * An immutable type representing a multiplication expression.
 * This class implements the Expression interface.
 */
public class Multiplication implements Expression {
    private final Expression left;
    private final Expression right;

    // Abstraction Function:
    // Represents a multiplication expression made up of two non-null immutable subexpressions.
    //
    // Representation Invariant:
    // The 'left' and 'right' subexpressions are non-null and immutable.
    //
    // Safety From Exposure:
    // - All fields are private and final.
    // - 'left' and 'right' are expected to be immutable, ensuring the immutability of this instance.

    private void checkRep() {
        assert left != null;
        assert right != null;
    }

    /**
     * Constructs a Multiplication expression with two subexpressions.
     *
     * @param left  The left subexpression of the multiplication.
     * @param right The right subexpression of the multiplication.
     */
    public Multiplication(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        checkRep();
    }

    /**
     * Adds the provided expression to this multiplication expression.
     *
     * @param e The expression to add to this multiplication.
     * @return An expression representing the addition.
     */
    @Override
    public Expression addExpr(Expression e) {
        if (e.equals(new Value(0))) {
            return this;
        }
        if (e.equals(this)) {
            return this.multiplyExpr(new Value(2));
        }
        return new Addition(this, e);
    }

    /**
     * Multiplies the provided expression with this multiplication expression.
     *
     * @param e The expression to multiply with this multiplication.
     * @return An expression representing the multiplication.
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
     * Adds a variable to this multiplication expression.
     *
     * @param variable The variable to add to this multiplication.
     * @return An expression representing the addition of the variable.
     */
    @Override
    public Expression addVariable(String variable) {
        assert variable != null && variable != "";
        return new Addition(new Variable(variable), this);
    }

    /**
     * Multiplies a variable with this multiplication expression.
     *
     * @param variable The variable to multiply with this multiplication.
     * @return An expression representing the multiplication of the variable.
     */
    @Override
    public Expression multiplyVariable(String variable) {
        assert variable != null && variable != "";
        return new Multiplication(new Variable(variable), this);
    }

    /**
     * Adds a constant value to this multiplication expression.
     *
     * @param num The constant value to add to this multiplication.
     * @return An expression representing the addition of the constant value.
     */
    @Override
    public Expression addConstant(double num) {
        assert num >= 0 && Double.isFinite(num);
        Value valNum = new Value(num);
        if (valNum.equals(new Value(0))) {
            return this;
        }
        return new Addition(valNum, this);
    }

    /**
     * Appends a coefficient to this multiplication expression.
     *
     * @param num The coefficient to append to this multiplication.
     * @return An expression representing the appended coefficient.
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
        return new Multiplication(valNum, this);
    }

    /**
     * Returns the string representation of this multiplication expression.
     *
     * @return A string representing the multiplication expression.
     */
    @Override
    public String toString() {
        String leftString = this.left.toString();
        String rightString = this.right.toString();
        leftString = "(" + leftString + ")";
        rightString = "(" + rightString + ")";
        checkRep();
        return leftString + "*" + rightString;
    }

    /**
     * Checks if the provided object is equal to this multiplication expression.
     *
     * @param thatObject The object to compare for equality.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (!(thatObject instanceof Multiplication)) {
            return false;
        }
        Multiplication thatMult = (Multiplication) thatObject;
        checkRep();
        return this.toString().equals(thatMult.toString());
    }

    /**
     * Computes a hash code for this multiplication expression.
     *
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;
        result = prime * result + this.left.hashCode();
        result = prime * result + this.right.hashCode();
        checkRep();
        return result;
    }
}
