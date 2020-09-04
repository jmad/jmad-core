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
 * $Id: MadXBin.java,v 1.6 2008-12-12 14:48:13 kfuchsbe Exp $
 *
 * $Date: 2008-12-12 14:48:13 $ $Revision: 1.6 $ $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.jmad.bin;

import java.io.File;
import java.io.IOException;

import cern.accsoft.steering.jmad.util.OsUtil;
import cern.accsoft.steering.jmad.util.StreamUtil;
import cern.accsoft.steering.jmad.util.TempFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Determines the correct version of the madx-executable and provides methods to start its execution. Depending on the
 * operating system the correct executable is extracted to a temporary directory and can be executed from there.
 *
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class MadxBinImpl implements MadxBin {
    private static final Logger LOGGER = LoggerFactory.getLogger(MadxBinImpl.class);

    private static final String EXTERNAL_MADX_EXECUTABLE_PATH_PROP = "cern.jmad.kernel.madxpath";

    private static final String BIN_NAME = "madx";

    private static final String RESOURCE_PREFIX_WIN = "win/";
    private static final String RESOURCE_PREFIX_LINUX = "linux/";
    private static final String RESOURCE_PREFIX_OSX = "osx/";

    /**
     * The file util to use (injected by spring)
     */
    private TempFileUtil fileUtil;

    /**
     * The filename of the executable, which then can be called by a shell
     */
    private String executablePath;

    /**
     * init-method called by spring
     */
    public void init() {
        LOGGER.info("Preparing MAD-X binary for OS " + OsUtil.getOsName());
        extractExecutable();
    }

    @Override
    public Process execute() throws IOException {
        return Runtime.getRuntime().exec(getExecutablePath());
    }

    /**
     * @return the path to the executable depending on the OS
     */
    private String getExecutablePath() {
        return executablePath;
    }

    /**
     * @return the name of the resource depending on the OS
     */
    private static String getResourceName() {
        if (OsUtil.isWindows()) {
            return RESOURCE_PREFIX_WIN + BIN_NAME;
        } else if (OsUtil.isLinux()) {
            return RESOURCE_PREFIX_LINUX + BIN_NAME;
        } else if (OsUtil.isOsX()) {
            return RESOURCE_PREFIX_OSX + BIN_NAME;
        } else {
            return null;
        }
    }

    /**
     * copies the executable to the actual path. This is necessary, when the file is included in a jar.
     */
    private void extractExecutable() {
        if (fileUtil == null) {
            return;
        }

        String executableName = BIN_NAME;
        String resourceName = getResourceName();
        String sysPropExecutablePath = System.getProperty(EXTERNAL_MADX_EXECUTABLE_PATH_PROP);
        if (sysPropExecutablePath != null) {
            LOGGER.info("Using madx binary '{}' (from system property {}).", sysPropExecutablePath,
                    EXTERNAL_MADX_EXECUTABLE_PATH_PROP);
            executablePath = sysPropExecutablePath;
        } else if (resourceName != null) {
            LOGGER.debug("Extracting madx binary for further use.");
            File file = fileUtil.getOutputFile(executableName);
            StreamUtil.toFile(MadxBinImpl.class.getResourceAsStream(resourceName), file);
            file.setExecutable(true);
            executablePath = file.getAbsolutePath();
        } else {
            LOGGER.warn("No madx binary is available in the jar for operating system '{}',\n" //
                            + "and the system property '{}' is not set!\n"  //
                            + "If you have no executable named '{}' in the path,\n" //
                            + "you will not be able to perform any calculations!", //
                    OsUtil.getOsName(), EXTERNAL_MADX_EXECUTABLE_PATH_PROP, executableName);
            executablePath = executableName;
        }
    }

    public void setFileUtil(TempFileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }
}
