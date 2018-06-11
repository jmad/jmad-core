/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.kernel.cmd;

import java.util.List;

import cern.accsoft.steering.jmad.domain.twiss.TwissInitialConditions;
import cern.accsoft.steering.jmad.domain.var.enums.MadxTwissVariable;
import cern.accsoft.steering.jmad.kernel.cmd.param.GenericParameter;
import cern.accsoft.steering.jmad.kernel.cmd.param.Parameter;

/**
 * Contains utility methods to create parameters out of initial conditions
 * 
 * @author kfuchsbe
 */
public final class InitialContitionsParameters {

    private InitialContitionsParameters() {
        /* Only static methods */
    }

    /**
     * Adds the parameters representing the given initial conditions to the parameter list.
     * 
     * @param parameters the list to which to append the parameters
     * @param tw the twiss initial conditons which shall be added to the list of parameters
     */
    public static void addTwissParameters(List<Parameter> parameters, TwissInitialConditions tw) {
        parameters.add(new GenericParameter<Double>("deltap", tw.getDeltap()));

        /*
         * the initial conditions must not be set, if we want to calc the closed orbit solution
         */
        if (!tw.isClosedOrbit() && (tw.getSaveBetaName() == null)) {
            for (MadxTwissVariable var : tw.getMadxVariables()) {
                Double value = tw.getValue(var);
                if ((value != null) && (!MadxTwissVariable.DELTAP.equals(var))) {
                    parameters.add(new GenericParameter<Double>(var.getMadxName(), value));
                }
            }
        }

        if (tw.getSaveBetaName() != null) {
            parameters.add(new GenericParameter<String>("beta0", tw.getSaveBetaName(), true));
        }

        parameters.add(new GenericParameter<Boolean>("chrom", tw.isCalcChromaticFunctions()));
        parameters.add(new GenericParameter<Boolean>("centre", tw.isCalcAtCenter()));
    }
}
