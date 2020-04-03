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

/*
 * $Id: EalignCommand.java,v 1.1 2009-01-15 11:46:26 kfuchsbe Exp $
 * 
 * $Date: 2009-01-15 11:46:26 $ $Revision: 1.1 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.jmad.kernel.cmd;

import cern.accsoft.steering.jmad.kernel.cmd.param.GenericParameter;
import cern.accsoft.steering.jmad.kernel.cmd.param.Parameter;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * represents the command for adding magnetic field errors to one or more elements. The elements have to be selected
 * before by the select command.
 *
 * EFCOMP, ORDER=integer, RADIUS=real,
 *   DKN={dkn(0),  dkn(1),  dkn(2),...},
 *   DKS={dks(0),  dks(1),  dks(2),...},
 *   DKNR={dknr(0), dknr(1), dknr(2),...},
 *   DKSR={dksr(0), dksr(1), dksr(2),...};
 *
 * @author michi
 */
public class EfcompCommand extends AbstractCommand {

    /** the name of the command */
    private static final String CMD_NAME = "efcomp";

    private Double radius = null;
    private Integer order = null;
    private List<Double> absoluteErrors = null;
    private List<Double> absoluteSkewErrors = null;
    private List<Double> relativeErrors = null;
    private List<Double> relativeSkewErrors = null;

    /**
     * The default constructor.
     */
    public EfcompCommand() {
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<Double> getAbsoluteErrors() {
        return absoluteErrors;
    }

    public void setAbsoluteErrors(List<Double> absoluteErrors) {
        this.absoluteErrors = Optional.ofNullable(absoluteErrors).map(ImmutableList::copyOf).orElse(null);
    }

    public List<Double> getAbsoluteSkewErrors() {
        return absoluteSkewErrors;
    }

    public void setAbsoluteSkewErrors(List<Double> absoluteSkewErrors) {
        this.absoluteSkewErrors = Optional.ofNullable(absoluteSkewErrors).map(ImmutableList::copyOf).orElse(null);
    }

    public List<Double> getRelativeErrors() {
        return relativeErrors;
    }

    public void setRelativeErrors(List<Double> relativeErrors) {
        this.relativeErrors = Optional.ofNullable(relativeErrors).map(ImmutableList::copyOf).orElse(null);
    }

    public List<Double> getRelativeSkewErrors() {
        return relativeSkewErrors;
    }

    public void setRelativeSkewErrors(List<Double> relativeSkewErrors) {
        this.relativeSkewErrors = Optional.ofNullable(relativeSkewErrors).map(ImmutableList::copyOf).orElse(null);
    }

    @Override
    public String getName() {
        return CMD_NAME;
    }

    @Override
    public List<Parameter> getParameters() {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();

        /*
         * define the mapping of the member-vars to the parameter names
         */

        /*
         * EFCOMP, ORDER=integer, RADIUS=real,
         *   DKN={dkn(0),  dkn(1),  dkn(2),...},
         *   DKS={dks(0),  dks(1),  dks(2),...},
         *   DKNR={dknr(0), dknr(1), dknr(2),...},
         *   DKSR={dksr(0), dksr(1), dksr(2),...};
         */
        parameters.add(new GenericParameter<>("order", order));
        parameters.add(new GenericParameter<>("radius", radius));
        parameters.add(new GenericParameter<>("dkn", absoluteErrors));
        parameters.add(new GenericParameter<>("dks", absoluteSkewErrors));
        parameters.add(new GenericParameter<>("dknr", relativeErrors));
        parameters.add(new GenericParameter<>("dksr", relativeSkewErrors));
        return parameters;
    }

}
