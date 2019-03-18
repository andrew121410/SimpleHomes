package Main;

import commands.DeleteHomeCommand;
import commands.HomeCommand;
import commands.HomeListCommand;
import commands.SetHomeCommand;
import events.GatewayListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SimpleHomes extends JavaPlugin {

    public static SimpleHomes instance;
    public static SimpleHomes plugin; //IK i have 2 of em.

    PluginManager pm;

    public void onEnable() {
        plugin = this;
        pm = plugin.getServer().getPluginManager();
        loadListeners();
        loadCommands();
        getLogger().info("SimpleHomes2 Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "SimpleHomes2 Disabled!");
    }

    private void loadCommands() {
        new DeleteHomeCommand(this);
        new HomeCommand(this);
        new HomeListCommand(this);
        new SetHomeCommand(this);
    }

    private void loadListeners() {
        Bukkit.getServer().getPluginManager()
                .registerEvents(new GatewayListener(this), this);
    }
}
