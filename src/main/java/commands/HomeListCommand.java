package commands;

import CCUtils.Storage.ISQL;
import CCUtils.Storage.SQLite;
import Main.SimpleHomes;
import config.HomesAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeListCommand implements CommandExecutor {

    private SimpleHomes plugin;

    ISQL isql;
    HomesAPI homesAPI;

    public HomeListCommand(SimpleHomes plugin) {
        this.plugin = plugin;
        isql = new SQLite(this.plugin.getDataFolder(), "Homes");
        homesAPI = new HomesAPI(isql);
        plugin.getCommand("homelist").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if(args.length >= 0){
            player.sendMessage(homesAPI.listHomesInMap(player));
            return true;
        }
        return true;
    }
}