/**
 * Copyright (c) 2017 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.modeldefs;

import static cern.accsoft.steering.jmad.domain.beam.Beam.Direction.PLUS;
import static cern.accsoft.steering.jmad.domain.beam.Beam.Particle.PROTON;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import cern.accsoft.steering.jmad.domain.beam.Beam;
import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.service.JMadService;
import cern.accsoft.steering.jmad.service.JMadServiceFactory;

/**
 * This test tries to cover against regression bugs while loading the model. This was triggered by a bug in the
 * converter which was used in XStream to convert the beam object and did not load correctly the enums included there
 * and appeared when switching from xstream version 1.3 to 1.4.7.
 * 
 * @author kfuchsbe
 */
public class LoadModelDefinitionRegressionTest {

    private static final String LHC_2016_MODEL_NAME = "example";
    private static final String LHC_B2_SEQUENCE_NAME = "ti2";

    private JMadService jmadService;

    @Before
    public void setUp() {
        jmadService = JMadServiceFactory.createJMadService();
    }

    @Test
    public void particleIsProton() {
        assertThat(lhcB2Beam().getParticle()).isEqualTo(PROTON);
    }

    @Test
    public void directionIsMinus() {
        assertThat(lhcB2Beam().getDirection()).isEqualTo(PLUS);
    }

    @Test
    public void energyIs450GeV() {
        assertThat(lhcB2Beam().getEnergy()).isEqualTo(450.0);
    }

    @Test
    public void initFilesHaveNonNullLocation() {
        List<ModelFile> initFiles = lhcModelDefinition().getInitFiles();
        for (ModelFile file : initFiles) {
            Assertions.assertThat(file.getLocation()).isNotNull();
        }
    }

    private Beam lhcB2Beam() {
        return lhcModelDefinition().getSequenceDefinition(LHC_B2_SEQUENCE_NAME).getBeam();
    }

    private JMadModelDefinition lhcModelDefinition() {
        return jmadService.getModelDefinitionManager().getModelDefinition(LHC_2016_MODEL_NAME);
    }

}
