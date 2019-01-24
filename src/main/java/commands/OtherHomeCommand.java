package commands;

import Main.SimpleHomes;
import Translate.Translate;
import Utils.UUIDManager;
import config.LanguageManager;
import homes.HomeManager;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class OtherHomeCommand implements CommandExecutor {

    private SimpleHomes plugin;
    private final SimpleHomes simpleHomes;
    private final HomeManager homeManager;

    public OtherHomeCommand(SimpleHomes plugin, HomeManager manager) {
        this.plugin = plugin;
        simpleHomes = plugin;
        homeManager = manager;
        plugin.getCommand("otherhome").setExecutor(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return false;
        }
        Player p = (Player) sender;
        if (args.length <= 1) {
            p.sendMessage(
                Translate.chat("[&2SimpleHomes2&r] &cUsage: /otherhome <Player> <Homename>"));
            return true;
        } else if (args.length >= 2) {
            String homename;
            String targetname;
            targetname = args[0].toLowerCase();
            homename = args[1].toLowerCase();

            new BukkitRunnable() {
                UUID targetUUID;

                @Override
                public void run() {
                    targetUUID = UUIDManager.getUUIDFromPlayer(targetname);

                    if (targetUUID != null) {
                        Location location = homeManager.getPlayerHome(targetUUID, homename);

                        if (location == null) {
                            location = homeManager.getPlayerHomeFromFile(targetUUID, homename);
                        }

                        if (location != null) {
                            p.teleport(location);
                            p.sendMessage(
                                LanguageManager.TELEPORT_OTHERHOME.replaceAll("%p", targetname));
                        } else {
                            p.sendMessage(LanguageManager.HOME_NOT_FOUND);
                        }
                    }
                }
            }.runTaskAsynchronously(this.plugin);
        }
        return true;
    }
}