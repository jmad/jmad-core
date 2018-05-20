/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.cleaning;

import java.nio.file.Paths;

public class ExampleModelPackageCleaner {

    public static void main(String... args) {
        ModelPackageCleaner.cleanUnusedBelow(Paths.get("./src/test"));
    }
}
