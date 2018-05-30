/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.util;

import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public final class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
    private static final String JAVA_SUFFIX = ".java";

    private ClassUtil() {
        /* Only static methods */
    }

    public static final String classNameFromJavaFile(File file, Path pathPrefix) {
        String prefixString = pathPrefix.toString();
        String filePath = file.getPath();
        if (!filePath.startsWith(prefixString)) {
            throw new IllegalArgumentException(
                    "The given file '" + file + "' seems not to be below the given path prefix '" + pathPrefix + "'.");
        }
        if (!file.getName().endsWith(JAVA_SUFFIX)) {
            throw new IllegalArgumentException("File name does not end in '.java'.");
        }

        String relative = filePath.substring(prefixString.length() + 1);
        String trimmed = relative.substring(0, relative.length() - JAVA_SUFFIX.length());
        return trimmed.replace(File.separatorChar, '.');
    }

    public static final Set<String> classNamesFromJavaFiles(Set<File> javaFiles, Path pathPrefix) {
        return javaFiles.stream().map(f -> classNameFromJavaFile(f, pathPrefix)).collect(Collectors.toSet());
    }

    public static final Optional<Class<?>> loadIfPossible(String className) {
        try {
            Class<?> loaded = ClassUtil.class.getClassLoader().loadClass(className);
            return Optional.of(loaded);
        } catch (ClassNotFoundException e) {
            LOGGER.debug("Cannot load class {}.", className);
            return Optional.empty();
        }
    }

    public static final Set<Class<?>> loadIfPossible(Iterable<String> classNames) {
        // @formatter:off
        return streamOf(classNames)
                .map(ClassUtil::loadIfPossible)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toSet());
        // @formatter:on
    }

    public static final <T> Optional<T> instantiateIfPossible(Class<T> classToInstantiate) {
        try {
            return Optional.of(classToInstantiate.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.debug("Cannot instantiate class {}.", classToInstantiate);
            return Optional.empty();
        }
    }

    public static final <T> Set<T> instantiateIfPossible(Iterable<Class<T>> classesToInstantiate) {
        // @formatter:off
        return streamOf(classesToInstantiate)
                .map(ClassUtil::instantiateIfPossible)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toSet());
        // @formatter:on
    }

    private static <T> Stream<T> streamOf(Iterable<T> classNames) {
        return ImmutableList.copyOf(classNames).stream();
    }

}
