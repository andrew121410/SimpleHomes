package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import SimpleHomes2.SimpleHomes2.SimpleHomes2.SimpleHomes;
import homes.HomeManager;

public class ReloadCommand implements CommandExecutor {

	private SimpleHomes plugin;
    private SimpleHomes simpleHomes;
    
	public ReloadCommand(SimpleHomes plugin, HomeManager manager){
		this.plugin = plugin;
		simpleHomes = plugin;
		plugin.getCommand("shreload").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        simpleHomes.reloadConfig();
        return true;
    }
}
