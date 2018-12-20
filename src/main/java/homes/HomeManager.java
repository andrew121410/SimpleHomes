/*
 * Copyright (c) 2014, LankyLord
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package homes;

import config.ConfigManager;
import config.HomeFileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeManager {

    private final HomeFileManager fileManager;
    private final Map<UUID, Map<String, Location>> loadedHomes;

    public HomeManager(HomeFileManager fileManager) {
        this.fileManager = fileManager;
        this.loadedHomes = new HashMap<>();
    }

    /**
     * Check whether a player has reached their maximum amount of homes
     *
     * @param uuid UUID of the player
     * @return Whether a player has reached the maximum amount of homes
     */
    public boolean reachedMaxHomes(UUID uuid) {
        return getHomesSize(uuid) == ConfigManager.getMaxHomes();
    }

    /**
     * Get the amount of homes of a player
     *
     * @param uuid UUID of the player
     * @return Amount of homes
     */
    private int getHomesSize(UUID uuid) {
        if (fileManager.getHomes().contains(uuid.toString())) {
            return this.fileManager.getHomes().getConfigurationSection(uuid.toString()).getKeys(false).size();
        }
        return 0;
    }

    /**
     * Saves homes too the file.
     *
     * @param uuid
     * @param location
     * @param homeName
     */
    private void saveHomeToFile(UUID uuid, Location location, String homeName) {
        ConfigurationSection home = this.fileManager.getHomes().getConfigurationSection(uuid.toString() + "." +
                homeName.toLowerCase());
        if (home == null) {
            home = this.fileManager.getHomes().createSection(uuid.toString() + "." + homeName.toLowerCase());
        }
        float yaw = location.getYaw();
        float pitch = location.getPitch();

        home.set("world", location.getWorld().getName());
        home.set("x", location.getBlockX());
        home.set("y", location.getBlockY());
        home.set("z", location.getBlockZ());
        home.set("yaw", Float.valueOf(yaw));
        home.set("pitch", Float.valueOf(pitch));

        this.fileManager.saveHomes();
    }

    /**
     * Save a player's home
     *
     * @param player   The player
     * @param homeName Name of the home
     */
    @SuppressWarnings("unchecked")
    public void saveHome(Player player, String homeName) {
        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();

        Map<String, Location> homeLocation = this.loadedHomes.get(uuid);
        if (homeLocation == null) {
            homeLocation = new HashMap<>();
        }
        homeLocation.put(homeName.toLowerCase(), location);
        this.loadedHomes.put(uuid, homeLocation);

        saveHomeToFile(uuid, location, homeName);
    }

    /**
     * Delete homes from HashMap and homes.yml
     *
     * @param uuid
     * @param homeName
     */
    @SuppressWarnings("unchecked")
    public void deleteHome(UUID uuid, String homeName) {
        Map homeLocations = loadedHomes.get(uuid);
        if (homeLocations != null) {
            homeLocations.remove(homeName.toLowerCase());
            this.loadedHomes.put(uuid, homeLocations);
        }
        ConfigurationSection home = this.fileManager.getHomes().getConfigurationSection(uuid.toString());
        home.set(homeName.toLowerCase(), null);
        this.fileManager.saveHomes();
    }

    /**
     * Load a player's homes from file uses in GatewayListener.
     *
     * @param uuid UUID of the player
     */
    public void loadPlayerHomes(UUID uuid) {
        ConfigurationSection homes = this.fileManager.getHomes().getConfigurationSection(uuid.toString());
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
                    homeLocation.put(homeName.toLowerCase(), new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch));
                } else {
                    System.out.println("Error in/or/an home, not loaded.");
                }
            }
            this.loadedHomes.put(uuid, homeLocation);
        } else {
            this.loadedHomes.put(uuid, new HashMap<>());
        }
    }

    /**
     * Remove a player's homes from memory
     *
     * @param uuid UUID of the player
     */
    public void unloadPlayerHomes(UUID uuid) {
        this.loadedHomes.remove(uuid);
    }

    /**
     * Get a player home from memory
     *
     * @param uuid     UUID of the player
     * @param homeName Name of the home
     * @return Location of home
     */
    @SuppressWarnings("unchecked")
    public Location getPlayerHome(UUID uuid, String homeName) {
        Map<String, Location> homeLocations = this.loadedHomes.get(uuid);
        if (homeLocations != null) {
            return homeLocations.get(homeName.toLowerCase());
        } else {
            return null;
        }
    }

    /**
     * Gets player's home from memory
     *
     * @param uuid
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Location> getPlayerHomes(UUID uuid) {
        if (this.loadedHomes.containsKey(uuid)) {
            return this.loadedHomes.get(uuid);
        } else {
            loadPlayerHomes(uuid);
            HashMap playerHomes = (HashMap) this.loadedHomes.get(uuid);
            unloadPlayerHomes(uuid);
            return playerHomes;
        }
    }

    /**
     * Gets Player home from home file.
     *
     * @param uuid
     * @param homeName
     * @return
     */
    public Location getPlayerHomeFromFile(UUID uuid, String homeName) { //GET PLAYER FROM HOME
        ConfigurationSection homes = this.fileManager.getHomes().getConfigurationSection(uuid.toString());
        Map<String, Location> homeLocation = new HashMap<>();
        if (homes != null) {
            for (String home : homes.getKeys(false)) {
                ConfigurationSection homeSection = homes.getConfigurationSection(home);

                String world = homeSection.getString("world");
                int x = homeSection.getInt("x");
                int y = homeSection.getInt("y");
                int z = homeSection.getInt("z");
                if (homeSection.getString("yaw") == null) {
                    homeSection.set("yaw", 0);
                }
                if (homeSection.getString("pitch") == null) {
                    homeSection.set("pitch", 0);
                }
                float yaw = homeSection.getInt("yaw");
                float pitch = homeSection.getInt("pitch");

                homeLocation.put(home.toLowerCase(), new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch));
            }
        }
        return homeLocation.get(homeName.toLowerCase());
    }

    /**
     * Get loadedHomes
     *
     * @return
     */
    public Map<UUID, Map<String, Location>> getHomes() {
        return this.loadedHomes;
    }
}