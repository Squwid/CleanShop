package squwid.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ben on 4/2/2018.
 */
public class ShopSettingsManager {
    static ShopSettingsManager instance = new ShopSettingsManager();
    Plugin p;
    
    FileConfiguration market;
    FileConfiguration players;
    FileConfiguration transactions;
    FileConfiguration settings;
    
    File marketFile;
    File playerFile;
    File transactionsFile;
    File settingsFile;
    
    private ShopSettingsManager(){}
    
    public static ShopSettingsManager getInstance() { return instance; }
    
    public void setup(Plugin p) {
        this.p = p;
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }
        this.settingsFile = new File(p.getDataFolder(), "settings.yml");
        this.marketFile = new File(p.getDataFolder(), "market.yml");
        this.playerFile = new File(p.getDataFolder(), "players.yml");
        this.transactionsFile = new File(p.getDataFolder(), "transactions.yml");

        this.market = YamlConfiguration.loadConfiguration(this.marketFile);
        if (!this.marketFile.exists()) {
            try {
                this.marketFile.createNewFile();
            } catch (Exception e) {
                MessageManager.getInstance().error("Could not create marketfile");
                return;
            }
        }

        this.players = YamlConfiguration.loadConfiguration(this.playerFile);
        if (!this.playerFile.exists()) {
            try {
                this.playerFile.createNewFile();
            } catch (Exception e) {
                MessageManager.getInstance().error("Could not create playerfile");
                return;
            }
        }

        this.transactions = YamlConfiguration.loadConfiguration(this.transactionsFile);
        if (!this.transactionsFile.exists()) {
            try {
                this.transactionsFile.createNewFile();
            } catch (Exception e) {
                MessageManager.getInstance().error("Could not create transactionfile");
                return;
            }

        }
        
        this.settings = YamlConfiguration.loadConfiguration(this.settingsFile);
        if (!this.settingsFile.exists()) {
            try {
                this.settingsFile.createNewFile();
                settings.set("autoint", 0);
                this.saveSettings();
            }
            catch (Exception e) {
                MessageManager.getInstance().error("Could not create transactionfile");
            }
        }
    }
    
    
    /* AFTER SETTINGS */
    public FileConfiguration getPlayers() {return this.players;}
    public FileConfiguration getTransactions() {return this.transactions;}
    public FileConfiguration getMarket(){
        return this.market;
    }
    public FileConfiguration getSettings() { return this.settings;}
    
    

    public void savePlayers() {
        try {
            this.players.save(this.playerFile);
        }
        catch (IOException e){
            MessageManager.getInstance().error("could not save settings file");
        }
    }
    
    public void saveSettings() {
        try {
            this.settings.save(this.settingsFile);
        }
        catch (IOException e){
            MessageManager.getInstance().error("could not save settings file");
        }
    }
    
    public void saveMarket() {
        try {
            this.market.save(this.marketFile);
        }
        catch (IOException e) {
            MessageManager.getInstance().error("Could not save teams file");
        }
    }
    
    public PluginDescriptionFile getDesc() { return this.p.getDescription();}
}
