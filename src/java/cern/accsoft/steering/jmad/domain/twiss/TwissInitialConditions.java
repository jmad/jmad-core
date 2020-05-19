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

package cern.accsoft.steering.jmad.domain.twiss;

import java.util.List;

import cern.accsoft.steering.jmad.domain.optics.EditableOpticPoint;
import cern.accsoft.steering.jmad.domain.var.enums.MadxTwissVariable;

public interface TwissInitialConditions extends EditableOpticPoint {

    Integer getPtcPhaseSpaceDimension();

    void setPtcPhaseSpaceDimension(Integer dim);

    Integer getPtcMapOrder();

    void setPtcMapOrder(Integer order);

    Double getPtcBetz();

    void setPtcBetz(Double betz);

    Double getDeltap();

    void setDeltap(Double deltap);

    /**
     * @param calcChromaticFunctions the calcChromaticFunctions to set
     */
    void setCalcChromaticFunctions(boolean calcChromaticFunctions);

    /**
     * @return the calcChromaticFunctions
     */
    boolean isCalcChromaticFunctions();

    /**
     * @param closedOrbit the closedOrbit to set
     */
    void setClosedOrbit(boolean closedOrbit);

    /**
     * @return the closedOrbit
     */
    boolean isClosedOrbit();

    /**
     * @param calcAtCentre the calcAtCentre to set
     */
    void setCalcAtCenter(boolean calcAtCentre);

    /**
     * @return the calcAtCenter
     */
    boolean isCalcAtCenter();

    Double getT();

    /* short name to use the same as madx */
    void setT(Double t);

    Double getPt();

    /* short name to use the same as madx */
    void setPt(Double pt);

    List<MadxTwissVariable> getMadxVariables();

    Double getValue(MadxTwissVariable var);

    void setSaveBetaName(String saveBetaName);

    String getSaveBetaName();

    void addListener(TwissListener listener);

    void removeListener(TwissListener listener);
}
