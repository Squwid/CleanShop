package squwid.cmds;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Ben on 4/3/2018.
 */
public class WithdrawCommand implements ShopCommand {
    
    @Override
    public void onCommand(Player p, String[] args) {
        if (args.length > 1){
            mm.usage(p, "Usage: /withdraw [amount]");
            return;
        }
        int amount;
        try{
            amount = Integer.valueOf(args[0]);
        }
        catch (Exception e) {
            mm.msg(p, "AMOUNT MUST BE A VALUE. EX 10");
            return;
        }
        if(amount <= 0) {
            mm.msg(p,"YOU CANNOT WITHDRAW 0");
            return;
        }
        
        ItemStack ni = new ItemStack(Material.GOLD_INGOT, amount, (byte)0);
        double bal = t.getBalance(p.getUniqueId().toString());
        if (bal < amount) {
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.FLOOR);
            mm.msg(p, "YOU ONLY HAVE " + df.format(bal) + " IN YOUR ACCOUNT");
            return;
        }
        
        int roomInInventory = gi.roomInInv(p.getInventory(), ni);
        
        //mm.msg(p, "ROOM: " + roomInInventory);
        if (roomInInventory < amount) {
            mm.msg(p, "NOT ENOUGH INVENTORY SPACE FOR " + amount + " GOLD");
            return;
        }
        
        gi.addInventoryItem(p, ni);
        t.withdraw(p.getUniqueId().toString(), amount);
        mm.msg(p, "SUCCESSFULLY WITHDREW " + amount + " FROM YOUR ACCOUNT");
    }
    

    @Override
    public String name() {
        return "withdraw";
    }

    @Override
    public String alias() {
        return null;
    }

    @Override
    public String usage() {
        return "[amount]";
    }

    @Override
    public String desc() {
        return "Withdraw gold from account";
    }

    @Override
    public boolean adminOnly() {
        return false;
    }
}
