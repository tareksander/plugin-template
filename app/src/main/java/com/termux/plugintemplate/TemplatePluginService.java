package com.termux.plugintemplate;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.termux.plugin_shared.PluginServiceWrapper;
import com.termux.plugin_shared.PluginUtils;

public class TemplatePluginService extends Service
{
    private final static String LOG_TAG = "TemplatePluginService";
    
    static Notification getServiceNotification(Context c) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(c);
        nm.createNotificationChannel(new NotificationChannelCompat.Builder("service", NotificationManagerCompat.IMPORTANCE_MIN).setName("Service").build());
        return new NotificationCompat.Builder(c, "service").setPriority(NotificationCompat.PRIORITY_MIN).setContentTitle("TestPlugin").build();
    }
    
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        
        startForeground(100, getServiceNotification(this));
        
        
        Log.d(LOG_TAG, "Plugin service started");
    
        PluginServiceWrapper w = PluginUtils.bindPluginService(this);
        if (w == null) {
            Log.d(LOG_TAG, "Could not bind to Termux PluginService");
            stopSelf();
            return Service.START_NOT_STICKY;
        }
    
        try {
            w.setCallbackService(new ComponentName(this, TemplateCallbackService.class).getShortClassName());
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    
        return Service.START_STICKY;
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}