package commands;

import CCUtils.Storage.ISQL;
import CCUtils.Storage.SQLite;
import Main.SimpleHomes;
import config.HomesAPI;
import config.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {

    private SimpleHomes plugin;

    ISQL isql;
    HomesAPI homesAPI;

    public SetHomeCommand(SimpleHomes plugin) {
        this.plugin = plugin;
        isql = new SQLite(this.plugin.getDataFolder(), "Homes");
        homesAPI = new HomesAPI(this.isql);
        plugin.getCommand("sethome").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String homeName = "home";
            if (args.length == 1 && sender.hasPermission("simplehomes.home")) {
                homeName = args[0].toLowerCase();
            }

            homesAPI.setHome(isql, player, homeName);
            player.sendMessage(LanguageManager.HOME_SET);
            return true;
        } else {
            sender.sendMessage(LanguageManager.PLAYER_COMMAND_ONLY);
        }
        return false;
    }
}