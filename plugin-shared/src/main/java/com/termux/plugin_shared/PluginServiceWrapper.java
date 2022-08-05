package com.termux.plugin_shared;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.termux.plugin_aidl.IPluginCallback;
import com.termux.plugin_aidl.IPluginService;
import com.termux.plugin_aidl.Task;

/**
 * Provides overloaded methods so the AIDL doesn't have to include overloaded methods.
 */
public class PluginServiceWrapper implements IPluginService, AutoCloseable
{
    private IPluginService mInterface;
    private ServiceConnection mCon;
    private Context mContext;
    
    public PluginServiceWrapper(@NonNull IPluginService pluginService, @NonNull ServiceConnection connection, @NonNull Context c) {
        mInterface = pluginService;
        mCon = connection;
        mContext = c;
    }
    
    /**
     * Unbinds the PluginService, invalidating this connection. All further methods will fail.
     */
    public void unbind() {
        if (mInterface == null) throw new NullPointerException("PluginServiceWrapper already unbound");
        try {
            mContext.unbindService((mCon));
        } catch(Exception e) {
            Log.d("PluginServiceWrapper", "Exception while unbinding PluginService:", e);
        }
        mContext = null;
        mCon = null;
        mInterface = null;
    }
    
    /**
     * Unbinds the PluginService, invalidating this connection. All further methods will fail.
     */
    @Override
    public void close() {
        unbind();
    }
    
    
    // overloads
    
    
    
    
    // original methods
    
    
    @Override
    public void setCallbackBinder(@NonNull IPluginCallback callback) throws RemoteException {
        if (mInterface == null) throw new NullPointerException("PluginServiceWrapper already unbound");
        mInterface.setCallbackBinder(callback);
    }
    
    @Override
    public void setCallbackService(@NonNull String componentName, int priority) throws android.os.RemoteException {
        if (mInterface == null) throw new NullPointerException("PluginServiceWrapper already unbound");
        mInterface.setCallbackService(componentName, priority);
    }
    
    @Override
    public Task runTask(String commandPath, String[] arguments, ParcelFileDescriptor stdin, String workdir, String[] environment) throws RemoteException {
        if (mInterface == null) throw new NullPointerException("PluginServiceWrapper already unbound");
        return mInterface.runTask(commandPath, arguments, stdin, workdir, environment);
    }
    
    @Override
    public boolean signalTask(int pid, int signal) throws RemoteException {
        if (mInterface == null) throw new NullPointerException("PluginServiceWrapper already unbound");
        return mInterface.signalTask(pid, signal);
    }
    
    
    @Override
    public void listenOnSocketFile(@NonNull String name) throws RemoteException {
        if (mInterface == null) throw new NullPointerException("PluginServiceWrapper already unbound");
        mInterface.listenOnSocketFile(name);
    }
    
    
    @Override
    public ParcelFileDescriptor openFile(@NonNull String name, @NonNull String mode) throws RemoteException {
        if (mInterface == null) throw new NullPointerException("PluginServiceWrapper already unbound");
        return mInterface.openFile(name, mode);
    }
    
    @Override
    public IBinder asBinder() {
        if (mInterface == null) throw new NullPointerException("PluginServiceWrapper already unbound");
        return mInterface.asBinder();
    }
}
