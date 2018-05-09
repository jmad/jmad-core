/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.util;

import static cern.accsoft.steering.jmad.util.PathUtil.parentPath;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PathUtilTest {

    @Test
    public void emptyStringReturnsEmpty() {
        assertThat(parentPath("")).isEqualTo("");
    }

    @Test
    public void noSlashReturnsEmpty() {
        assertThat(parentPath("aText")).isEqualTo("");
    }

    @Test
    public void startingSlashReturnsEmpty() {
        assertThat(parentPath("/aText")).isEqualTo("");
    }

    @Test
    public void somePathReturnsPath() {
        assertThat(parentPath("aPath/aText")).isEqualTo("aPath");
    }

    @Test
    public void someLongerPathReturnsPath() {
        String path = "/a/longer/path/with/leading/slash";
        assertThat(parentPath(path + "/aText")).isEqualTo(path);
    }

    @Test
    public void someLongerPathWithTrailingSlash() {
        String path = "/a/longer/path";
        assertThat(parentPath(path + "/")).isEqualTo(path);
    }

}
