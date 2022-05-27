package com.termux.plugin_shared;

import android.annotation.SuppressLint;
import android.app.PendingIntent;

import java.util.Formatter;

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
    
    
    @SuppressLint("SdCardPath")
    public static final String TERMUX_INTERNAL_PRIVATE_APP_DATA_DIR_PATH = "/data/data/" + TERMUX_PACKAGE_NAME; // Default: "/data/data/com.termux"
    
    /** Termux app Files directory path */
    public static final String TERMUX_FILES_DIR_PATH = TERMUX_INTERNAL_PRIVATE_APP_DATA_DIR_PATH + "/files"; // Default: "/data/data/com.termux/files"
    
    /** Termux and plugin apps directory path */
    public static final String TERMUX_APPS_DIR_PATH = TERMUX_FILES_DIR_PATH + "/apps"; // Default: "/data/data/com.termux/files/apps"
    
    /** Termux and plugin apps directory path */
    public static final String TERMUX_PLUGINS_DIR_PATH = TERMUX_APPS_DIR_PATH + "/plugins"; // Default: "/data/data/com.termux/files/apps/plugins"
    
    
    /**
     * Termux app run command service to receive commands sent by 3rd party apps.
     */
    public static final class RUN_COMMAND_SERVICE {
        
        /** Intent action to execute command with RUN_COMMAND_SERVICE */
        public static final String ACTION_RUN_COMMAND = TERMUX_PACKAGE_NAME + ".RUN_COMMAND"; // Default: "com.termux.RUN_COMMAND"
        
        /** Intent {@code String} extra for absolute path of command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_COMMAND_PATH = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_PATH"; // Default: "com.termux.RUN_COMMAND_PATH"
        /** Intent {@code String[]} extra for arguments to the executable of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_ARGUMENTS = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_ARGUMENTS"; // Default: "com.termux.RUN_COMMAND_ARGUMENTS"
        /** Intent {@code boolean} extra for whether to replace comma alternative characters in arguments with comma characters for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_REPLACE_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_REPLACE_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS"; // Default: "com.termux.RUN_COMMAND_REPLACE_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS"
        /** Intent {@code String} extra for the comma alternative characters in arguments that should be replaced instead of the default {@link #COMMA_ALTERNATIVE} for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS"; // Default: "com.termux.RUN_COMMAND_COMMA_ALTERNATIVE_CHARS_IN_ARGUMENTS"
        
        /** Intent {@code String} extra for stdin of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_STDIN = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_STDIN"; // Default: "com.termux.RUN_COMMAND_STDIN"
        /** Intent {@code String} extra for current working directory of command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_WORKDIR = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_WORKDIR"; // Default: "com.termux.RUN_COMMAND_WORKDIR"
        /** Intent {@code boolean} extra for whether to run command in background {@link Runner#APP_SHELL} or foreground {@link Runner#TERMINAL_SESSION} for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        @Deprecated
        public static final String EXTRA_BACKGROUND = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_BACKGROUND"; // Default: "com.termux.RUN_COMMAND_BACKGROUND"
        /** Intent {@code String} extra for command the {@link Runner} for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_RUNNER = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_RUNNER"; // Default: "com.termux.RUN_COMMAND_RUNNER"
        /** Intent {@code String} extra for custom log level for background commands defined by {@link com.termux.shared.logger.Logger} for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_BACKGROUND_CUSTOM_LOG_LEVEL = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_BACKGROUND_CUSTOM_LOG_LEVEL"; // Default: "com.termux.RUN_COMMAND_BACKGROUND_CUSTOM_LOG_LEVEL"
        /** Intent {@code String} extra for session action of {@link Runner#TERMINAL_SESSION} commands for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_SESSION_ACTION = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_SESSION_ACTION"; // Default: "com.termux.RUN_COMMAND_SESSION_ACTION"
        /** Intent {@code String} extra for session name of {@link Runner#TERMINAL_SESSION} commands for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_SESSION_NAME = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_SESSION_NAME"; // Default: "com.termux.RUN_COMMAND_SESSION_NAME"
        /** Intent {@code String} extra for the {@link ExecutionCommand.SessionCreateMode}  for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent. */
        public static final String EXTRA_SESSION_CREATE_MODE = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_SESSION_CREATE_MODE"; // Default: "com.termux.RUN_COMMAND_SESSION_CREATE_MODE"
        /** Intent {@code String} extra for label of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_COMMAND_LABEL = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_COMMAND_LABEL"; // Default: "com.termux.RUN_COMMAND_COMMAND_LABEL"
        /** Intent markdown {@code String} extra for description of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_COMMAND_DESCRIPTION = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_COMMAND_DESCRIPTION"; // Default: "com.termux.RUN_COMMAND_COMMAND_DESCRIPTION"
        /** Intent markdown {@code String} extra for help of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_COMMAND_HELP = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_COMMAND_HELP"; // Default: "com.termux.RUN_COMMAND_COMMAND_HELP"
        /** Intent {@code Parcelable} extra for the pending intent that should be sent with the result of the execution command to the execute command caller for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_PENDING_INTENT = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_PENDING_INTENT"; // Default: "com.termux.RUN_COMMAND_PENDING_INTENT"
        /** Intent {@code String} extra for the directory path in which to write the result of
         * the execution command for the execute command caller for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_RESULT_DIRECTORY = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_RESULT_DIRECTORY"; // Default: "com.termux.RUN_COMMAND_RESULT_DIRECTORY"
        /** Intent {@code boolean} extra for whether the result should be written to a single file
         * or multiple files (err, errmsg, stdout, stderr, exit_code) in
         * {@link #EXTRA_RESULT_DIRECTORY} for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_RESULT_SINGLE_FILE = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_RESULT_SINGLE_FILE"; // Default: "com.termux.RUN_COMMAND_RESULT_SINGLE_FILE"
        /** Intent {@code String} extra for the basename of the result file that should be created
         * in {@link #EXTRA_RESULT_DIRECTORY} if {@link #EXTRA_RESULT_SINGLE_FILE} is {@code true}
         * for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_RESULT_FILE_BASENAME = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_RESULT_FILE_BASENAME"; // Default: "com.termux.RUN_COMMAND_RESULT_FILE_BASENAME"
        /** Intent {@code String} extra for the output {@link Formatter} format of the
         * {@link #EXTRA_RESULT_FILE_BASENAME} result file for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_RESULT_FILE_OUTPUT_FORMAT = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_RESULT_FILE_OUTPUT_FORMAT"; // Default: "com.termux.RUN_COMMAND_RESULT_FILE_OUTPUT_FORMAT"
        /** Intent {@code String} extra for the error {@link Formatter} format of the
         * {@link #EXTRA_RESULT_FILE_BASENAME} result file for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_RESULT_FILE_ERROR_FORMAT = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_RESULT_FILE_ERROR_FORMAT"; // Default: "com.termux.RUN_COMMAND_RESULT_FILE_ERROR_FORMAT"
        /** Intent {@code String} extra for the optional suffix of the result files that should be
         * created in {@link #EXTRA_RESULT_DIRECTORY} if {@link #EXTRA_RESULT_SINGLE_FILE} is
         * {@code false} for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
        public static final String EXTRA_RESULT_FILES_SUFFIX = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_RESULT_FILES_SUFFIX"; // Default: "com.termux.RUN_COMMAND_RESULT_FILES_SUFFIX"
        
    }
    
    /** Intent {@code Bundle} extra to store result of execute command that is sent back for the
     * TERMUX_SERVICE.ACTION_SERVICE_EXECUTE intent if the {@link #EXTRA_PENDING_INTENT} is not
     * {@code null} */
    public static final String EXTRA_PLUGIN_RESULT_BUNDLE = "result"; // Default: "result"
    /** Intent {@code String} extra for stdout value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
    public static final String EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT = "stdout"; // Default: "stdout"
    /** Intent {@code String} extra for original length of stdout value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
    public static final String EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT_ORIGINAL_LENGTH = "stdout_original_length"; // Default: "stdout_original_length"
    /** Intent {@code String} extra for stderr value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
    public static final String EXTRA_PLUGIN_RESULT_BUNDLE_STDERR = "stderr"; // Default: "stderr"
    /** Intent {@code String} extra for original length of stderr value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
    public static final String EXTRA_PLUGIN_RESULT_BUNDLE_STDERR_ORIGINAL_LENGTH = "stderr_original_length"; // Default: "stderr_original_length"
    /** Intent {@code int} extra for exit code value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
    public static final String EXTRA_PLUGIN_RESULT_BUNDLE_EXIT_CODE = "exitCode"; // Default: "exitCode"
    /** Intent {@code int} extra for err value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
    public static final String EXTRA_PLUGIN_RESULT_BUNDLE_ERR = "err"; // Default: "err"
    /** Intent {@code String} extra for errmsg value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
    public static final String EXTRA_PLUGIN_RESULT_BUNDLE_ERRMSG = "errmsg"; // Default: "errmsg"
    
    /** Termux app run command service name. */
    public static final String RUN_COMMAND_SERVICE_NAME = TERMUX_PACKAGE_NAME + ".app.RunCommandService"; // Termux app service to receive commands from 3rd party apps "com.termux.app.RunCommandService"
    
    
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
