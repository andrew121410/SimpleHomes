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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import SimpleHomes2.SimpleHomes2.SimpleHomes2.SimpleHomes;

public class LanguageFileManager {

    private static final String LANGUAGE_FILE_NAME = "languages.yml";
    private final SimpleHomes instance;
    private FileConfiguration languageConfig = null;
    private File languageFile = null;

    public LanguageFileManager(SimpleHomes instance) {
        this.instance = instance;
        saveDefaultLanguages();
    }

    public FileConfiguration getLanguageConfig() {
        if (languageConfig == null) {
            this.reloadLanguages();
        }
        return languageConfig;
    }

    void reloadLanguages() {
        if (languageFile == null) {
            languageFile = new File(instance.getDataFolder(), LANGUAGE_FILE_NAME);
        }
        languageConfig = YamlConfiguration.loadConfiguration(languageFile);

        InputStream defLanguageConfig = instance.getResource(LANGUAGE_FILE_NAME);
        if (defLanguageConfig != null) {
            // Yes, this is silly
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new BufferedReader(new InputStreamReader(defLanguageConfig)));
            languageConfig.setDefaults(defConfig);
        }
    }

    public void saveLanguages() {
        if (languageConfig == null || languageFile == null) {
            return;
        }
        try {
            getLanguageConfig().save(languageFile);
        } catch (IOException ex) {
            instance.getLogger().log(Level.SEVERE, "Could not save languages file to " + languageFile, ex);
        }
    }

    public void saveDefaultLanguages() {
        if (languageFile == null) {
            languageFile = new File(instance.getDataFolder(), LANGUAGE_FILE_NAME);
        }
        if (!languageFile.exists()) {
            instance.saveResource(LANGUAGE_FILE_NAME, false);
        }
    }
}
