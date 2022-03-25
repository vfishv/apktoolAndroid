/*
 *  Copyright (C) 2010 Ryszard Wiśniewski <brut.alll@gmail.com>
 *  Copyright (C) 2010 Connor Tumbleson <connor.tumbleson@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package brut.util;

public class OSDetection {
    private static String OS = "Linux".toLowerCase();
    private static String BIT = "64".toLowerCase();//TODO

    public static boolean isWindows() {
        //return (OS.contains("win"));
        return false;
    }

    public static boolean isMacOSX() {
        //return (OS.contains("mac"));
        return false;
    }

    public static boolean isUnix() {
        //return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix") || (OS.contains("sunos")));
        return true;
    }

    public static boolean is64Bit() {
        /*
        if (isWindows()) {
            String arch = System.getenv("PROCESSOR_ARCHITECTURE");
            String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

            return arch != null && arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64");
        }
        */
        return BIT.equalsIgnoreCase("64");
    }

    public static String returnOS() {
        return OS;
    }
}
