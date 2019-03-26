package commands;

import Main.SimpleHomes;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class HomeListTab implements TabCompleter {

    SimpleHomes main = SimpleHomes.plugin;

    //Lists
    Map<UUID, Map<String, Location>> rawHomesMap = HomeCommand.rawHomesMap;
    //...

    public HomeListTab() {

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("home") || cmd.getName().equalsIgnoreCase("delhome")) {
            Set<String> homeSet = rawHomesMap.get(player.getUniqueId()).keySet();
            String[] homeString = homeSet.toArray(new String[0]);
            List<String> list = getContains(args[0], Arrays.asList(homeString));
            return list;
        }
        return null;
    }

    private List<String> getContains(String args, List<String> a) {
        List<String> list = new ArrayList<>();
        for (String mat : a) {
            if (mat.contains(args.toLowerCase())) {
                list.add(mat);
            }
        }
        return list;
    }
}
