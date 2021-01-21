package com.iodefaction.api.bukkit.common.inventories;

import com.iodefaction.api.bukkit.API;
import com.iodefaction.api.bukkit.common.inventories.content.InventoryContents;
import com.iodefaction.api.bukkit.common.inventories.opener.ChestInventoryOpener;
import com.iodefaction.api.bukkit.common.inventories.opener.InventoryOpener;
import com.iodefaction.api.bukkit.common.inventories.opener.SpecialInventoryOpener;
import com.iodefaction.api.bukkit.common.plugins.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryManager {

    private final JavaPlugin plugin;
    private final PluginManager pluginManager;

    private final Map<Player, SmartInventory> inventories;
    private final Map<Player, InventoryContents> contents;

    private final List<InventoryOpener> defaultOpeners;
    private final List<InventoryOpener> openers;

    public InventoryManager(Plugin plugin) {
        this.plugin = plugin;
        this.pluginManager = Bukkit.getPluginManager();

        this.inventories = new ConcurrentHashMap<>();
        this.contents = new ConcurrentHashMap<>();

        this.defaultOpeners = Arrays.asList(
                new ChestInventoryOpener(),
                new SpecialInventoryOpener()
        );

        this.openers = new ArrayList<>();

        init();
    }

    public InventoryManager() {
        this(API.getInstance());
    }

    public void init() {
        pluginManager.registerEvents(new InvListener(), plugin);

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () ->
                inventories.forEach((player, inv) -> inv.getProvider().update(player, contents.get(player))), 5, 5);
    }

    public Optional<InventoryOpener> findOpener(InventoryType type) {
        Optional<InventoryOpener> opInv = this.openers.stream()
                .filter(opener -> opener.supports(type))
                .findAny();

        if(!opInv.isPresent()) {
            opInv = this.defaultOpeners.stream()
                    .filter(opener -> opener.supports(type))
                    .findAny();
        }

        return opInv;
    }

    public void registerOpeners(InventoryOpener... openers) {
        this.openers.addAll(Arrays.asList(openers));
    }

    public List<Player> getOpenedPlayers(SmartInventory inv) {
        List<Player> list = new ArrayList<>();

        this.inventories.forEach((player, playerInv) -> {
            if(inv.equals(playerInv))
                list.add(player);
        });

        return list;
    }

    public Optional<SmartInventory> getInventory(Player p) {
        return Optional.ofNullable(this.inventories.get(p));
    }

    protected void setInventory(Player p, SmartInventory inv) {
        if(inv == null)
            this.inventories.remove(p);
        else
            this.inventories.put(p, inv);
    }

    public Optional<InventoryContents> getContents(Player p) {
        return Optional.ofNullable(this.contents.get(p));
    }

    protected void setContents(Player p, InventoryContents contents) {
        if(contents == null)
            this.contents.remove(p);
        else
            this.contents.put(p, contents);
    }

    @SuppressWarnings("unchecked")
    class InvListener implements Listener {

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryClick(InventoryClickEvent e) {
            Player p = (Player) e.getWhoClicked();

            if(!inventories.containsKey(p))
                return;

            if(e.getAction() == InventoryAction.COLLECT_TO_CURSOR ||
                    e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
                    e.getAction() == InventoryAction.NOTHING) {

                e.setCancelled(true);
                return;
            }

            if(e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {
                e.setCancelled(true);

                int row = e.getSlot() / 9;
                int column = e.getSlot() % 9;

                if(row < 0 || column < 0)
                    return;

                SmartInventory inv = inventories.get(p);

                if(row >= inv.getRows() || column >= inv.getColumns())
                    return;

                inv.getListeners().stream()
                        .filter(listener -> listener.getType() == InventoryClickEvent.class)
                        .forEach(listener -> ((InventoryListener<InventoryClickEvent>) listener).accept(e));

                contents.get(p).get(row, column).ifPresent(item -> item.run(e));

                p.updateInventory();
            }
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryDrag(InventoryDragEvent e) {
            Player p = (Player) e.getWhoClicked();

            if(!inventories.containsKey(p))
                return;

            SmartInventory inv = inventories.get(p);

            for(int slot : e.getRawSlots()) {
                if(slot >= p.getOpenInventory().getTopInventory().getSize())
                    continue;

                e.setCancelled(true);
                break;
            }

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryDragEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryDragEvent>) listener).accept(e));
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryOpen(InventoryOpenEvent e) {
            Player p = (Player) e.getPlayer();

            if(!inventories.containsKey(p))
                return;

            SmartInventory inv = inventories.get(p);

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryOpenEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryOpenEvent>) listener).accept(e));
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryClose(InventoryCloseEvent e) {
            Player p = (Player) e.getPlayer();

            if(!inventories.containsKey(p))
                return;

            SmartInventory inv = inventories.get(p);

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener).accept(e));

            if(inv.isCloseable()) {
                e.getInventory().clear();

                inventories.remove(p);
                contents.remove(p);
            }
            else
                Bukkit.getScheduler().runTask(plugin, () -> p.openInventory(e.getInventory()));
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerQuit(PlayerQuitEvent e) {
            Player p = e.getPlayer();

            if(!inventories.containsKey(p))
                return;

            SmartInventory inv = inventories.get(p);

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == PlayerQuitEvent.class)
                    .forEach(listener -> ((InventoryListener<PlayerQuitEvent>) listener).accept(e));

            inventories.remove(p);
            contents.remove(p);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPluginDisable(PluginDisableEvent e) {
            new HashMap<>(inventories).forEach((player, inv) -> {
                inv.getListeners().stream()
                        .filter(listener -> listener.getType() == PluginDisableEvent.class)
                        .forEach(listener -> ((InventoryListener<PluginDisableEvent>) listener).accept(e));

                inv.close(player);
            });

            inventories.clear();
            contents.clear();
        }

    }
}
