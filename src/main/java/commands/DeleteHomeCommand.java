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

public class DeleteHomeCommand implements CommandExecutor {

    private SimpleHomes plugin;

    ISQL isql;
    HomesAPI homesAPI;

    public DeleteHomeCommand(SimpleHomes plugin) {
        this.plugin = plugin;
        isql = new SQLite(this.plugin.getDataFolder(), "Homes");
        homesAPI = new HomesAPI(isql);
        plugin.getCommand("delhome").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String homeName = "home";
            if (strings.length == 1 && sender.hasPermission("simplehomes.home")) {
                homeName = strings[0].toLowerCase();
            }
            homesAPI.removeHome(isql, player, homeName);
            player.sendMessage(LanguageManager.HOME_DELETED);
            return true;
        }
        sender.sendMessage(LanguageManager.PLAYER_COMMAND_ONLY);
        return true;
    }
}
