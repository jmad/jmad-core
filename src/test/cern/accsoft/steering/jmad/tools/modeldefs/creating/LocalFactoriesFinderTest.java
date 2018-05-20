/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating;

import java.util.Set;

import org.junit.Test;

import cern.accsoft.steering.jmad.modeldefs.ModelDefinitionFactory;

public class LocalFactoriesFinderTest {

    @Test
    public void findFactories() {
        Set<ModelDefinitionFactory> factories = LocalFactoriesFinder.scanningDefault().findModelDefinitionFactories();
        System.out.println(factories);
    }

}
