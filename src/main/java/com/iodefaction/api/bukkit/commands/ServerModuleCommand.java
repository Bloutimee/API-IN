package com.iodefaction.api.bukkit.commands;

import com.iodefaction.api.bukkit.API;
import com.iodefaction.api.bukkit.colors.Color;
import com.iodefaction.api.bukkit.common.commands.Args;
import com.iodefaction.api.bukkit.common.commands.annontations.Command;
import com.iodefaction.api.bukkit.common.modules.Module;
import com.iodefaction.api.bukkit.inventories.ModuleInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.iodefaction.api.bukkit.utils.BukkitModuleUtils.getModuleByName;

@AllArgsConstructor
public class ServerModuleCommand {
    @Getter
    private final API api;

    @Command(name = {"servermodule", "servermodule.help", "servermodules", "servermodules.help"},
            permissionNode = "module.admin",
            isConsole = true)
    public void onModuleHelp(Args args) {
        ModuleInventory.getInventory(api).open(args.getPlayer());
    }

    @Command(name = {"servermodule.toggle", "servermodules.toggle"},
            permissionNode = "module.admin",
            isConsole = true)
    public void onModuleToggle(Args args) {
        if(args.length() != 1) {
            args.getPlayer().sendMessage(Color.getPrefix("Avertissement", Color.PRIMARY) + "La commande s'utilise comme ceci: /" + args.getLabel() + " toggle <nom>");
            return;
        }

        String moduleName = args.getArgs(0);

        Module module = getModuleByName(moduleName);

        if(module == null) {
            args.getPlayer().sendMessage(Color.getPrefix("Erreur", Color.FAILED) + "Ce module n'existe pas !");
            return;
        }

        if(module.isEnabled()) {
            module.disable();
            args.getPlayer().sendMessage(Color.getPrefix("Succès", Color.SUCCESS) + "Le module est désactivé !");
        } else {
            module.enable();
            args.getPlayer().sendMessage(Color.getPrefix("Succès", Color.SUCCESS) + "Le module est activé !");
        }
    }
}
