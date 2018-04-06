package squwid.cmds;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ben on 4/2/2018.
 */
public class DepositCommand implements ShopCommand {

    @Override
    public void onCommand(Player p, String[] args) {
        if (args.length > 1){
            mm.usage(p, "Usage: /deposit [amount]");
            return;
        }
        int amount;
        try{
            amount = Integer.valueOf(args[0]);
        }
        catch (Exception e) {
            mm.msg(p, "AMOUNT MUST BE A WHOLE NUMBER");
            return;
        }
        if(amount <= 0) {
            mm.msg(p,"YOU CANNOT DEPOSIT " + amount);
            return;
        }
        
        ItemStack is = new ItemStack(Material.GOLD_INGOT, amount, (byte)0); 
        
        Inventory pinv = p.getInventory();
        if (!p.getInventory().containsAtLeast(is, amount)){
            int itemAmount = gi.getItemAmount(pinv, is);
            mm.msg(p, "TRIED TO DEPOSIT " + amount + " GOLD, BUT YOU ONLY HAVE " + itemAmount);
            return;
        }
        t.deposit(p.getUniqueId().toString(), amount);
        p.getInventory().removeItem(is);
        p.updateInventory();
        mm.msg(p, "SUCCESSFULLY DEPOSITED " + amount + " GOLD INTO ACCOUNT");
    }

    @Override
    public String name() {
        return "deposit";
    }

    @Override
    public String alias() {
        return "dep";
    }

    @Override
    public String usage() {
        return "[amount]";
    }

    @Override
    public String desc() {
        return "Deposit gold into your account";
    }

    @Override
    public boolean adminOnly() {
        return false;
    }
}
