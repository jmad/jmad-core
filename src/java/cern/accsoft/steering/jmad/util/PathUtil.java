/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.util;

public final class PathUtil {

    private PathUtil() {
        /* only static methods */
    }

    /**
     * Given a string representing a file path (using slashes as separator), returns the path part (removing the file
     * part, i.e. the part after the last slash)
     * 
     * @param fileName the fileName from which to return the path
     * @return the path of the full file name
     */
    public static final String parentPath(String fileName) {
        int lastSlash = fileName.lastIndexOf('/');
        if (lastSlash >= 0) {
            return fileName.substring(0, lastSlash);
        } else {
            return "";
        }
    }

}
