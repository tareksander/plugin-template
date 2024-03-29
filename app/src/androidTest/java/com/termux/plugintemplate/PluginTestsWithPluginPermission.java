package com.termux.plugintemplate;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.termux.plugin_aidl.IPluginCallback;
import com.termux.plugin_shared.PluginServiceWrapper;
import com.termux.plugin_shared.PluginUtils;
import com.termux.plugin_shared.TermuxPluginConstants;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;


@RunWith(AndroidJUnit4.class)
public class PluginTestsWithPluginPermission
{
    @Rule
    public GrantPermissionRule pluginPermission = GrantPermissionRule.grant(TermuxPluginConstants.PERMISSION_TERMUX_PLUGIN);
    
    
    @Test
    public void bindServiceTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PluginServiceWrapper w = PluginUtils.bindPluginService(appContext);
        if (w != null) {
            w.close();
        }
        assert PluginUtils.bindPluginService(appContext) != null; // binding the service should be possible with the Plugin permission
    }
    
    
    @Test public void runCommandTest() throws RemoteException, IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        
        assert appContext.checkSelfPermission(TermuxPluginConstants.PERMISSION_RUN_COMMAND) == PackageManager.PERMISSION_DENIED; // check that the RUN_COMMAND permission hasn't been granted
        
        try (PluginServiceWrapper w = PluginUtils.bindPluginService(appContext)) {
            assert w != null; // binding the service should be possible with the Plugin permission
            w.setCallbackBinder(new IPluginCallback.Stub()
            {
                @Override
                public int getCallbackVersion() {
                    Log.d("IPluginCallback", "getCallbackVersion");
                    return IPluginCallback.CURRENT_CALLBACK_VERSION;
                }
                
                @Override
                public void socketConnection(String sockname, ParcelFileDescriptor connection) {
                }
                
                @Override
                public void taskFinished(int pid, int code) {
                }
            });
            
            ParcelFileDescriptor[] in = ParcelFileDescriptor.createPipe();
            try {
                w.runTask("", null, in[0], "/", null);
                assert false; // runTask should always throw a SecurityException without the RUN_COMMAND permission
            }
            catch (SecurityException ignored) {
            }
            finally {
                in[0].close();
                in[1].close();
            }
        }
        
    }
    
    
    
    @After
    public void revokePermission() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("pm revoke "+InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName()+" "+TermuxPluginConstants.PERMISSION_TERMUX_PLUGIN);
    }
    
}