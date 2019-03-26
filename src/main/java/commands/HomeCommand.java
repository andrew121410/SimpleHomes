package commands;

import CCUtils.Storage.ISQL;
import CCUtils.Storage.SQLite;
import Main.SimpleHomes;
import Translate.Translate;
import config.HomesAPI;
import config.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeCommand implements CommandExecutor {

    public static Map<UUID, Map<String, Location>> rawHomesMap = new HashMap<>();

    private ISQL sqLite;
    private HomesAPI homesAPI;

    private SimpleHomes plugin;

    public HomeCommand(SimpleHomes plugin) {
        this.plugin = plugin;
        sqLite = new SQLite(this.plugin.getDataFolder(), "Homes");
        homesAPI = new HomesAPI(this.sqLite);

        plugin.getCommand("home").setExecutor(this);
        plugin.getCommand("home").setTabCompleter(new HomeListTab());
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

            if (homeName.equalsIgnoreCase("@olddata")) {
                homesAPI.getOLDSHIT(this.plugin, sqLite, player);
                player.sendMessage("Getting Old Data From Homes.YML");
                return true;
            }

            if(homeName.equalsIgnoreCase("@regetall")){
                Bukkit.getServer().getOnlinePlayers().forEach((player1) -> {
                    homesAPI.unloadPlayerHomes(player1);
                    homesAPI.getAllHomesFromISQL(sqLite, player1);
                    player1.sendMessage(Translate.chat("[&6SimpleHomes2&r] &cYour home data got wiped from memory BUT luckily it saved because Andrew's smart like that."));
                });
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