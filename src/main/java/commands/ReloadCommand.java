package commands;

import SimpleHomes2.SimpleHomes2.SimpleHomes2.SimpleHomes;
import Translate.Translate;
import config.LanguageFileManager;
import homes.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class ReloadCommand implements CommandExecutor {

    private SimpleHomes plugin;
    private SimpleHomes simpleHomes;

    public ReloadCommand(SimpleHomes plugin, HomeManager manager) {
        this.plugin = plugin;
        simpleHomes = plugin;
        plugin.getCommand("shreload").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            simpleHomes.reloadConfig();
            sender.sendMessage(Translate.chat("The config has been reloaded."));
            return true;
        } else if (args.length >= 1) {
            switch (args[0].toString()) {

                case "deleteconfig": {
                    LanguageFileManager languageconfig = new LanguageFileManager(plugin);
                    File lanuagefile = languageconfig.getLanguageFile();

                    lanuagefile.delete();
                    sender.sendMessage("OK...");
                }
                default: {
                }
            }
            return true;
        } else {
        }


        return true;
    }
}
