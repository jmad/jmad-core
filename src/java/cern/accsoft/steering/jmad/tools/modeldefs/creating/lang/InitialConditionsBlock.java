/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import cern.accsoft.steering.jmad.domain.twiss.TwissInitialConditionsImpl;

public class InitialConditionsBlock {

    private final TwissInitialConditionsImpl initialConditions;

    public InitialConditionsBlock(TwissInitialConditionsImpl initialConditions) {
        this.initialConditions = requireNonNull(initialConditions, "initialConditions must not be null");
    }

    public InitialConditionsBlock betx(Double betx) {
        initialConditions.setBetx(betx);
        return this;
    }

    public InitialConditionsBlock bety(Double bety) {
        initialConditions.setBety(bety);
        return this;
    }

    public InitialConditionsBlock mux(Double mux) {
        initialConditions.setMux(mux);
        return this;
    }

    public InitialConditionsBlock muy(Double muy) {
        initialConditions.setMuy(muy);
        return this;
    }

    public InitialConditionsBlock dx(Double dx) {
        initialConditions.setDx(dx);
        return this;
    }

    public InitialConditionsBlock dy(Double dy) {
        initialConditions.setDy(dy);
        return this;
    }

    public InitialConditionsBlock alfx(Double alfx) {
        initialConditions.setAlfx(alfx);
        return this;
    }

    public InitialConditionsBlock alfy(Double alfy) {
        initialConditions.setAlfy(alfy);
        return this;
    }

    public InitialConditionsBlock ddx(Double ddx) {
        initialConditions.setDdx(ddx);
        return this;
    }

    public InitialConditionsBlock dpx(Double dpx) {
        initialConditions.setDpx(dpx);
        return this;
    }

    public InitialConditionsBlock dpy(Double dpy) {
        initialConditions.setDpy(dpy);
        return this;
    }

    public InitialConditionsBlock ddy(Double ddy) {
        initialConditions.setDdy(ddy);
        return this;
    }

    public InitialConditionsBlock ddpx(Double ddpx) {
        initialConditions.setDdpx(ddpx);
        return this;
    }

    public InitialConditionsBlock ddpy(Double ddpy) {
        initialConditions.setDdpy(ddpy);
        return this;
    }

    public InitialConditionsBlock x(Double x) {
        initialConditions.setX(x);
        return this;
    }

    public InitialConditionsBlock px(Double px) {
        initialConditions.setPx(px);
        return this;
    }

    public InitialConditionsBlock y(Double y) {
        initialConditions.setY(y);
        return this;
    }

    public InitialConditionsBlock py(Double py) {
        initialConditions.setPy(py);
        return this;
    }

    public InitialConditionsBlock deltap(Double deltap) {
        initialConditions.setDeltap(deltap);
        return this;
    }

    public InitialConditionsBlock doNotCalcChromaticFunctions() {
        initialConditions.setCalcChromaticFunctions(false);
        return this;
    }

    public InitialConditionsBlock closedOrbit() {
        initialConditions.setClosedOrbit(true);
        return this;
    }

    public InitialConditionsBlock calcAtCenter() {
        initialConditions.setCalcAtCenter(true);
        return this;
    }

    public InitialConditionsBlock t(Double t) {
        initialConditions.setT(t);
        return this;
    }

    public InitialConditionsBlock pt(Double pt) {
        initialConditions.setPt(pt);
        return this;
    }

}
