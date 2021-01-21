package com.iodefaction.api.bukkit.utils;

import com.iodefaction.api.bukkit.common.modules.Module;
import com.iodefaction.api.bukkit.common.plugins.Plugin;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class BukkitModuleUtils {
    public static List<Module> getModules() {
        List<Module> modules = new ArrayList<>();

        for (org.bukkit.plugin.Plugin p : Bukkit.getServer().getPluginManager().getPlugins()) {
            if(p instanceof Plugin) {
                Plugin plugin = (Plugin) p;

                modules.addAll(plugin.getModules());
            }
        }

        return modules;
    }

    public static Module getModuleByName(String name) {
        return getModules().stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
