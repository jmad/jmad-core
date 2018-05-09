// @formatter:off
 /*******************************************************************************
 *
 * This file is part of JMad.
 * 
 * Copyright (c) 2008-2011, CERN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ******************************************************************************/
// @formatter:on

/*
 * $Id: ResourceUtil.java,v 1.5 2009-02-25 18:48:27 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:27 $ $Revision: 1.5 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.jmad.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * This class provides methods to handle resource files
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public final class ResourceUtil {
    /** the logger for the class */
    private static final Logger LOGGER = Logger.getLogger(ResourceUtil.class);

    private ResourceUtil() {
        /* only static methods */
    }

    /**
     * searches all the resources in the classpath to get a list of all the resources in the given package.
     * 
     * @param packageName the package name in which to search for the resources.
     * @return the names of the resources (without leading package names)
     */
    public static Collection<String> getResourceNames(String packageName) {
        String patternString = ".*" + packageName.replaceAll("\\/", "\\\\/").replaceAll("\\.", "\\\\/") + "\\/[^\\/]+";
        LOGGER.debug("Searching for pattern:" + patternString);
        Pattern pattern = Pattern.compile(patternString);
        Collection<String> resources = getResources(pattern);
        List<String> names = new ArrayList<String>();
        for (String resource : resources) {
            File file = new File(resource);
            names.add(file.getName());
        }
        return names;
    }

    /**
     * just adds a prefix to the path
     * 
     * @param filepath the path to which to add the prefix
     * @param offset the offset to add as prefix
     * @return the whole path
     */
    public static String prependPathOffset(String filepath, String offset) {
        if ((offset != null) && (offset.length() > 0)) {
            /*
             * IMPORTANT: Do NOT use File.separator here, since this is also used for resource-paths!
             */
            return offset + "/" + filepath;
        } else {
            return filepath;
        }
    }

    /**
     * Converts the given package name into a path like string (e.g. "a.java.pkg" is converted to "a/java/pkg"). Hereby
     * always slashes ("/") are used and not the system file separator.
     * 
     * @param packageName the package name to convert
     * @return the converted package name as path represention
     */
    public static String packageToPath(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }

    /*
     * The following code was found on the net at http://forums.devx.com/showthread.php?t=153784
     */

    /**
     * for all elements of java.class.path get a Collection of resources. All ressources can be found with:
     * <p>
     * <code>
     * Pattern pattern = Pattern.compile(".*"); 
     * </code>
     * 
     * @param pattern the pattern to match
     * @return the resources in the order they are found
     */
    public static Collection<String> getResources(Pattern pattern) {
        ArrayList<String> retval = new ArrayList<String>();
        retval.addAll(getResourcesFromClassPath(pattern));
        return retval;
    }

    /**
     * @param pattern a regular expression pattern to search
     * @return all names of the resources included in the class-path (jars and directories)
     */
    private static Collection<String> getResourcesFromClassPath(Pattern pattern) {
        List<String> retval = new ArrayList<String>();

        Set<String> classPathElements = ClassPathUtil.getAllClassPathEntries();
        for (String element : classPathElements) {
            LOGGER.debug("Processing classpath entry '" + element + "'");
            retval.addAll(getResources(element, pattern));
        }
        return retval;
    }

    private static Collection<String> getResources(String element, Pattern pattern) {
        ArrayList<String> retval = new ArrayList<String>();
        if (JarUtil.isJarName(element)) {
            retval.addAll(getResourcesFromJarFile(element, pattern));
        } else {
            File file = getFileFromUrlString(element);
            if (file == null) {
                LOGGER.warn("Could not correctly associate '" + element + "' to a file!?");
            } else {
                if (file.isDirectory()) {
                    retval.addAll(getResourcesFromDirectory(file, pattern));
                } else {
                    LOGGER.warn("Do not know how to process classpath entry '" + element + "'");
                }
            }
        }
        return retval;
    }

    private static Collection<String> getResourcesFromJarFile(String fileName, Pattern pattern) {
        Collection<String> retvals = new ArrayList<String>();
        JarFile jarFile = null;
        try {
            jarFile = JarUtil.getJarFile(fileName);
            retvals = ZipUtil.getFileNames(jarFile, pattern);
        } catch (IOException e) {
            LOGGER.warn("Unable to open jar '" + fileName + "'. Ignoring it.");
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    LOGGER.error("Erro while closing jar file.", e);
                }
            }
        }
        return retvals;
    }

    private static Collection<String> getResourcesFromDirectory(File directory, Pattern pattern) {
        ArrayList<String> retval = new ArrayList<String>();
        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                retval.addAll(getResourcesFromDirectory(file, pattern));
            } else {
                try {
                    String fileName = file.getCanonicalPath().replaceAll("\\\\", "/");
                    boolean accept = pattern.matcher(fileName).matches();
                    if (accept) {
                        retval.add(fileName);
                    }
                } catch (IOException e) {
                    throw new Error(e);
                }
            }
        }
        return retval;
    }

    private static final File getFileFromUrlString(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e1) {
            return null;
        }
        File f = null;
        f = new File(url.getPath());
        return f;
    }
}
