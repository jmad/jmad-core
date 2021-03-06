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

/**
 * The general implementation of an optics point.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class EditableOpticPointImpl extends OpticPointImpl implements EditableOpticPoint {

    public EditableOpticPointImpl(String name) {
        super(name);
    }

    @Override
    public void setBetx(Double betx) {
        setValue(MadxTwissVariable.BETX, betx);
    }

    @Override
    public void setBety(Double bety) {
        setValue(MadxTwissVariable.BETY, bety);
    }

    @Override
    public void setMux(Double mux) {
        setValue(MadxTwissVariable.MUX, mux);
    }

    @Override
    public void setMuy(Double muy) {
        setValue(MadxTwissVariable.MUY, muy);
    }

    @Override
    /* short name to use the same as madx */
    public void setDx(Double dx) { // NOPMD by kaifox on 6/25/10 5:51 PM
        setValue(MadxTwissVariable.DX, dx);
    }

    @Override
    /* short name to use the same as madx */
    public void setDy(Double dy) { // NOPMD by kaifox on 6/25/10 5:51 PM
        setValue(MadxTwissVariable.DY, dy);
    }

    @Override
    public void setAlfx(Double alfx) {
        setValue(MadxTwissVariable.ALFX, alfx);
    }

    @Override
    public void setAlfy(Double alfy) {
        setValue(MadxTwissVariable.ALFY, alfy);
    }

    @Override
    public void setDdx(Double ddx) {
        setValue(MadxTwissVariable.DDX, ddx);
    }

    @Override
    public void setDpx(Double dpx) {
        setValue(MadxTwissVariable.DPX, dpx);
    }

    @Override
    public void setDpy(Double dpy) {
        setValue(MadxTwissVariable.DPY, dpy);
    }

    @Override
    public void setDdy(Double ddy) {
        setValue(MadxTwissVariable.DDY, ddy);
    }

    @Override
    public void setDdpx(Double ddpx) {
        setValue(MadxTwissVariable.DDPX, ddpx);
    }

    @Override
    public void setDdpy(Double ddpy) {
        setValue(MadxTwissVariable.DDPY, ddpy);
    }

    @Override
    /* short name to use the same as madx */
    public void setX(Double x) { // NOPMD by kaifox on 6/25/10 5:51 PM
        setValue(MadxTwissVariable.X, x);
    }

    @Override
    /* short name to use the same as madx */
    public void setPx(Double px) { // NOPMD by kaifox on 6/25/10 5:51 PM
        setValue(MadxTwissVariable.PX, px);
    }

    @Override
    /* PMD: short name to use the same as madx */
    public void setY(Double y) { // NOPMD by kaifox on 6/25/10 5:51 PM
        setValue(MadxTwissVariable.Y, y);
    }

    @Override
    /* PMD: short name to use the same as madx */
    public void setPy(Double py) { // NOPMD by kaifox on 10/6/10 6:19 PM
        setValue(MadxTwissVariable.PY, py);
    }

}
