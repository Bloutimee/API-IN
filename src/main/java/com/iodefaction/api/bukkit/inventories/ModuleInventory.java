package com.iodefaction.api.bukkit.inventories;

import com.iodefaction.api.bukkit.API;
import com.iodefaction.api.bukkit.colors.Color;
import com.iodefaction.api.bukkit.common.inventories.ClickableItem;
import com.iodefaction.api.bukkit.common.inventories.SmartInventory;
import com.iodefaction.api.bukkit.common.inventories.content.InventoryContents;
import com.iodefaction.api.bukkit.common.inventories.content.InventoryProvider;
import com.iodefaction.api.bukkit.common.inventories.content.Pagination;
import com.iodefaction.api.bukkit.common.inventories.content.SlotIterator;
import com.iodefaction.api.bukkit.common.items.ItemBuilder;
import com.iodefaction.api.bukkit.utils.BukkitModuleUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ModuleInventory implements InventoryProvider {

    @Getter
    private final API api;

    public static SmartInventory getInventory(API api) {
        return api.getInventoryFactory()
                .createInventory(new ModuleInventory(api))
                .size(6, 9).title(Color.SECONDARY + "Modules").build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(5*9);

        List<ClickableItem> clickableItems = new ArrayList<>();

        BukkitModuleUtils.getModules().forEach(module -> {
            ClickableItem clickableItem = ClickableItem.of(new ItemBuilder(Material.INK_SACK, 1, (byte) (module.isEnabled() ? 10 : 1))
                    .setName(Color.SECONDARY + module.getName() + Color.DARK_COLOR + " ❘ " + (module.isEnabled() ? Color.SUCCESS + "activé" : Color.FAILED + "désactivé"))
                    .toItemStack(), event -> {
                if(module.isEnabled()) {
                    module.disable();
                } else {
                    module.enable();
                }
            });

            clickableItems.add(clickableItem);
        });

        pagination.setItems(clickableItems.toArray(new ClickableItem[0]));
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        contents.fillRow(5, ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 1).toItemStack()));

        contents.set(5, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§bPage précédente").toItemStack(), inventoryClickEvent -> {
            if(!pagination.isFirst()) {
                getInventory(api).open(player, pagination.getPage() - 1);
            }
        }));

        contents.set(5, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§bPage suivante").toItemStack(), inventoryClickEvent -> {
            if(!pagination.isLast()) {
                getInventory(api).open(player, pagination.getPage() + 1);
            }
        }));
    }
}
