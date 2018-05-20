/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.cleaning.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionImporter;
import cern.accsoft.steering.jmad.modeldefs.io.ModelFileFinderManager;
import cern.accsoft.steering.jmad.tools.modeldefs.cleaning.UnusedLocalFileDetector;

@Configuration
@ImportResource(locations = "classpath:app-ctx-jmad-service.xml")
public class ModelFileCleaningConfiguration {

    @Bean
    public UnusedLocalFileDetector unusedLocalFileDetector(JMadModelDefinitionImporter modelDefinitionImporter,
            ModelFileFinderManager fileFinderManager) {
        return new UnusedLocalFileDetector(modelDefinitionImporter, fileFinderManager);
    }

}
