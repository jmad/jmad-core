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

package cern.accsoft.steering.jmad.model;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cern.accsoft.steering.jmad.JMadTestCase;
import cern.accsoft.steering.jmad.domain.ex.JMadModelException;
import cern.accsoft.steering.jmad.domain.result.tfs.TfsResult;
import cern.accsoft.steering.jmad.domain.result.tfs.TfsResultRequest;
import cern.accsoft.steering.jmad.domain.result.tfs.TfsResultRequestImpl;
import cern.accsoft.steering.jmad.domain.result.track.DynapResult;
import cern.accsoft.steering.jmad.domain.result.track.DynapResultRequest;
import cern.accsoft.steering.jmad.domain.result.track.DynapResultRequestImpl;
import cern.accsoft.steering.jmad.domain.result.track.TrackResult;
import cern.accsoft.steering.jmad.domain.result.track.TrackResultRequest;
import cern.accsoft.steering.jmad.domain.result.track.TrackResultRequestImpl;
import cern.accsoft.steering.jmad.domain.track.RelativeParticleCoordinateImpl;
import cern.accsoft.steering.jmad.domain.track.RelativeParticleDistributionImpl;
import cern.accsoft.steering.jmad.domain.track.TrackInitialCondition;
import cern.accsoft.steering.jmad.domain.track.TrackInitialConditionImpl;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;

public class JMadThinLensModelTest extends JMadTestCase {

    private static JMadModelDefinition thinLensModelDefinition;
    private static JMadModel thinLensModel;

    @BeforeClass
    public static void classSetUp() {
        thinLensModelDefinition = JMadTestCase.findThinLensExampleModelDefinition();
        thinLensModel = getJMadService().createModel(thinLensModelDefinition);
    }

    @Before
    public void setUp() throws Exception {
        thinLensModel.init();
    }

    @After
    public void tearDown() throws Exception {
        thinLensModel.cleanup();
    }

    @Test
    public void testTwiss() throws JMadModelException {
        TfsResultRequest request = TfsResultRequestImpl.createSummaryOnlyRequest();
        TfsResult result = thinLensModel.twiss(request);
        assertNotNull(result);
    }

    @Test
    public void testTracking() throws JMadModelException {
        TrackInitialCondition init = new TrackInitialConditionImpl();

        RelativeParticleDistributionImpl dist = new RelativeParticleDistributionImpl();
        dist.add(new RelativeParticleCoordinateImpl(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        dist.add(new RelativeParticleCoordinateImpl(1E-6, 0.0, 0.0, 0.0, 0.0, 0.0));
        dist.add(new RelativeParticleCoordinateImpl(0.0, 0.0, 1E-6, 0.0, 0.0, 0.0));
        dist.add(new RelativeParticleCoordinateImpl(1E-6, 0.0, 1E-6, 0.0, 0.0, 0.0));
        TrackResultRequest request = new TrackResultRequestImpl(dist);
        request.setApertureLimited(false);
        request.setPrintFrequency(1);
        request.setTurns(10);

        TrackResult result = thinLensModel.track(request, init);
        assertNotNull(result);

    }

    @Test
    public void testDynapTune() throws JMadModelException {
        TrackInitialCondition init = new TrackInitialConditionImpl();

        RelativeParticleDistributionImpl dist = new RelativeParticleDistributionImpl();
        dist.add(new RelativeParticleCoordinateImpl(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        dist.add(new RelativeParticleCoordinateImpl(1E-6, 0.0, 0.0, 0.0, 0.0, 0.0));
        dist.add(new RelativeParticleCoordinateImpl(0.0, 0.0, 1E-6, 0.0, 0.0, 0.0));
        dist.add(new RelativeParticleCoordinateImpl(1E-6, 0.0, 1E-6, 0.0, 0.0, 0.0));
        DynapResultRequest request = new DynapResultRequestImpl(dist);
        request.setApertureLimited(false);
        request.setTurns(10);
        request.setFastTune(true);

        DynapResult result = thinLensModel.dynap(request, init);
        assertNotNull(result);

    }

    @Test
    public void testDynap() throws JMadModelException {
        TrackInitialCondition init = new TrackInitialConditionImpl();

        RelativeParticleDistributionImpl dist = new RelativeParticleDistributionImpl();
        dist.add(new RelativeParticleCoordinateImpl(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        dist.add(new RelativeParticleCoordinateImpl(1E-6, 0.0, 0.0, 0.0, 0.0, 0.0));
        dist.add(new RelativeParticleCoordinateImpl(0.0, 0.0, 1E-6, 0.0, 0.0, 0.0));
        dist.add(new RelativeParticleCoordinateImpl(1E-6, 0.0, 1E-6, 0.0, 0.0, 0.0));
        DynapResultRequest request = new DynapResultRequestImpl(dist);
        request.setApertureLimited(false);
        request.setTurns(10);
        request.setFastTune(false);

        DynapResult result = (DynapResult) thinLensModel.dynap(request, init);
        assertNotNull(result);

    }

}
