package com.iodefaction.api.bukkit.common.plugins;

import com.iodefaction.api.bukkit.common.commands.CommandFramework;
import com.iodefaction.api.bukkit.common.inventories.builder.InventoryFactory;
import com.iodefaction.api.bukkit.common.modules.Module;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class Plugin extends JavaPlugin {

    @Getter @Setter
    private CommandFramework commandFramework;

    @Getter
    private InventoryFactory inventoryFactory;

    @Getter
    private List<Module> modules;

    public abstract void onPluginEnable();
    public abstract void onPluginDisable();

    @Override
    public void onEnable() {
        this.modules = new ArrayList<>();

        commandFramework = new CommandFramework(this);
        inventoryFactory = new InventoryFactory(this);

        onPluginEnable();
        modules.forEach(Module::enable);
    }

    @Override
    public void onDisable() {
        onPluginDisable();
        modules.forEach(Module::disable);
    }

    public void registerModule(Module module) {
        this.modules.add(module);
    }
}
