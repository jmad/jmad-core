package cern.accsoft.steering.jmad.domain.elem.impl;

import cern.accsoft.steering.jmad.domain.elem.MadxElementType;


/**
 * Reresents in jmad a madx element of type
 * <a href="http://mad.web.cern.ch/mad/madx.old/Introduction/solenoid.html">SOLENOID</a>.
 *
 * @author kaifox
 */
public class Solenoid extends AbstractElement {

    /**
     * The solenoid strength Ks (default: 0 rad/m). For positive KS and positive particle charge,
     * the solenoid field points in the direction of increasing s.
     */
    private static final String ATTR_KS = "ks";

    /**
     * The solenoid integrated strength Ks*L (default: 0 rad).
     * This additional attribute is needed only when using the thin solenoid, where L=0!
     */
    private static final String ATTR_KSI = "ksi";

    /**
     * default constructor to enforce that the element has a madx element type and a
     * name.
     *
     * @param madxElementType the type of the element, as it is represented in MadX
     * @param name            the name of the solenoid
     */
    public Solenoid(MadxElementType madxElementType, String name) {
        super(madxElementType, name);

        addAttribute(ATTR_KS);
        addAttribute(ATTR_KSI);
    }

    public void setKs(double ks) {
        setAttribute(ATTR_KS, ks);
    }

    public double getKs() {
        return getAttribute(ATTR_KS);
    }

    public void setKsi(double ksi) {
        setAttribute(ATTR_KSI, ksi);
    }

    public double getKsi() {
        return getAttribute(ATTR_KSI);
    }
}
