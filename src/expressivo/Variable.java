package expressivo;

/**
 * An immutable type representing a named variable in an expression.
 * Variable names are case-sensitive.
 */
public class Variable implements Expression {
    // Private field to store the variable name
    private final String id;
    
    // Abstraction Function
    //   Represents a named variable as an expression.
    //
    // Representation Invariant
    //   - id.length > 0
    //   - id.charAt(n) must be a letter, a-zA-Z 
    //     for all 0 < n < id.length
    //
    // Safety From Exposure
    //   - id is private and final
    //   - id is a string which is immutable
    //   - Variable shares its rep with other implementations
    //     but they do not mutate it
    
    // Method to check the representation invariant
    private void checkRep() {
        assert id != null && id != "";
        assert id.length() > 0;
        assert id.matches("[a-zA-Z]+");
    }

    /**
     * Constructor to initialize the Variable object with a given name.
     * 
     * @param id The name of the variable.
     */
    public Variable(String id) {
        this.id = id;
        checkRep();
    }

    /**
     * Adds an expression to the current variable.
     * If the expression to be added is zero, returns the current variable.
     * 
     * @param e The expression to be added.
     * @return Expression resulting from the addition.
     */
    @Override
    public Expression addExpr(Expression e) {
        if (e.equals(new Value(0))) {
            return this;
        }
        checkRep();
        return e.addVariable(id);
    }

    /**
     * Multiplies the current variable by an expression.
     * If the expression to be multiplied is zero, returns zero.
     * 
     * @param e The expression to be multiplied.
     * @return Expression resulting from the multiplication.
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
        checkRep();
        return e.multiplyVariable(this.id);
    }

    // TODO: have it simplify such that x + x + x = 3*x

    /**
     * Adds a variable to the current variable.
     * 
     * @param variable The variable to be added.
     * @return Expression resulting from adding a variable.
     */
    @Override
    public Expression addVariable(String variable) {
        assert variable != null && variable != "";
        
        return new Addition(new Variable(variable), this);
    }

    /**
     * Multiplies the current variable by another variable.
     * 
     * @param variable The variable to be multiplied.
     * @return Expression resulting from multiplying by a variable.
     */
    @Override
    public Expression multiplyVariable(String variable) {
        assert variable != null && variable != "";
        
        return new Multiplication(new Variable(variable), this);
    }

    /**
     * Adds a constant to the current variable.
     * If the constant is zero, returns the current variable.
     * 
     * @param num The constant to be added.
     * @return Expression resulting from adding a constant.
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
     * Appends a coefficient to the current variable.
     * If the coefficient is zero, returns zero.
     * 
     * @param num The coefficient to be appended.
     * @return Expression resulting from appending a coefficient.
     */
    @Override
    public Expression appendCoefficient(double num) {    
        assert num >= 0 && Double.isFinite(num);
        
        Value valNum = new Value(num);
        Value zero = new Value(0);
        if (valNum.equals(zero)) {
            return new Value(0);
        }
        if (valNum.equals(new Value(1))) {
            return this;
        }
        return new Multiplication(new Value(num), this);
    }

    /**
     * Converts the Variable object to a string representation.
     * 
     * @return String representation of the Variable.
     */
    @Override
    public String toString() {
        return this.id;
    }

    /**
     * Checks if two Variable objects are equal.
     * 
     * @param thatObject Object to compare equality with.
     * @return true if two Variable objects are equal.
     */
    @Override
    public boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (!(thatObject instanceof Variable)) {
            return false;
        }
        Variable thatVar = (Variable) thatObject;
        String thatId = thatVar.toString();

        checkRep();
        return this.id.equals(thatId);
    }

    /**
     * Generates a hash code for the Variable object.
     * 
     * @return Hash code for the Variable object.
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
