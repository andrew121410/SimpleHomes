package config;

import CCUtils.Storage.ISQL;
import Main.SimpleHomes;
import Translate.Translate;
import commands.HomeCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class HomesAPI {

    Map<UUID, Map<String, Location>> rawHomesMap = HomeCommand.rawHomesMap;

    private ISQL isql;

    public HomesAPI() {

        if (rawHomesMap == null) {
            rawHomesMap = new HashMap<>();
        }
    }

    public HomesAPI(ISQL isql) {
        this.isql = isql;

        isql.Connect();
        isql.ExecuteCommand("CREATE TABLE IF NOT EXISTS `Homes` (" +
                "`UUID` TEXT," +
                "`Date` TEXT," +
                "`PlayerName` TEXT," +
                "`HomeName` TEXT," +
                "`X` TEXT," +
                "`Y` TEXT," +
                "`Z` TEXT," +
                "`YAW` TEXT," +
                "`PITCH` TEXT," +
                "`World` TEXT" +
                ");");
        isql.Disconnect();

        if (rawHomesMap == null) {
            rawHomesMap = new HashMap<>();
        }
    }

    public void getAllHomesFromISQL(ISQL isql, Player player) {
        this.fixMaps(player.getUniqueId());

        isql.Connect();

        ResultSet rs = isql.GetResult("SELECT * FROM Homes WHERE (UUID='" + player.getUniqueId().toString() + "');");
        try {
            while (rs.next()) {
                String UUID = rs.getString("UUID");
                String Date = rs.getString("Date");
                String PlayerName = rs.getString("PlayerName");
                String HomeName = rs.getString("HomeName");
                String X = rs.getString("X");
                String Y = rs.getString("Y");
                String Z = rs.getString("Z");
                String YAW = rs.getString("YAW");
                String PITCH = rs.getString("PITCH");
                String World = rs.getString("World");

                rawHomesMap.get(player.getUniqueId()).put(HomeName, new Location(Bukkit.getServer().getWorld(World), Double.parseDouble(X), Double.parseDouble(Y), Double.parseDouble(Z), Float.parseFloat(YAW), Float.parseFloat(PITCH)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            isql.Disconnect();
        }
    }

    public Location getHomeFromMap(Player player, String HomeName) {
        this.fixMaps(player.getUniqueId());

        return rawHomesMap.get(player.getUniqueId()).get(HomeName);
    }

    public Location getHomeFromMap(UUID uuid, String HomeName) {
        this.fixMaps(uuid);

        return rawHomesMap.get(uuid).get(HomeName);
    }

    public void removeHome(ISQL isql, Player player, String HomeName) {
        this.fixMaps(player.getUniqueId());

        rawHomesMap.get(player.getUniqueId()).remove(HomeName.toLowerCase());

        removeHomeForISQL(isql, player, HomeName);
    }

    public void removeHomeForISQL(ISQL isql, Player player, String HomeName) {
        isql.Connect();
        isql.ExecuteCommand("DELETE FROM Homes WHERE UUID='" + player.getUniqueId() + "' AND HomeName='" + HomeName.toLowerCase() + "'");
        isql.Disconnect();
    }

    public void removeAllHomesFromISQL(ISQL isql, Player player) {
        isql.Connect();
        isql.ExecuteCommand("DELETE FROM Homes WHERE UUID='" + player.getUniqueId() + "'");
        isql.Disconnect();
        rawHomesMap.remove(player.getUniqueId());
        player.kickPlayer("All homes has been cleared! Rejoin the server!");
    }

    public String listHomesInMap(Player player) {
        this.fixMaps(player.getUniqueId());

        Set<String> homeSet = rawHomesMap.get(player.getUniqueId()).keySet();
        String[] homeString = homeSet.toArray(new String[0]);
        Arrays.sort(homeString);
        String str = String.join(", ", homeString);
        String newSTR = LanguageManager.HOME_LIST_PREFIX + " " + str;
        return newSTR;
    }

    public void setHome(ISQL isql, Player player, String HomeName) {
        this.fixMaps(player.getUniqueId());

        rawHomesMap.get(player.getUniqueId()).put(HomeName.toLowerCase(), player.getLocation());

        setHomeToISQL(isql, player.getUniqueId(), player.getDisplayName(), HomeName, player.getLocation());
    }

    public void setHome(ISQL isql, UUID uuid, String PlayerName, String HomeName, Location location) {
        this.fixMaps(uuid);

        rawHomesMap.get(uuid).put(HomeName.toLowerCase(), location);

        setHomeToISQL(isql, uuid, PlayerName, HomeName, location);
    }

    private void setHomeToISQL(ISQL isql, UUID uuid, String PlayerName, String HomeName, Location location) {
        isql.Connect();
        PreparedStatement preparedStatement = isql.ExecuteCommandPreparedStatement("INSERT INTO Homes (UUID,Date,PlayerName,HomeName,X,Y,Z,YAW,PITCH,World) VALUES (?,?,?,?,?,?,?,?,?,?);");
        try {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, "0");
            preparedStatement.setString(3, PlayerName);
            preparedStatement.setString(4, HomeName.toLowerCase());
            preparedStatement.setString(5, String.valueOf(location.getX()));
            preparedStatement.setString(6, String.valueOf(location.getY()));
            preparedStatement.setString(7, String.valueOf(location.getZ()));
            preparedStatement.setString(8, String.valueOf(location.getYaw()));
            preparedStatement.setString(9, String.valueOf(location.getPitch()));
            preparedStatement.setString(10, location.getWorld().getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            isql.Disconnect();
        }

    }

    public void unloadPlayerHomes(Player player) {
        rawHomesMap.remove(player.getUniqueId());
    }

    private HomeFileManager homeFileManager = null;

    public void getOLDSHIT(SimpleHomes instance, ISQL isql, Player player) {

        if (homeFileManager == null) {
            homeFileManager = new HomeFileManager(instance);
        }

        ConfigurationSection homes = homeFileManager.getHomes().getConfigurationSection(player.getUniqueId().toString());
        if (homes != null) {
            Map<String, Location> homeLocation = new HashMap<>();

            for (String homeName : homes.getKeys(false)) {
                ConfigurationSection home = homes.getConfigurationSection(homeName);

                String world = home.getString("world", null);
                int x = home.getInt("x", Integer.MIN_VALUE);
                int y = home.getInt("y", Integer.MIN_VALUE);
                int z = home.getInt("z", Integer.MIN_VALUE);
                if (home.getString("yaw") == null) {
                    home.set("yaw", 0);
                }
                if (home.getString("pitch") == null) {
                    home.set("pitch", 0);
                }
                float yaw = home.getInt("yaw", Integer.MIN_VALUE);
                float pitch = home.getInt("pitch", Integer.MIN_VALUE);


                if (!(world == null || x == Integer.MIN_VALUE || y == Integer.MIN_VALUE || z == Integer.MIN_VALUE || yaw == Integer.MIN_VALUE || pitch == Integer.MIN_VALUE)) {
                    this.setHome(isql, player.getUniqueId(), player.getDisplayName(), homeName, new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch));
                    player.sendMessage(Translate.chat("&6<&9Andrew's Data Converter &ev1.0&6> &aimported-> &c" + homeName));
                } else {
                    System.out.println("Error in/or/an home, not loaded.");
                }
            }
        } else {
            this.rawHomesMap.put(player.getUniqueId(), new HashMap<>());
        }
    }

    private void fixMaps(UUID uuid) {
        if (rawHomesMap == null) {
            rawHomesMap = new HashMap<>();
        }
        rawHomesMap.computeIfAbsent(uuid, k -> new HashMap<>());

        Player player = Bukkit.getServer().getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            return;
        }

        this.getAllHomesFromISQL(isql, player);
    }
}
