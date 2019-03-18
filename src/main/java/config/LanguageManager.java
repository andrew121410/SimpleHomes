package config;

import org.bukkit.ChatColor;

public class LanguageManager {

    public static String HOME_DELETED = ChatColor.YELLOW + "Home deleted.";
    public static String HOME_LIST_PREFIX = ChatColor.GOLD + "Homes:" + ChatColor.RESET + ChatColor.GRAY;
    public static String HOME_NOT_FOUND = ChatColor.RED + "Home not found.";
    public static String HOME_MAX_REACHED = ChatColor.RED + "Home cannot be set. The maximum number of homes has been " +
            "reached";
    public static String HOME_SET = ChatColor.YELLOW + "Home set.";
    public static String NO_HOMES_FOUND = ChatColor.RED + "No homes found.";
    public static String PLAYER_COMMAND_ONLY = ChatColor.RED + "Only players may issue that command.";
    public static String TELEPORT_OTHERHOME = ChatColor.YELLOW + "Teleported to %p's home.";
    public static String TELEPORT_SUCCESS = ChatColor.YELLOW + "Teleported.";
    public static String PLAYER_NOT_EXIST = ChatColor.RED + "That player doesn't exist.";
    public static String DONT_HAVE_PERMISSION = ChatColor.RED + "You don't have permission";

    public LanguageManager() {
    }
}