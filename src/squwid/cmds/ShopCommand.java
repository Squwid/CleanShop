package squwid.cmds;

import org.bukkit.entity.Player;
import squwid.util.GetItem;
import squwid.util.MessageManager;
import squwid.util.ShopSettingsManager;
import squwid.util.TransactionInteractions;

/**
 * Created by Ben on 4/2/2018.
 */
public interface ShopCommand {
    
    MessageManager mm = MessageManager.getInstance();
    ShopSettingsManager s = ShopSettingsManager.getInstance();
    GetItem gi = GetItem.getInstance();
    TransactionInteractions t = TransactionInteractions.getInstance();
    
    void onCommand(Player p, String[] args);
    String name();
    String alias();
    String usage();
    String desc();
    boolean adminOnly();
}
