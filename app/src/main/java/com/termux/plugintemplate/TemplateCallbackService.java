package com.termux.plugintemplate;

import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.NonNull;

import com.termux.plugin_shared.CallbackService;

public class TemplateCallbackService extends CallbackService
{
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TemplateCallbackService", "callback service bound");
    }
    
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