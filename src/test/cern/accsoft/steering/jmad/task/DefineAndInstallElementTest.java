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

package cern.accsoft.steering.jmad.task;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cern.accsoft.steering.jmad.domain.elem.Element;
import cern.accsoft.steering.jmad.domain.elem.MadxElementType;
import cern.accsoft.steering.jmad.domain.elem.impl.BeamBeam;
import cern.accsoft.steering.jmad.domain.elem.impl.Quadrupole;
import cern.accsoft.steering.jmad.kernel.task.DefineAndInstallElements;

public class DefineAndInstallElementTest {

    @Test
    public void installElementTest() {
        Quadrupole quad = new Quadrupole(MadxElementType.QUADRUPOLE, "testQuad");
        quad.setPosition(1000);
        quad.setAttribute("k1", 1E-4);
        quad.setAttributesInitialized(true);

        BeamBeam beamBeam = new BeamBeam(MadxElementType.BEAMBEAM, "testBeamBeam");
        beamBeam.setAttribute("charge", 0.0);
        beamBeam.setAttributesInitialized(true);

        List<Element> input = new ArrayList<Element>();
        input.add(quad);
        input.add(beamBeam);

        String sequ = "lchb1";

        DefineAndInstallElements testTask = new DefineAndInstallElements(sequ, input);
        assertEquals(
                testTask.compose(),
                "\n"
                        + "// ***** BEGIN autogenerated task: cern.accsoft.steering.jmad.kernel.task.DefineAndInstallElements *****\n"
                        + "testQuad: quadrupole, k1=1.0E-4;\n"
                        + "testBeamBeam: beambeam, charge=0.0;\n"
                        + "seqedit, sequence=lchb1;\n"
                        + "install, element=testQuad, class=quadrupole, at=1000.0;\n"
                        + "install, element=testBeamBeam, class=beambeam, at=0.0;\n"
                        + "flatten;\n"
                        + "endedit;\n"
                        + "// ***** END autogenerated task: cern.accsoft.steering.jmad.kernel.task.DefineAndInstallElements *****");
    }
}
