package com.termux.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.termux.plugin_aidl.IPluginCallback;
import com.termux.plugin_aidl.IPluginService;
import com.termux.plugin_shared.CallbackService;
import com.termux.plugin_shared.PluginServiceWrapper;
import com.termux.plugin_shared.PluginUtils;
import com.termux.plugin_shared.TermuxPluginConstants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


@RunWith(AndroidJUnit4.class)
public class PluginTestsWithPluginPermission
{
    @Rule public GrantPermissionRule pluginPermission = GrantPermissionRule.grant(TermuxPluginConstants.PERMISSION_TERMUX_PLUGIN);
    @Rule public GrantPermissionRule runCommandPermission = GrantPermissionRule.grant(TermuxPluginConstants.PERMISSION_RUN_COMMAND);
    
    @Test
    public void bindServiceTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    
        assert PluginUtils.bindPluginService(appContext) != null;
    }
    
    
    @Test
    public void openFileTest() throws RemoteException, IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    
        PluginServiceWrapper w = PluginUtils.bindPluginService(appContext);
        assert w != null;
        w.setCallbackBinder(new IPluginCallback.Stub()
        {
            @Override
            public int getCallbackVersion() {
                Log.d("IPluginCallback","getCallbackVersion");
                return IPluginCallback.CURRENT_CALLBACK_VERSION;
            }
        });
        ParcelFileDescriptor p = w.openFile("test.txt", "w");
        assert p != null;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(p.getFileDescriptor()))) {
            writer.write("Hello Plugin!\n");
        }
        p.close();
    }
    
    
    
    @After
    public void revokePermission() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("pm revoke "+InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName()+" "+TermuxPluginConstants.PERMISSION_TERMUX_PLUGIN);
    }
}