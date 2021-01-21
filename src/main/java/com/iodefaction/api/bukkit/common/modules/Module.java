package com.iodefaction.api.bukkit.common.modules;

import com.iodefaction.api.bukkit.common.plugins.Plugin;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public abstract class Module {
    @NonNull
    @Getter @Setter
    private String name;

    @Getter @Setter
    private boolean enabled = true;

    @NonNull
    @Getter @Setter
    private Plugin plugin;

    @Getter
    private final List<Listener> listeners = new ArrayList<>();

    @Getter
    private final List<Object> commands = new ArrayList<>();

    public abstract void onModuleEnable();
    public abstract void onModuleDisable();

    public void enable() {
        onModuleEnable();
        this.registerCommands();
        this.setEnabled(true);
    }

    public void disable() {
        onModuleDisable();
        this.removeCommands();
        this.getListeners().forEach(HandlerList::unregisterAll);
        this.setEnabled(false);
    }

    private void removeCommands() {
        this.getCommands().forEach(o -> {
            Collection<String> names = plugin.getCommandFramework().fetchNames(o);

            names.forEach(this.getPlugin().getCommandFramework()::removeCommand);
        });
    }

    private void registerCommands() {
        this.getCommands().forEach(this.getPlugin().getCommandFramework()::registerCommands);
    }

    public void reload() {
        disable();
        enable();
    }

    public void registerCommand(Object o) {
        commands.add(o);
    }

    public void registerListener(Listener listener) {
        this.listeners.add(listener);

        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
