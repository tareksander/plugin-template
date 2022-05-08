package com.termux.plugintemplate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.termux.plugin_shared.CallbackService;

public class TemplateCallbackService extends CallbackService
{
    
    @NonNull
    @Override
    protected Callbacks getCallbacks() {
        return new Callbacks()
        {
            
        };
    }
}