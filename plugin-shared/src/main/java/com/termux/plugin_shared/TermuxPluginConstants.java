package com.termux.plugin_shared;

import android.app.PendingIntent;

public final class TermuxPluginConstants
{
    
    /** Termux package name */
    public static final String TERMUX_PACKAGE_NAME = BuildConfig.TERMUX_PACKAGE_NAME; // Default: "com.termux"
    
    /** Termux plugin service name */
    public static final String TERMUX_PLUGIN_SERVICE_NAME = ".app.plugin.PluginService";
    
    
    /** F-Droid APK release signing certificate SHA-256 digest */
    public static final String APK_RELEASE_FDROID_SIGNING_CERTIFICATE_SHA256_DIGEST = "228FB2CFE90831C1499EC3CCAF61E96E8E1CE70766B9474672CE427334D41C42"; // Default: "228FB2CFE90831C1499EC3CCAF61E96E8E1CE70766B9474672CE427334D41C42"
    
    /** Github APK release signing certificate SHA-256 digest */
    public static final String APK_RELEASE_GITHUB_SIGNING_CERTIFICATE_SHA256_DIGEST = "B6DA01480EEFD5FBF2CD3771B8D1021EC791304BDD6C4BF41D3FAABAD48EE5E1"; // Default: "B6DA01480EEFD5FBF2CD3771B8D1021EC791304BDD6C4BF41D3FAABAD48EE5E1"
    
    /** Google Play Store APK release signing certificate SHA-256 digest */
    public static final String APK_RELEASE_GOOGLE_PLAYSTORE_SIGNING_CERTIFICATE_SHA256_DIGEST = "738F0A30A04D3C8A1BE304AF18D0779BCF3EA88FB60808F657A3521861C2EBF9"; // Default: "738F0A30A04D3C8A1BE304AF18D0779BCF3EA88FB60808F657A3521861C2EBF9"
    
    
    
    /** Android OS permission declared by Termux app in AndroidManifest.xml which can be requested by
     * 3rd party apps to run various commands in Termux app context */
    public static final String PERMISSION_RUN_COMMAND = TERMUX_PACKAGE_NAME + ".permission.RUN_COMMAND"; // Default: "com.termux.permission.RUN_COMMAND"
    
    /**
     * Android OS permission declared by Termux app in AndroidManifest.xml which can be requested by
     * 3rd party apps to connect to the {@link com.termux.app.plugin.PluginService}.
     */
    public static final String PERMISSION_TERMUX_PLUGIN = TERMUX_PACKAGE_NAME + ".permission.TERMUX_PLUGIN"; // Default: "com.termux.permission.TERMUX_PLUGIN"
    
    /**
     * A permission only the Termux app can hold that can be used by 3rd party apps to restrict component access to Termux.
     * The 3rd party apps should also verify the signature of the package {@link TermuxPluginConstants#TERMUX_PACKAGE_NAME}
     * and verify the request came from {@link TermuxPluginConstants#TERMUX_PACKAGE_NAME}.
     */
    public static final String PERMISSION_TERMUX_SIGNATURE = TERMUX_PACKAGE_NAME + ".permission.TERMUX_SIGNATURE"; // Default: "com.termux.permission.TERMUX_SIGNATURE"
    
    
    
    
    /**
     * The value for {@link com.termux.plugin_aidl.IPluginService#runCommand(String, String[], boolean, String, String, String, boolean, String, String, String, String, String, PendingIntent, String)} that will set the new session as
     * the current session and will start {@link TERMUX_ACTIVITY} if its not running to bring
     * the new session to foreground.
     */
    public static final int VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_OPEN_ACTIVITY = 0;
    
    /**
     * The value for {@link #EXTRA_SESSION_ACTION} extra that will keep any existing session
     * as the current session and will start {@link TERMUX_ACTIVITY} if its not running to
     * bring the existing session to foreground. The new session will be added to the left
     * sidebar in the sessions list.
     */
    public static final int VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY = 1;
    
    /**
     * The value for {@link #EXTRA_SESSION_ACTION} extra that will set the new session as
     * the current session but will not start {@link TERMUX_ACTIVITY} if its not running
     * and session(s) will be seen in Termux notification and can be clicked to bring new
     * session to foreground. If the {@link TERMUX_ACTIVITY} is already running, then this
     * will behave like {@link #VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY}.
     */
    public static final int VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_DONT_OPEN_ACTIVITY = 2;
    
    /**
     * The value for {@link #EXTRA_SESSION_ACTION} extra that will keep any existing session
     * as the current session but will not start {@link TERMUX_ACTIVITY} if its not running
     * and session(s) will be seen in Termux notification and can be clicked to bring
     * existing session to foreground. If the {@link TERMUX_ACTIVITY} is already running,
     * then this will behave like {@link #VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY}.
     */
    public static final int VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_DONT_OPEN_ACTIVITY = 3;
    
    /** The minimum allowed value for {@link #EXTRA_SESSION_ACTION}. */
    public static final int MIN_VALUE_EXTRA_SESSION_ACTION = VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_OPEN_ACTIVITY;
    
    /** The maximum allowed value for {@link #EXTRA_SESSION_ACTION}. */
    public static final int MAX_VALUE_EXTRA_SESSION_ACTION = VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_DONT_OPEN_ACTIVITY;
    
    
}
