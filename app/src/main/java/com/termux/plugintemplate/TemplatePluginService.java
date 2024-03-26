package com.termux.plugintemplate;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
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
        return new NotificationCompat.Builder(c, "service").setPriority(NotificationCompat.PRIORITY_MIN).setContentTitle("TestPlugin").setSmallIcon(R.drawable.ic_launcher_foreground).build();
    }
    
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(100, getServiceNotification(this), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        } else {
            startForeground(100, getServiceNotification(this));
        }
        
        Log.d(LOG_TAG, "Plugin service started");
    
        PluginUtils.bindPluginService(this, (PluginServiceWrapper w) -> {
            if (w == null) {
                Log.d(LOG_TAG, "Could not bind to Termux PluginService");
                stopSelf();
                return;
            }
            Log.d(LOG_TAG, "Bound to Termux PluginService");
    
            try {
                w.setCallbackService(new ComponentName(TemplatePluginService.this, TemplateCallbackService.class).getShortClassName(), PluginServiceWrapper.PRIORITY_NORMAL);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        
    
        return Service.START_STICKY;
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}