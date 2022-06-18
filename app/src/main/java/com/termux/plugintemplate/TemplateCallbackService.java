package com.termux.plugintemplate;

import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.termux.plugin_shared.CallbackService;

public class TemplateCallbackService extends CallbackService
{
    
    @NonNull
    @Override
    protected Callbacks getCallbacks() {
        return new Callbacks() {
    
            @Override
            public void socketConnection(String sockname, ParcelFileDescriptor connection) {
        
            }
            @Override
            public void taskFinished(int pid, int code) {}
        };
    }
}