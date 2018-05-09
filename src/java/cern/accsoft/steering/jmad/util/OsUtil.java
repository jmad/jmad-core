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
 * $Id: OsUtil.java,v 1.3 2009-02-25 18:48:26 kfuchsbe Exp $
 * 
 * $Date: 2009-02-25 18:48:26 $ $Revision: 1.3 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.jmad.util;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * provides some methods to determine the operating-system
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public final class OsUtil {
    /* the name of the system-property for the OS name */
    private static final String OSNAME_PROPERTY = "os.name";

    /* the name of the system-property for the OS CPU architecture */
    private static final String OSARCH_PROPERTY = "os.arch";

    /*
     * the prefixes for different operating systems. Use lower case strings here!
     */
    private static final String PREFIX_WINDOWS = "windows";
    private static final String PREFIX_LINUX = "linux";
    private static final String PREFIX_OSX = "mac os x";

    /*
     * identifiers for 32 bit and 64 bit intel x86 architectures
     */
    private static final Set<String> ARCH_X86_32 = ImmutableSet.of("i386", "i486", "i586", "i686", "x86", "x86_32");
    private static final Set<String> ARCH_X86_64 = ImmutableSet.of("x86_64", "amd64");

    /* the name of the OS in lower case */
    private static final String OS_NAME = System.getProperty(OSNAME_PROPERTY).toLowerCase();

    /* the CPU architecture of the OS in lower case */
    private static final String OS_ARCH = System.getProperty(OSARCH_PROPERTY).toLowerCase();

    private OsUtil() {
        /* only static methods */
    }

    /**
     * @return true, if VM is running on a windows - platform, false otherwise
     */
    public static boolean isWindows() {
        return OS_NAME.startsWith(PREFIX_WINDOWS);
    }

    /**
     * @return true, if the VM is running on a mac-osx platform, false otherwise.
     */
    public static boolean isOsX() {
        return OS_NAME.startsWith(PREFIX_OSX);
    }

    /**
     * @return true if VM is running on a Linux platform, false otherwise
     */
    public static boolean isLinux() {
        return OS_NAME.startsWith(PREFIX_LINUX);
    }

    /**
     * @return true if the VM is running on an intel x86 32-bit CPU, false otherwise
     */
    public static boolean isIntel32BitArchitecture() {
        return ARCH_X86_32.contains(OS_ARCH);
    }

    /**
     * @return true if the VM is running on an intel x86 64-bit CPU, false otherwise
     */
    public static boolean isIntel64BitArchitecture() {
        return ARCH_X86_64.contains(OS_ARCH);
    }

    /**
     * @return the name of the OS
     */
    public static String getOsName() {
        return OS_NAME;
    }

    /**
     * @return the CPU architecture
     */
    public static String getCpuArchitecture() {
        return OS_ARCH;
    }
}
