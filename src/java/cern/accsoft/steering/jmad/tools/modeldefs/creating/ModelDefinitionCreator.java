/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating;

import static java.util.stream.Collectors.toSet;

import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.accsoft.steering.jmad.modeldefs.ModelDefinitionFactory;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;

public final class ModelDefinitionCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelDefinitionCreator.class);

    private final LocalFactoriesFinder finder;

    public ModelDefinitionCreator(LocalFactoriesFinder finder) {
        this.finder = finder;
    }

    public static final ModelDefinitionCreator scan(String basePath) {
        return new ModelDefinitionCreator(LocalFactoriesFinder.scanning(Paths.get(basePath)));
    }

    public static final ModelDefinitionCreator scanDefault() {
        return new ModelDefinitionCreator(LocalFactoriesFinder.scanningDefault());
    }

    public ModelDefinitionCreator and() {
        return this;
    }

    public void writeToDefault() {
        write(ModelDefinitionWriter.create());
    }

    public void writeTo(String destinationPath) {
        write(ModelDefinitionWriter.toDestination(destinationPath));
    }

    private void write(ModelDefinitionWriter writer) {
        Set<ModelDefinitionFactory> factories = finder.findModelDefinitionFactories();
        if (factories.isEmpty()) {
            LOGGER.info("No factories found. Nothing to do.");
            return;
        }

        Set<JMadModelDefinition> modelDefs = factories.stream().map(ModelDefinitionFactory::create).collect(toSet());
        writer.write(modelDefs);
    }

    public static void main(String... args) {
        scanDefault().and().writeToDefault();
    }
}
