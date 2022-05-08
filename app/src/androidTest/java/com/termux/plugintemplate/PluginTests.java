package com.termux.plugintemplate;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.termux.plugin_shared.PluginUtils;
import com.termux.plugin_shared.TermuxPluginConstants;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class PluginTests
{
    @Test
    public void bindServiceWithoutPermissionTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        
        assert appContext.checkSelfPermission(TermuxPluginConstants.PERMISSION_TERMUX_PLUGIN) == PackageManager.PERMISSION_DENIED;
        
        assert PluginUtils.bindPluginService(appContext) == null;
    }
    
    
    
    
    
    
    
}