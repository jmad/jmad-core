package cern.accsoft.steering.jmad.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * utility to collect the effective classpath. This is not that trivial since the classpath is combine of the entries
 * from the system property and also from classpath entries in jar file.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class ClassPathUtil {

    /** the logger for the class */
    private final static Logger LOGGER = Logger.getLogger(ClassPathUtil.class);

   
    /**
     * collects all the classpath-entries from the different resources:
     * <ul>
     * <li>entries of the system classpath
     * <li>the loaded jars
     * <li>entries from classpath-attribute in any jars as detected before
     * </ul>
     * 
     * @return all the entries
     */
    public static final Set<String> getAllClassPathEntries() {
        Set<String> entries = new HashSet<String>();

        processEntries(entries, getSystemPropertyClassPathEntries(), true);
        processEntries(entries, getLoadedJarNames(), false);

        return entries;
    }

    /**
     * adds all the newEntries to the entries, if they are not already there. And if a new entry is added and the new
     * entry represents a jar then it checks also if there are entries in the manifes of the classpath and adds them
     * also by calling itself recursively.
     * 
     * @param entries the set of entries to which the new ones shall be added
     * @param newEntries the entries to add.
     */
    private static void processEntries(Set<String> entries, List<String> newEntries, boolean assumeFile) {
        for (String entry : newEntries) {
            if (entry == null || entry.isEmpty()) {
                continue;
            }
            String unifiedEntry = unifyName(entry, assumeFile);
            if (!entries.contains(unifiedEntry)) {
                entries.add(unifiedEntry);
                LOGGER.debug("Added classpath-entry: '" + unifiedEntry + "'");

                if (JarUtil.isJarName(unifiedEntry)) {
                    processEntries(entries, getClassPathEntriesFromJarFile(unifiedEntry), true);
                }
            }
        }
    }

    /**
     * extracts the class path entries from the system classpath.
     * 
     * @return all the entries from the system classpath
     */
    private static List<String> getSystemPropertyClassPathEntries() {
        String classPath = System.getProperty("java.class.path", ".");
        String[] classPathEntries = classPath.split(File.pathSeparator);
        return Arrays.asList(classPathEntries);
    }

    /**
     * extracts the class path entries from the jar-manifest
     * 
     * @param fileName the filename from which to extract the entries
     * @return the entries
     */
    private static List<String> getClassPathEntriesFromJarFile(String fileName) {
        String manifestClassPath = JarUtil.getManifestClassPathFromJarFile(fileName);
        String[] classPathEntries = manifestClassPath.split(" ");
        return Arrays.asList(classPathEntries);
    }

    /**
     * crawls through all classloaders and gets the names of all loaded jars
     * 
     * @return a list of jar names
     */
    private static List<String> getLoadedJarNames() {
        List<String> retval = new ArrayList<String>();
        ClassLoader classLoader = ClassPathUtil.class.getClassLoader();
        while (classLoader != null) {
            if (classLoader instanceof URLClassLoader) {
                URL[] urls = ((URLClassLoader) classLoader).getURLs();
                for (URL url : urls) {
                    if (url != null) {
                        String name = url.toExternalForm();
                        if (JarUtil.isJarName(name)) {
                            retval.add(name);
                        }
                    }
                }
            }
            classLoader = classLoader.getParent();
        }
        return retval;
    }

    
    private static final String unifyName(final String name, boolean file) {
        String newName = name.replaceAll("\\\\", "\\/");
        if (file) {
            if (!newName.startsWith("file:")) {
                newName = "file:" + newName;
            }
        }
        if (OsUtil.isWindows()) {
            /*
             * remove trailing slashes
             */
            newName = newName.replace("file:/", "file:");
        }
        
        return newName;
    }
    
}
