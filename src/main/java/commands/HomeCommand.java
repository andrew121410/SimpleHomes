package commands;

import CCUtils.Storage.ISQL;
import CCUtils.Storage.SQLite;
import Main.SimpleHomes;
import config.HomesAPI;
import config.LanguageManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class HomeCommand implements CommandExecutor {

    public static Map<UUID, Map<String, Location>> rawHomesMap;

    ISQL sqLite;
    HomesAPI homesAPI;

    private SimpleHomes plugin;

    public HomeCommand(SimpleHomes plugin) {
        this.plugin = plugin;

        sqLite = new SQLite(this.plugin.getDataFolder(), "Homes");
        homesAPI = new HomesAPI(this.sqLite);

        plugin.getCommand("home").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
        }
        Player player = (Player) sender;
        String homeName = "home";

        if (args.length == 1 && sender.hasPermission("simplehomes.home")) {
            homeName = args[0].toLowerCase();
            if (homeName.equalsIgnoreCase("olddata")) {
                homesAPI.getOLDSHIT(this.plugin, sqLite, player);
                player.sendMessage("Getting Old Data From Homes.YML");
                return true;
            }
        }
        Location home = homesAPI.getHomeFromMap(player, homeName);

        if (home != null) {
            player.teleport(home);
            player.sendMessage(LanguageManager.TELEPORT_SUCCESS);
        } else {
            player.sendMessage(LanguageManager.HOME_NOT_FOUND);
            return true;
        }
        return true;
    }
}