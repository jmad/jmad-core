/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.util;

import static cern.accsoft.steering.jmad.util.ClassUtil.classNameFromJavaFile;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ClassUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final File file = new File("src/test/cern/accsoft/steering/jmad/util/TestFile.java");
    private final Path rootPath = Paths.get("src/test");

    @Test
    public void notNull() {
        assertThat(classNameFromJavaFile(file, rootPath)).isNotNull();
    }

    @Test
    public void nameIsCorrect() {
        assertThat(classNameFromJavaFile(file, rootPath)).isEqualTo("cern.accsoft.steering.jmad.util.TestFile");
    }

    @Test
    public void throwsIsFileNotBelowPathPrefix() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("below the given path prefix");

        classNameFromJavaFile(file, Paths.get("src/someother"));
    }
    
    @Test
    public void throwsIfNotJavaFile()  {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("not end in '.java'");
        
        classNameFromJavaFile(new File("src/test/cern/accsoft/steering/jmad/util/TestFile.someother"), rootPath);
    }

}
