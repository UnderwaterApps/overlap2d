/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.utils;

/**
 * types of Operating Systems
 *
 * please keep the note below as a pseudo-license
 *
 * helper class to check the operating system this Java VM runs in
 * http://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
 * compare to http://svn.terracotta.org/svn/tc/dso/tags/2.6.4/code/base/common/src/com/tc/util/runtime/Os.java
 * http://www.docjar.com/html/api/org/apache/commons/lang/SystemUtils.java.html
 */
public enum OSType {
    MacOS("mac", "darwin"),
    Windows("win"),
    Linux("nux"),
    Other("generic");

    private static OSType detectedOS;

    private final String[] keys;

    private OSType(String... keys) {
        this.keys = keys;
    }

    private boolean match(String osKey) {
        for (int i = 0; i < keys.length; i++) {
            if (osKey.indexOf(keys[i]) != -1)
                return true;
        }
        return false;
    }

    public static OSType getOS_Type() {
        if (detectedOS == null)
            detectedOS = getOperatingSystemType(System.getProperty("os.name", Other.keys[0]).toLowerCase());
        return detectedOS;
    }

    private static OSType getOperatingSystemType(String osKey) {
        for (OSType osType : values()) {
            if (osType.match(osKey))
                return osType;
        }
        return Other;
    }
}
