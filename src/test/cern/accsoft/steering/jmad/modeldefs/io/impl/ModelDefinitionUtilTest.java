/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.modeldefs.io.impl;

import static cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionUtil.modelDefinitionFilesBelow;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Paths;

import org.junit.Test;

public class ModelDefinitionUtilTest {

    @Test
    public void findModelExampleDefinitionsInTestPath() {
        assertThat(modelDefinitionFilesBelow(Paths.get("./src/java"))).hasSize(3);
    }

    @Test
    public void findNoModelExampleDefinitionsInJavaPath() {
        assertThat(modelDefinitionFilesBelow(Paths.get("./src/test"))).isEmpty();
    }

}
