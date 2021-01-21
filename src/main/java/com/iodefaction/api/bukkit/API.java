package com.iodefaction.api.bukkit;

import com.iodefaction.api.bukkit.commands.ServerModuleCommand;
import com.iodefaction.api.bukkit.common.plugins.Plugin;
import com.iodefaction.api.common.tasks.MultiThreading;
import lombok.Getter;

public class API extends Plugin {

    @Getter
    private static API instance;

    @Override
    public void onPluginEnable() {
        instance = this;

        this.getCommandFramework().registerCommands(new ServerModuleCommand(this));

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onPluginDisable() {
        MultiThreading.stopTask();
    }
}
