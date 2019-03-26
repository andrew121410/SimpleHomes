package events;

import CCUtils.Storage.ISQL;
import CCUtils.Storage.SQLite;
import Main.SimpleHomes;
import Translate.Translate;
import config.HomesAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GatewayListener implements Listener {

    private ISQL isql;
    private HomesAPI homesAPI;

    private SimpleHomes simpleHomes;

    public GatewayListener(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
        this.isql = new SQLite(this.simpleHomes.getDataFolder(), "Homes");
        this.homesAPI = new HomesAPI(this.isql);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        homesAPI.getAllHomesFromISQL(isql, p);

        p.sendMessage(Translate.chat("&a<Simple&9Homes>&c&l To get your old home data use the command &9/home @olddata&c&l if you haven't already."));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        homesAPI.unloadPlayerHomes(event.getPlayer());
    }
}
