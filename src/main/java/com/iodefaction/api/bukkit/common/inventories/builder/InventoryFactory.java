package com.iodefaction.api.bukkit.common.inventories.builder;

import com.iodefaction.api.bukkit.common.inventories.InventoryManager;
import com.iodefaction.api.bukkit.common.inventories.SmartInventory;
import com.iodefaction.api.bukkit.common.inventories.content.InventoryProvider;
import com.iodefaction.api.bukkit.common.plugins.Plugin;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class InventoryFactory {
    @Getter
    private final InventoryManager inventoryManager;

    public InventoryFactory(Plugin plugin) {
        this(new InventoryManager(plugin));
    }

    public SmartInventory.Builder createInventory(InventoryProvider inventoryProvider) {
        return SmartInventory.builder().manager(this.getInventoryManager()).provider(inventoryProvider);
    }
}
