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