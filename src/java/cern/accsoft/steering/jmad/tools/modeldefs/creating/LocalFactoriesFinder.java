/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.accsoft.steering.jmad.modeldefs.ModelDefinitionFactory;
import cern.accsoft.steering.jmad.util.ClassUtil;
import cern.accsoft.steering.jmad.util.FileUtil;

public class LocalFactoriesFinder {

    private static final String DEFAULT_SCAN_PATH = "src/test";
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFactoriesFinder.class);

    private final Path rootPath;

    private LocalFactoriesFinder(Path scanPath) {
        this.rootPath = requireNonNull(scanPath, "scanPath must not be null;");
    }

    public static LocalFactoriesFinder scanning(Path scanPath) {
        return new LocalFactoriesFinder(scanPath);
    }

    public static LocalFactoriesFinder scanningDefault() {
        return scanning(Paths.get(DEFAULT_SCAN_PATH));
    }

    public Set<ModelDefinitionFactory> findModelDefinitionFactories() {
        Set<File> javaFiles = FileUtil.searchInFor(rootPath, p -> p.getFileName().toString().endsWith(".java"));
        Set<String> classNames = ClassUtil.classNamesFromJavaFiles(javaFiles, rootPath);
        Set<Class<?>> classes = ClassUtil.loadIfPossible(classNames);

        // @formatter:off
        @SuppressWarnings("unchecked")
        Set<Class<ModelDefinitionFactory>> factoryClasses = classes.stream()
                .filter(ModelDefinitionFactory.class::isAssignableFrom)
                .map(c -> (Class<ModelDefinitionFactory>) c)
                .collect(toSet());
        // @formatter:on

        LOGGER.debug("Trying to instantiate factory classes: {}.", factoryClasses);
        return ClassUtil.instantiateIfPossible(factoryClasses);
    }

}
