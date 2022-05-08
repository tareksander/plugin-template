package com.termux.plugintemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.termux.plugin_shared.TermuxPluginConstants;

public class MainActivity extends AppCompatActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        requestPermissions(new String[] {TermuxPluginConstants.PERMISSION_TERMUX_PLUGIN}, 0);
        
    }
    
    
    
}