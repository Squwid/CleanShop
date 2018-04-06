package squwid;

import org.bukkit.plugin.java.JavaPlugin;
import squwid.util.ShopCommandManager;
import squwid.util.ShopSettingsManager;

/**
 * Created by Ben on 4/1/2018.
 */
public class CleanShop extends JavaPlugin {
    
    public void onEnable(){
        ShopSettingsManager.getInstance().setup(this);
        ShopCommandManager.getInstance().setup();
        this.getCommand("shop").setExecutor(ShopCommandManager.getInstance());
    }
}
