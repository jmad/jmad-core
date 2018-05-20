/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.cleaning;

import static cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionUtil.getRequiredModelFiles;
import static cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionUtil.nonIgnoredFilesBelow;
import static com.google.common.collect.Sets.difference;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets.SetView;

import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionImporter;
import cern.accsoft.steering.jmad.modeldefs.io.ModelFileFinder;
import cern.accsoft.steering.jmad.modeldefs.io.ModelFileFinderManager;
import cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionUtil;

public class UnusedLocalFileDetector {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnusedLocalFileDetector.class);

    private final JMadModelDefinitionImporter modelDefinitionImporter;
    private final ModelFileFinderManager fileFinderManager;

    public UnusedLocalFileDetector(JMadModelDefinitionImporter modelDefinitionImporter,
            ModelFileFinderManager fileFinderManager) {
        this.modelDefinitionImporter = requireNonNull(modelDefinitionImporter,
                "modelDefinitionImporter must not be null");
        this.fileFinderManager = requireNonNull(fileFinderManager, "fileFinderManager must not be null");
    }

    public Set<File> detectUnusedFiles(Path rootPath) {
        Set<File> modelDefinitionFiles = ModelDefinitionUtil.modelDefinitionFilesBelow(rootPath);
        LOGGER.info("Found model definition files below {} :\n{}", rootPath, modelDefinitionFiles);
        Set<JMadModelDefinition> modelDefs = modelDefinitionFiles.stream()
                .flatMap(f -> modelDefinitionImporter.importModelDefinitions(f).stream()).collect(toSet());

        Set<File> requiredLocalFiles = new HashSet<>();
        requiredLocalFiles
                .addAll(modelDefinitionFiles.stream().map(UnusedLocalFileDetector::canonicalFile).collect(toSet()));

        for (JMadModelDefinition md : modelDefs) {
            ModelFileFinder finder = fileFinderManager.getModelFileFinder(md);
            Set<Optional<File>> localFiles = getRequiredModelFiles(md).stream().map(finder::getLocalSourceFile)
                    .collect(toSet());

            if (!localFiles.stream().allMatch(Optional::isPresent)) {
                throw new IllegalStateException(
                        "Not all Files of model definition '" + md + "' are local files! Cannot proceed.");
            }

            Set<File> absoluteFiles = localFiles.stream().map(Optional::get).map(UnusedLocalFileDetector::canonicalFile)
                    .collect(toSet());
            LOGGER.info("Model definition {} uses the following files: {}", md, absoluteFiles);

            requiredLocalFiles.addAll(absoluteFiles);
        }

        Set<File> allFiles = nonIgnoredFilesBelow(rootPath).stream().map(UnusedLocalFileDetector::canonicalFile)
                .collect(toSet());

        SetView<File> difference = difference(allFiles, requiredLocalFiles);
        return ImmutableSet.copyOf(difference);
    }

    private static File canonicalFile(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            throw new IllegalArgumentException("Canonical file of file '" + file + "' cannot be determined.");
        }
    }

}
