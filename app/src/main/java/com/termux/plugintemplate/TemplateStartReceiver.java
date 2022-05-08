package com.termux.plugintemplate;

import com.termux.plugin_shared.PluginStartReceiver;

public class TemplateStartReceiver extends PluginStartReceiver
{
    @Override
    protected Class<?> getServiceClass() {
        return TemplatePluginService.class;
    }
}
