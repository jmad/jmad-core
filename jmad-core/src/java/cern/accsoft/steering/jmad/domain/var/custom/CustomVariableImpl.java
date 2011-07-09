/**
 * 
 */
package cern.accsoft.steering.jmad.domain.var.custom;

import cern.accsoft.steering.jmad.util.MadxVarType;

/**
 * This class is the basic implementation of a {@link CustomVariable}
 * 
 * @author kfuchsbe
 */
public class CustomVariableImpl implements CustomVariable {

    /** The name of the variable */
    private final String name;

    /** The expression-string of the variable */
    private final String expression;

    /** The comment for the variable */
    private final String comment;

    /** True if defined with ":=", false if defined with "=" */
    private final boolean lateAssigned;

    /**
     * The constructor which enforces to give a name and the expression.
     * 
     * @param name the name of the variable
     * @param expression the expression defining the variable in madx.
     * @param comment a simple description of the variable
     * @param lateAssigned true, if assigned by ":=", false if assigned by ":".
     */
    public CustomVariableImpl(String name, String expression, String comment, boolean lateAssigned) {
        this.name = name;
        this.expression = expression;
        this.comment = comment;
        this.lateAssigned = lateAssigned;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    @Override
    public String getMadxName() {
        return this.name;
    }

    @Override
    public boolean isLateAssigned() {
        return this.lateAssigned;
    }

    @Override
    public boolean isApertureVariable() {
        /*
         * this is just a primitive implementation. Maybe this shall become somehow selectable!?
         */
        return (getMadxName().toUpperCase().contains("APER") || ((getComment() != null) && getComment().toUpperCase()
                .contains("APERTURE")));
    }

    @Override
    public String toString() {
        return getMadxName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CustomVariable) {
            return this.getMadxName().equals(((CustomVariable) other).getMadxName());
        } else {
            return super.equals(other);
        }
    }

    @Override
    public int hashCode() {
        return this.getMadxName().hashCode();
    }

    @Override
    public String getKey() {
        return getMadxName().trim().toLowerCase();
    }

    @Override
    public String getUnit() {
        return null;
    }

    @Override
    public MadxVarType getVarType() {
        return MadxVarType.DOUBLE;
    }

    @Override
    public String getName() {
        return getMadxName();
    }

    @Override
    public Class<?> getValueClass() {
        return getVarType().getValueClass();
    }

}