/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static cern.accsoft.steering.jmad.tools.modeldefs.creating.ModelDefinitionCreator.scanDefault;

import org.junit.Test;

public class JMadModelDefinitionDslSupportTest {

    @Test
    public void writeModelDefinition() {
        scanDefault().and().writeTo("src/test/cern/accsoft/steering/jmad/modeldefs/defs");
    }

}
