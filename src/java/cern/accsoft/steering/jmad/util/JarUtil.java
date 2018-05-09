package cern.accsoft.steering.jmad.util;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;

/**
 * Utility to get attributes from jar-manifest.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class JarUtil {

    /** The logger for the class */
    private static final Logger LOGGER = Logger.getLogger(JarUtil.class);

    /** the file extension to detect a jar */
    private static final String JAR_EXT = ".jar";

    /** The manifest key for the classpath */
    private static final String KEY_CLASSPATH = "Class-Path";

    /**
     * @param fileName the filename of the jar to get the manifest from
     * @return The classpath entry in the jar-file or an empty string.
     */
    public static final String getManifestClassPathFromJarFile(String fileName) {
        JarFile jar = null;
        String classpath = null;
        try {
            jar = getJarFile(fileName);
            Manifest manifest = jar.getManifest();
            if (manifest == null) {
                return "";
            }
            Attributes attributes = manifest.getMainAttributes();
            classpath = attributes.getValue(KEY_CLASSPATH);
        } catch (IOException e) {
            LOGGER.warn("Could not access jar file. '" + fileName + "'. Ignoring it.");
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                    LOGGER.error("Erro while closing jar file.", e);
                }
            }
        }
        if (classpath == null) {
            return "";
        } else {
            return classpath;
        }
    }

    public static final JarFile getJarFile(String name) throws IOException {
        URL url = new URL("jar:" + name + "!/");

        /* Get the jar file */
        JarURLConnection conn = (JarURLConnection) url.openConnection();
        return conn.getJarFile();
    }

    /**
     * @param name the name to check
     * @return <code>true</code> if the given name is the name of a jarfile, <code>false</code> otherwise
     */
    public static final boolean isJarName(String name) {
        return name.endsWith(JAR_EXT);
    }
}
