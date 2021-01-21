package com.iodefaction.api.bungee;

import com.iodefaction.api.common.tasks.MultiThreading;
import net.md_5.bungee.api.plugin.Plugin;

public class API extends Plugin {
    @Override
    public void onDisable() {
        MultiThreading.stopTask();
        super.onDisable();
    }
}
