/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.cleaning;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cern.accsoft.steering.jmad.tools.modeldefs.cleaning.conf.ModelFileCleaningConfiguration;

public final class ModelPackageCleaner {

    private static final String DEFAULT_PATH = "./src/java";

    private ModelPackageCleaner() {
        /* only static methods */
    }

    public static void cleanUnusedBelow(String rootPath) {
        requireNonNull(rootPath, "rootPath must not be null");
        cleanUnusedBelow(Paths.get(rootPath));
    }

    public static void cleanUnusedBelow(Path rootPath) {
        @SuppressWarnings("resource")
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ModelFileCleaningConfiguration.class);
        UnusedLocalFileDetector detector = ctx.getBean(UnusedLocalFileDetector.class);
        Set<File> unusedFiles = detector.detectUnusedFiles(rootPath);

        if (unusedFiles.isEmpty()) {
            System.out.println("No unused files. Nothing to do.");
            return;
        }

        System.out.println("\nThe following files seem to be unused:");
        System.out.println("--------------------------------------");
        for (File f : unusedFiles) {
            System.out.println(f.getAbsolutePath());
        }
        System.out.println("--------------------------------------");

        System.out.println("Do you want to delete these files (" + unusedFiles.size() + " in total) (yes/no)?");

        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        scanner.close();

        if ("yes".equals(answer)) {
            unusedFiles.stream().forEach(File::delete);
            System.out.println("Successfully deleted " + unusedFiles.size() + " files.");
            System.out.println("DONE");
        } else {
            System.out.println("aborted by user");
            System.out.println("DONE");
        }
    }

    public static void main(String... args) {
        if (args.length > 1) {
            throw new IllegalArgumentException("Only one argument allowed! (" + args.length + " were given)");
        }
        String path = DEFAULT_PATH;
        if (args.length == 1) {
            path = args[0];
        }
        cleanUnusedBelow(Paths.get(path));
    }

}
