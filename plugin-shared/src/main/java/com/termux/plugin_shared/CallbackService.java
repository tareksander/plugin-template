package com.termux.plugin_shared;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.termux.plugin_aidl.IPluginCallback;

/**
 * Template implementation of a callback service for the Termux plugin system.
 * Override {@link #getCallbacks()} in a subclass and provide your own implementation.
 * {@link Callbacks} also checks that the calling package is Termux in the first transaction.
 * The signature is not verified, as that should have happened when the plugin connected to Termux.
 * <br>
 * If you use this, you also have to declare the foreground service permission in the manifest.
 */
public abstract class CallbackService extends Service
{
    /**
     * This is defined so you can use the service context inside {@link Callbacks}.
     */
    protected final CallbackService mService = this;
    private final Callbacks mBinder = this.getCallbacks();
    
    @Override
    public void onStart(Intent intent, int startId) {
        stopSelf();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopSelf();
        return Service.START_NOT_STICKY;
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    /**
     * This method has to return the {@link Callbacks} object for {@link IPluginCallback} interface.
     * @return The callbacks object.
     */
    protected abstract @NonNull Callbacks getCallbacks();
    
    /**
     * Implements {@link IPluginCallback.Stub} and already defines {@link Stub#getCallbackVersion()} to be the callback version included in this library.
     * Also checks that the calling package is Termux in the first transaction.
     */
    public abstract class Callbacks extends IPluginCallback.Stub {
        boolean unverified = true;
        
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (unverified) {
                // check the Termux signature
                if (! PluginUtils.checkTermuxPackageSignature(mService)) {
                    throw new SecurityException("Wrong signature for " + TermuxPluginConstants.TERMUX_PACKAGE_NAME);
                }
                // check for the Termux package name in the list of packages for the Binder transaction uid
                boolean found = false;
                for (String pkg : mService.getPackageManager().getPackagesForUid(Binder.getCallingUid())) {
                    if (TermuxPluginConstants.TERMUX_PACKAGE_NAME.equals(pkg)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    unverified = false;
                } else {
                    throw new SecurityException("Only " + TermuxPluginConstants.TERMUX_PACKAGE_NAME + " can bind to this service.");
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
    
        @Override
        public int getCallbackVersion() {
            return IPluginCallback.CURRENT_CALLBACK_VERSION;
        }
    }
}
