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

package cern.accsoft.steering.jmad.domain.optics;

import cern.accsoft.steering.jmad.domain.var.enums.MadxTwissVariable;

public interface EditableOpticPoint extends OpticPoint {

    void setValue(MadxTwissVariable variable, Double value);

    void setBetx(Double betx);

    void setBety(Double bety);

    void setMux(Double mux);

    void setMuy(Double muy);

    /* short name to use the same as madx */
    void setDx(Double dx);

    /* short name to use the same as madx */
    void setDy(Double dy);

    void setAlfx(Double alfx);

    void setAlfy(Double alfy);

    void setDdx(Double ddx);

    void setDpx(Double dpx);

    void setDpy(Double dpy);

    void setDdy(Double ddy);

    void setDdpx(Double ddpx);

    void setDdpy(Double ddpy);

    /* short name to use the same as madx */
    void setX(Double x);

    /* short name to use the same as madx */
    void setPx(Double px);

    /* short name to use the same as madx */
    void setY(Double y);

    /* short name to use the same as madx */
    void setPy(Double py);
}
