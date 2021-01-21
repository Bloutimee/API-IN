package com.iodefaction.api.bukkit.colors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;

public class Color {
    public static final ChatColor PRIMARY = ChatColor.AQUA;
    public static final ChatColor SECONDARY = ChatColor.DARK_GREEN;
    public static final ChatColor DARK_COLOR = ChatColor.DARK_GRAY;

    public static final ChatColor SUCCESS = ChatColor.GREEN;
    public static final ChatColor FAILED = ChatColor.RED;

    public static final String SEPARATOR = ChatColor.DARK_GRAY.toString() +
            ChatColor.STRIKETHROUGH.toString() +
            ChatColor.BOLD.toString() +
            StringUtils.repeat("-", 16);

    public static String getPrefix(String name, ChatColor chatColor) {
        return DARK_COLOR + "[" + chatColor + name.toUpperCase() + DARK_COLOR + "] " + chatColor;
    }
}
