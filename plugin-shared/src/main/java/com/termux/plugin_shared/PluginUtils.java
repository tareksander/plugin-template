package com.termux.plugin_shared;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.termux.plugin_aidl.IPluginService;

import java.security.MessageDigest;

public final class PluginUtils
{
    
    /**
     * @param c The {@link Context} used to get the {@link PackageManager}.
     * @return Returns {@code true} if {@link BuildConfig#TERMUX_PACKAGE_NAME} is installed, false if not or there was en error.
     */
    public static boolean isTermuxInstalled(Context c) {
        return getTermuxPackageInfo(c) != null;
    }
    
    /**
     * @param c The {@link Context} used to get the {@link PackageManager}.
     * @return Returns the {@link PackageInfo} for the {@link BuildConfig#TERMUX_PACKAGE_NAME} package or null if it can't be found.
     */
    public static PackageInfo getTermuxPackageInfo(Context c) {
        try {
            return c.getPackageManager().getPackageInfo(BuildConfig.TERMUX_PACKAGE_NAME, 0);
        }
        catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
    
    /**
     * @param c The {@link Context} used to get the {@link PackageManager}.
     * @return The version code of the {@link BuildConfig#TERMUX_PACKAGE_NAME} package, or null if the package cannot be found.
     */
    public static Integer getTermuxVersionCode(Context c) {
        PackageInfo termuxAppInfo = getTermuxPackageInfo(c);
        if (termuxAppInfo != null) {
            return termuxAppInfo.versionCode;
        }
        return null;
    }
    
    
    /**
     * Bind the Termux plugin service and check the Termux package signature beforehand.
     * Returns a wrapper providing overloaded methods for {@link IPluginService}
     * 
     * @param c The context used to bind the service.
     * @return An {@link IPluginService} object or null if the termux app signature doesn't match
     * or the service refused the connection.
     */
    public static PluginServiceWrapper bindPluginService(Context c) {
        Context app = c.getApplicationContext();
        // fail if signature not valid
        if (! checkTermuxPackageSignature(app)) {
            return null;
        }
        
        final Object lock = new Object();
    
        final boolean[] finished = {false};
        final IPluginService[] service = new IPluginService[1];
        final Intent pluginServiceIntent = new Intent();
        
        pluginServiceIntent.setComponent(ComponentName.createRelative(TermuxPluginConstants.TERMUX_PACKAGE_NAME, TermuxPluginConstants.TERMUX_PLUGIN_SERVICE_NAME));
    
        final ServiceConnection con = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder b) {
                service[0] = IPluginService.Stub.asInterface(b);
                finished[0] = true;
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
    
            @Override
            public void onServiceDisconnected(ComponentName name) {
                app.unbindService(this);
            }
    
            @Override
            public void onNullBinding(ComponentName name) {
                ServiceConnection.super.onNullBinding(name);
                finished[0] = true;
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        };
        try {
            if (!app.bindService(pluginServiceIntent, con, Context.BIND_ABOVE_CLIENT | Context.BIND_AUTO_CREATE)) {
                return null;
            }
        } catch (SecurityException ignored) {
            return null;
        }
        while (!finished[0]) {
            try {
                synchronized (lock) {
                    lock.wait();
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        if (service[0] == null) {
            app.unbindService(con);
            return null;
        }
        return new PluginServiceWrapper(service[0]);
    }
    
    
    
    /**
     * Checks if the package {@link TermuxPluginConstants#TERMUX_PACKAGE_NAME} matches one of the signatures
     * defined in {@link TermuxPluginConstants}.
     * 
     * @param c The Context used to get the {@link android.content.pm.PackageManager}.
     * @return Returns {@code true} if {@link TermuxPluginConstants#TERMUX_PACKAGE_NAME} has a valid signature, {@code false} otherwise.
     */
    public static boolean checkTermuxPackageSignature(Context c) {
        String digest = getSigningCertificateSHA256DigestForPackage(c, TermuxPluginConstants.TERMUX_PACKAGE_NAME);
        if (digest == null) {
            return false;
        }
        switch (digest) {
            case TermuxPluginConstants.APK_RELEASE_FDROID_SIGNING_CERTIFICATE_SHA256_DIGEST:
            case TermuxPluginConstants.APK_RELEASE_GITHUB_SIGNING_CERTIFICATE_SHA256_DIGEST:
            case TermuxPluginConstants.APK_RELEASE_GOOGLE_PLAYSTORE_SIGNING_CERTIFICATE_SHA256_DIGEST:
                return true;
        }
        return false;
    }
    
    
    /**
     * Get the {@code SHA-256 digest} of signing certificate for the {@code packageName}.
     *
     * @param context The {@link Context} for operations.
     * @param packageName The package name of the package.
     * @return Returns the {@code SHA-256 digest}. This will be {@code null} if an exception is raised.
     */
    @Nullable
    private static String getSigningCertificateSHA256DigestForPackage(@NonNull final Context context, @NonNull final String packageName) {
        try {
            PackageInfo packageInfo = getPackageInfoForPackage(context, packageName, PackageManager.GET_SIGNATURES);
            if (packageInfo == null) return null;
            return bytesToHex(MessageDigest.getInstance("SHA-256").digest(packageInfo.signatures[0].toByteArray()));
        } catch (final Exception e) {
            return null;
        }
    }
    
    /**
     * Get the {@link PackageInfo} for the package associated with the {@code packageName}.
     *
     * @param context The {@link Context} for operations.
     * @param packageName The package name of the package.
     * @param flags The flags to pass to {@link PackageManager#getPackageInfo(String, int)}.
     * @return Returns the {@link PackageInfo}. This will be {@code null} if an exception is raised.
     */
    @Nullable
    private static PackageInfo getPackageInfoForPackage(@NonNull final Context context, @NonNull final String packageName, final int flags) {
        try {
            return context.getPackageManager().getPackageInfo(packageName, flags);
        } catch (final Exception e) {
            return null;
        }
    }
    
    
    
    
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    
    /**
     * Get the {@code hex string} from a {@link byte[]}.
     *
     * @param bytes The {@link byte[]} value.
     * @return Returns the {@code hex string} value.
     */
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
