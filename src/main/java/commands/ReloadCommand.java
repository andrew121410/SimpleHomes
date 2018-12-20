package commands;

import SimpleHomes2.SimpleHomes2.SimpleHomes2.SimpleHomes;
import Translate.Translate;
import config.LanguageFileManager;
import config.LanguageManager;
import homes.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class ReloadCommand implements CommandExecutor {

    private SimpleHomes plugin;
    private SimpleHomes simpleHomes;
    private final HomeManager homeManager = null;

    public ReloadCommand(SimpleHomes plugin, HomeManager manager) {
        this.plugin = plugin;
        simpleHomes = plugin;
        manager = homeManager;
        plugin.getCommand("shreload").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("What are you? Only players can use this command.");
        }
        Player p = (Player) sender;

        if (!(sender.hasPermission("simplehomes2.reload"))) {
            sender.sendMessage(LanguageManager.DONT_HAVE_PERMISSION);
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(Translate.chat("Commands:"));
            p.sendMessage(Translate.chat("/shreload reloadconfig"));
            p.sendMessage(Translate.chat("/shreload deletelanguageconfig"));
            p.sendMessage(Translate.chat("/shreload unloadhomes"));
            p.sendMessage(Translate.chat("/shreload gethomes"));
            return true;
        } else if (args.length >= 1) {
            switch (args[0].toString()) {
                case "reloadconfig": {
                    simpleHomes.reloadConfig();
                    sender.sendMessage(Translate.chat("The config has been reloaded."));
                    break;
                }
                case "deletelanguageconfig": {
                    LanguageFileManager languageconfig = new LanguageFileManager(plugin);
                    File lanuagefile = languageconfig.getLanguageFile();
                    lanuagefile.delete();
                    sender.sendMessage("OK...");
                    break;
                }
                case "unloadhomes": {
                    this.homeManager.unloadPlayerHomes(p.getUniqueId());
                    p.sendMessage("OK...");
                    break;
                }

                case "gethomes": {
                    this.homeManager.loadPlayerHomes(p.getUniqueId());
                    p.sendMessage("OK...");
                    break;
                }

                default: {
                    break;
                }
            }
            return true;
        } else {
        }

        return true;
    }
}
