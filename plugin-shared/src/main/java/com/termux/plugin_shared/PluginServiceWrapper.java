package com.termux.plugin_shared;

import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.termux.plugin_aidl.IPluginCallback;
import com.termux.plugin_aidl.IPluginService;
import com.termux.plugin_aidl.Task;

/**
 * Provides overloaded methods so the AIDL doesn't have to include overloaded methods.
 */
public class PluginServiceWrapper implements IPluginService
{
    private final IPluginService mInterface;
    
    public PluginServiceWrapper(@NonNull IPluginService pluginService) {
        mInterface = pluginService;
    }
    
    
    // overloads
    
    
    
    
    // original methods
    
    
    @Override
    public void setCallbackBinder(@NonNull IPluginCallback callback) throws RemoteException {
        mInterface.setCallbackBinder(callback);
    }
    
    @Override
    public void setCallbackService(@NonNull String componentName) throws android.os.RemoteException {
        mInterface.setCallbackService(componentName);
    }
    
    @Override
    public Task runTask(String commandPath, String[] arguments, ParcelFileDescriptor stdin, String workdir, String[] environment) throws RemoteException {
        return mInterface.runTask(commandPath, arguments, stdin, workdir, environment);
    }
    
    @Override
    public boolean signalTask(int pid, int signal) throws RemoteException {
        return mInterface.signalTask(pid, signal);
    }
    
    
    @Override
    public void listenOnSocketFile(@NonNull String name) throws RemoteException {
        mInterface.listenOnSocketFile(name);
    }
    
    
    @Override
    public ParcelFileDescriptor openFile(@NonNull String name, @NonNull String mode) throws RemoteException {
        return mInterface.openFile(name, mode);
    }
    
    @Override
    public IBinder asBinder() {
        return mInterface.asBinder();
    }
}
