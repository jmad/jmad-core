/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

public final class AssertUtil {

    private AssertUtil() {
        /* only static methods */
    }

    public static <T> void requireNull(T oldValue, String message) {
        if (oldValue != null) {
            throw new IllegalStateException(message);
        }
    }

    public static void requireFalse(boolean oldValue, String message) {
        if (oldValue) {
            throw new IllegalStateException(message);
        }
    }
}
