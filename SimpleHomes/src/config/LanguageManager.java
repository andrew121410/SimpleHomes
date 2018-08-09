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
package config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class LanguageManager {

    public static String HOME_DELETED = ChatColor.YELLOW + "Home deleted.";
    public static String HOME_LIST_PREFIX = ChatColor.YELLOW + "Homes:";
    public static String HOME_NOT_FOUND = ChatColor.RED + "Home not found.";
    public static String HOME_MAX_REACHED = ChatColor.RED + "Home cannot be set. The maximum number of homes has been " +
                                            "reached";
    public static String HOME_SET = ChatColor.YELLOW + "Home set.";
    public static String NO_HOMES_FOUND = ChatColor.RED + "No homes found.";
    public static String PLAYER_COMMAND_ONLY = ChatColor.RED + "Only players may issue that command.";
    public static String TELEPORT_OTHERHOME = ChatColor.YELLOW + "Teleported to %p's home.";
    public static String TELEPORT_SUCCESS = ChatColor.YELLOW + "Teleported.";
    public static String PLAYER_NOT_EXIST = ChatColor.RED + "That player doesn't exist.";

    public LanguageManager(LanguageFileManager fileManager) {
        loadMessages(fileManager.getLanguageConfig());
    }

    private String convertColours(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    private void loadMessages(FileConfiguration languageConfig) {
        HOME_DELETED = convertColours(languageConfig.getString("home-deleted"));
        HOME_LIST_PREFIX = convertColours(languageConfig.getString("home-list-prefix"));
        HOME_NOT_FOUND = convertColours(languageConfig.getString("home-not-found"));
        HOME_MAX_REACHED = convertColours(languageConfig.getString("home-max-reached"));
        HOME_SET = convertColours(languageConfig.getString("home-set"));
        NO_HOMES_FOUND = convertColours(languageConfig.getString("no-homes-found"));
        PLAYER_COMMAND_ONLY = convertColours(languageConfig.getString("player-command-only"));
        TELEPORT_OTHERHOME = convertColours(languageConfig.getString("teleport-otherhome"));
        TELEPORT_SUCCESS = convertColours(languageConfig.getString("teleport-success"));
        PLAYER_NOT_EXIST = convertColours(languageConfig.getString("player-not-exist"));
    }
}