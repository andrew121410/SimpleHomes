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
package commands;

import Main.SimpleHomes;
import Utils.UUIDManager;
import config.LanguageManager;
import homes.HomeManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeListCommand implements CommandExecutor {

    private SimpleHomes plugin;
    private final HomeManager homeManager;

    public HomeListCommand(SimpleHomes plugin, HomeManager manager) {
        this.plugin = plugin;
        homeManager = manager;
        plugin.getCommand("homelist").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Set<String> homeSet = new HashSet<>();
            // Returns a null if the user has no homes
            if (args.length != 0) {
                UUID uuid = UUIDManager.getUUIDFromPlayer(args[0]);
                if (uuid != null) {
                    homeSet = homeManager.getPlayerHomes(uuid).keySet();
                } else {
                    player.sendMessage(LanguageManager.PLAYER_NOT_EXIST);
                }
            } else {
                homeSet = homeManager.getPlayerHomes(player.getUniqueId()).keySet();
            }
            String[] homeString = homeSet.toArray(new String[homeSet.size()]);
            Arrays.sort(homeString);

            String homes = homeListString(homeString);
            if (homes != null) {
                sender.sendMessage(LanguageManager.HOME_LIST_PREFIX + " " + homes);
            } else {
                sender.sendMessage(LanguageManager.NO_HOMES_FOUND);
            }
            return true;
        } else {
            //sender.sendMessage(LanguageManager.PLAYER_COMMAND_ONLY);
            Map<UUID, Map<String, Location>> homes = homeManager.getHomes();
            for (Map.Entry<UUID, Map<String, Location>> entry : homes.entrySet()) {
                String playerName = UUIDManager.getPlayerFromUUID(entry.getKey());
                Set<String> playerHomes = entry.getValue().keySet();
                String[] homeStrings = playerHomes.toArray(new String[playerHomes.size()]);
                Arrays.sort(homeStrings);
                String homeList = homeListString(homeStrings);
                if (homeList != null) {
                    sender.sendMessage("[" + playerName + "]" + LanguageManager.HOME_LIST_PREFIX + " " + homeList);
                } else {
                    sender.sendMessage("[" + playerName + "]" + " " + LanguageManager.NO_HOMES_FOUND);
                }

            }
            return true;
        }
    }

    private String homeListString(String[] playerHomes) {
        int size = playerHomes.length;
        if (size > 0) {
            StringBuilder sb = new StringBuilder();
            if (size > 1) {
                for (String playerHome : playerHomes) {
                    sb.append(playerHome).append(", ");
                }
            }
            sb.append(playerHomes[size - 1]);

            return sb.toString();
        }
        return null;
    }
}
