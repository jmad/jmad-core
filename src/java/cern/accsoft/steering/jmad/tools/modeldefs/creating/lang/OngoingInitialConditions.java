/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import cern.accsoft.steering.jmad.domain.twiss.TwissInitialConditionsImpl;

public class OngoingInitialConditions {

    private final TwissInitialConditionsImpl initialConditions;

    public OngoingInitialConditions(TwissInitialConditionsImpl initialConditions) {
        this.initialConditions = requireNonNull(initialConditions, "initialConditions must not be null");
    }

    public OngoingInitialConditions betx(Double betx) {
        initialConditions.setBetx(betx);
        return this;
    }

    public OngoingInitialConditions bety(Double bety) {
        initialConditions.setBety(bety);
        return this;
    }

    public OngoingInitialConditions mux(Double mux) {
        initialConditions.setMux(mux);
        return this;
    }

    public OngoingInitialConditions muy(Double muy) {
        initialConditions.setMuy(muy);
        return this;
    }

    public OngoingInitialConditions dx(Double dx) {
        initialConditions.setDx(dx);
        return this;
    }

    public OngoingInitialConditions dy(Double dy) {
        initialConditions.setDy(dy);
        return this;
    }

    public OngoingInitialConditions alfx(Double alfx) {
        initialConditions.setAlfx(alfx);
        return this;
    }

    public OngoingInitialConditions alfy(Double alfy) {
        initialConditions.setAlfy(alfy);
        return this;
    }

    public OngoingInitialConditions ddx(Double ddx) {
        initialConditions.setDdx(ddx);
        return this;
    }

    public OngoingInitialConditions dpx(Double dpx) {
        initialConditions.setDpx(dpx);
        return this;
    }

    public OngoingInitialConditions dpy(Double dpy) {
        initialConditions.setDpy(dpy);
        return this;
    }

    public OngoingInitialConditions ddy(Double ddy) {
        initialConditions.setDdy(ddy);
        return this;
    }

    public OngoingInitialConditions ddpx(Double ddpx) {
        initialConditions.setDdpx(ddpx);
        return this;
    }

    public OngoingInitialConditions ddpy(Double ddpy) {
        initialConditions.setDdpy(ddpy);
        return this;
    }

    public OngoingInitialConditions x(Double x) {
        initialConditions.setX(x);
        return this;
    }

    public OngoingInitialConditions px(Double px) {
        initialConditions.setPx(px);
        return this;
    }

    public OngoingInitialConditions y(Double y) {
        initialConditions.setY(y);
        return this;
    }

    public OngoingInitialConditions py(Double py) {
        initialConditions.setPy(py);
        return this;
    }

    public OngoingInitialConditions deltap(Double deltap) {
        initialConditions.setDeltap(deltap);
        return this;
    }

    public OngoingInitialConditions doNotCalcChromaticFunctions() {
        initialConditions.setCalcChromaticFunctions(false);
        return this;
    }

    public OngoingInitialConditions closedOrbit() {
        initialConditions.setClosedOrbit(true);
        return this;
    }

    public OngoingInitialConditions calcAtCenter() {
        initialConditions.setCalcAtCenter(true);
        return this;
    }

    public OngoingInitialConditions t(Double t) {
        initialConditions.setT(t);
        return this;
    }

    public OngoingInitialConditions pt(Double pt) {
        initialConditions.setPt(pt);
        return this;
    }

}
