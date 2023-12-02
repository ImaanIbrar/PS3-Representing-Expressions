package expressivo;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * An immutable type representing a non-negative number in an expression.
 */
public class Value implements Expression {
    // Private field to store the numeric value
    private final double num;

    // Abstraction Function:
    //   Represents a nonnegative decimal number as an expression.
    //   
    // Representation Invariant:
    //   0 <= num <= Double.MAX_VALUE
    //
    // Safety From Exposure
    //   - num is a private and immutable reference
    //   - num references a primitive type, which is immutable

    // Method to check the representation invariant
    private void checkRep() {
        assert Double.isFinite(num);
        assert 0 <= num && num <= Double.MAX_VALUE;
    }

    /**
     * Constructor to initialize the Value object with a given numeric value.
     * 
     * @param num The non-negative numeric value for the Value object.
     */
    public Value(double num) {
        this.num = num;
        // Check and enforce the representation invariant
        checkRep();
    }

    /**
     * Adds an expression to the current value.
     * If the expression to be added is the same as the current value,
     * the numeric value is doubled.
     * 
     * @param e The expression to be added.
     * @return Expression resulting from the addition.
     */
    @Override
    public Expression addExpr(Expression e) {
        if (e.equals(this)) {
            double newNum = this.num * 2;
            checkRep();
            
            if (Double.isInfinite(newNum)) {
                return new Value(Double.MAX_VALUE);
            }
            return new Value(newNum);
        }
        Value zero = new Value(0.0);
        if (this.equals(zero)) {
            return e;
        }
        if (e.equals(zero)) {
            return this;
        }
        checkRep();
        return e.addConstant(num);
    }

    /**
     * Multiplies the current value by an expression.
     * If the current value is one, returns the other expression.
     * 
     * @param e The expression to be multiplied.
     * @return Expression resulting from the multiplication.
     */
    @Override
    public Expression multiplyExpr(Expression e) {
        Value zero = new Value(0);
        Value one = new Value(1);
        if (this.equals(zero) || e.equals(zero)) {
            return zero;
        }
        if (this.equals(one)) {
            checkRep();
            return e;
        }
        if (e.equals(one)) {
            return this;
        }
        checkRep();
        return e.appendCoefficient(num);
    }

    /**
     * Adds a variable to the current value.
     * If the current value is zero, returns a Variable object.
     * 
     * @param variable The variable to be added.
     * @return Expression resulting from adding a variable.
     */
    @Override
    public Expression addVariable(String variable) {
        assert variable != null && variable != "";
        if (this.equals(new Value(0))) {
            return new Variable(variable);
        }
        checkRep();
        return new Addition(new Variable(variable), this);
    }

    /**
     * Multiplies the current value by a variable.
     * If the current value is zero, returns zero.
     * 
     * @param variable The variable to be multiplied.
     * @return Expression resulting from multiplying by a variable.
     */
    @Override
    public Expression multiplyVariable(String variable) {
        assert variable != null && variable != "";
        Value zero = new Value(0);
        if (this.equals(zero)) {
            return zero;
        }
        if (this.equals(new Value(1))) {
            return new Variable(variable);
        }

        checkRep();
        return new Multiplication(new Variable(variable), this);
    }

    /**
     * Adds a constant to the current value.
     * Truncates the decimal part of the constant to 5 decimal places
     * before adding to the current value.
     * 
     * @param num The constant to be added.
     * @return Expression resulting from adding a constant.
     */
    @Override
    public Expression addConstant(double num) {
        double numTruncate = Math.floor(num * 100000.0) / 100000.0;
        double newNum = numTruncate + this.num;
        checkRep();
        
        if (Double.isInfinite(newNum)) {
            return new Value(Double.MAX_VALUE);
        }

        return new Value(newNum);
    }

    /**
     * Appends a coefficient to the current value.
     * Truncates the decimal part of the coefficient to 5 decimal places
     * before multiplying with the current value.
     * 
     * @param num The coefficient to be appended.
     * @return Expression resulting from appending a coefficient.
     */
    @Override
    public Expression appendCoefficient(double num) {    
        double numTruncate = Math.floor(num * 100000.0) / 100000.0;    
        double newNum = numTruncate * this.num;
        checkRep();
        
        if (Double.isInfinite(newNum)) {
            return new Value(Double.MAX_VALUE);
        }

        return new Value(newNum);
    }

    /**
     * Converts the Value object to a string representation.
     * Uses a decimal formatter to format the numeric value.
     * 
     * @return String representation of the Value.
     */
    @Override
    public String toString() {
        DecimalFormat myFormatter = new DecimalFormat("###.#####");
        myFormatter.setRoundingMode(RoundingMode.DOWN);
        String output = myFormatter.format(this.num);

        checkRep();
        return output;
    }

    /**
     * Checks if two Value objects are equal, correct to 5 decimal places.
     * 
     * @param thatObject Object to compare equality with.
     * @return true if two Value objects are equal.
     */
    @Override
    public boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (!(thatObject instanceof Value)) {
            return false;
        }
        Value thatValue = (Value) thatObject;
        double thatNum = Double.parseDouble(thatValue.toString());
        final double EPSILON = 0.00001;

        checkRep();
        return Math.abs(this.num - thatNum) < EPSILON;
    }

    /**
     * Generates a hash code for the Value object.
     * 
     * @return Hash code for the Value object.
     */
    @Override
    public int hashCode() {
        double preciseNum = Double.parseDouble(this.toString());
        final int prime = 37;
        int result = 1;
        long numLong = Double.doubleToLongBits(preciseNum);
        int numHash = (int) (numLong ^ (numLong >>> 32));
        
        result = prime*result + numHash;
        
        checkRep();
        return result;
    }
}
