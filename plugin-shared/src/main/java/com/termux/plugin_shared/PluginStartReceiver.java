package com.termux.plugin_shared;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


/**
 * This is a template receiver to allow programs inside Termux to start the plugin with a broadcast.
 * Override {@link #getServiceClass()} and provide the class of the service for the plugin.
 * The plugin needs a service, so it isn't destroyed by Android, so every plugin should have one.
 * The service has to be a foreground service on {@link Build.VERSION_CODES#O} and higher.
 */
public abstract class PluginStartReceiver extends BroadcastReceiver
{
    
    /**
     * @return Return the service you want to start when receiving the broadcast.
     */
    protected abstract Class<?> getServiceClass();
    
    @Override
    public void onReceive(Context c, Intent ignored) {
        Intent service = new Intent(c, getServiceClass());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            c.startForegroundService(service);
        } else {
            c.startService(service);
        }
    }
}
