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

package SimpleHomes2.SimpleHomes2.SimpleHomes2;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import commands.DeleteHomeCommand;
import commands.HomeCommand;
import commands.HomeListCommand;
import commands.OtherHomeCommand;
import commands.ReloadCommand;
import commands.SetHomeCommand;
import config.ConfigManager;
import config.HomeFileManager;
import config.LanguageFileManager;
import config.LanguageManager;
import events.GatewayListener;
import homes.HomeManager;

public class SimpleHomes extends JavaPlugin {

    public static SimpleHomes instance;
	public static SimpleHomes plugin;
	
    private HomeFileManager homeFileManager;
    private HomeManager homeManager;

    public void onEnable() {
    	plugin = this;
        FileConfiguration config = this.getConfig();
        //CONFIG Version
        this.homeFileManager = new HomeFileManager(this);
        this.homeManager = new HomeManager(homeFileManager);
        if (new File(this.getDataFolder(), "config.yml").exists()) {
            if (getConfig().getInt("ConfigVersion") < ConfigManager.CONFIG_VERSION_UUID_INTRODUCED || !getConfig().isSet("ConfigVersion")) {
                homeFileManager.UuidUpdate();
            }
        }
        //this.saveDefaultConfig();
        config.options().copyDefaults(true);
        this.saveConfig();
        LanguageFileManager languageFileManager = new LanguageFileManager(this);
        languageFileManager.saveLanguages();
        new LanguageManager(languageFileManager);

        new ConfigManager(this);
        homeFileManager.saveHomes();
        loadCommands();
        loadListeners();
        getLogger().info("SimpleHomes2 Enabled!");
    }

    @Override
    public void onDisable() {
        homeFileManager.saveHomes();
        getLogger().log(Level.INFO, "SimpleHomes2 Disabled!");
    }
    private void loadCommands() {
        //this.getCommand("delhome").setExecutor(new DeleteHomeCommand(homeManager));
    	new DeleteHomeCommand(this, homeManager);
        //this.getCommand("home").setExecutor(new HomeCommand(homeManager));
    	new HomeCommand(this, homeManager);
        //this.getCommand("homelist").setExecutor(new HomeListCommand(homeManager));
    	new HomeListCommand(this, homeManager);
        //this.getCommand("otherhome").setExecutor(new OtherHomeCommand(this, homeManager));
    	new OtherHomeCommand(this, homeManager);
        //this.getCommand("sethome").setExecutor(new SetHomeCommand(homeManager));
    	new SetHomeCommand(this, homeManager);
        //this.getCommand("shreload").setExecutor(new ReloadCommand(this));
        new ReloadCommand(this, homeManager);
    }

    private void loadListeners() {
    	Bukkit.getServer().getPluginManager().registerEvents(new GatewayListener(homeManager), this);
    }
}
