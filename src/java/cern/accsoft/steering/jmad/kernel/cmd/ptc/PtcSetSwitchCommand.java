// @formatter:off
 /*******************************************************************************
 *
 * This file is part of JMad.
 * 
 * Copyright (c) 2008-2011, CERN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ******************************************************************************/
// @formatter:on

/**
 * 
 */
package cern.accsoft.steering.jmad.kernel.cmd.ptc;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.jmad.kernel.cmd.AbstractCommand;
import cern.accsoft.steering.jmad.kernel.cmd.param.GenericParameter;
import cern.accsoft.steering.jmad.kernel.cmd.param.Parameter;

/**
 * This class represents the madx-command to initialize the ptc-universe
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class PtcSetSwitchCommand extends AbstractCommand {

    /** the name of the command */
    private static final String CMD_NAME = "ptc_setswitch";

    private Boolean time = null;
    private Boolean fringe = null;
    private Boolean nocavity = null;
    private Boolean exactmis = null;
    private Boolean totalpath = null;
    private Boolean radiation = null;
    private Boolean envelope = null;
    private Boolean stochastic = null;
    private Boolean modulation = null;

    @Override
    public String getName() {
        return CMD_NAME;
    }

    @Override
    public List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<>();
        if (time != null) {
            parameters.add(new GenericParameter<>("time", time.toString()));
        }
        parameters.add(new GenericParameter<>("fringe", fringe));
        parameters.add(new GenericParameter<>("nocavity", nocavity));
        parameters.add(new GenericParameter<>("exactmis", exactmis));
        parameters.add(new GenericParameter<>("totalpath", totalpath));
        parameters.add(new GenericParameter<>("radiation", radiation));
        parameters.add(new GenericParameter<>("envelope", envelope));
        parameters.add(new GenericParameter<>("stochastic", stochastic));
        parameters.add(new GenericParameter<>("modulation", modulation));

        return parameters;
    }

    public Boolean getTime() {
        return time;
    }

    public void setTime(Boolean time) {
        this.time = time;
    }

    public Boolean getFringe() {
        return fringe;
    }

    public void setFringe(Boolean fringe) {
        this.fringe = fringe;
    }

    public Boolean getNocavity() {
        return nocavity;
    }

    public void setNocavity(Boolean nocavity) {
        this.nocavity = nocavity;
    }

    public Boolean getExactmis() {
        return exactmis;
    }

    public void setExactmis(Boolean exactmis) {
        this.exactmis = exactmis;
    }

    public Boolean getTotalpath() {
        return totalpath;
    }

    public void setTotalpath(Boolean totalpath) {
        this.totalpath = totalpath;
    }

    public Boolean getRadiation() {
        return radiation;
    }

    public void setRadiation(Boolean radiation) {
        this.radiation = radiation;
    }

    public Boolean getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Boolean envelope) {
        this.envelope = envelope;
    }

    public Boolean getStochastic() {
        return stochastic;
    }

    public void setStochastic(Boolean stochastic) {
        this.stochastic = stochastic;
    }

    public Boolean getModulation() {
        return modulation;
    }

    public void setModulation(Boolean modulation) {
        this.modulation = modulation;
    }
}
