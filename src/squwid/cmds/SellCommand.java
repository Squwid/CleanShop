package squwid.cmds;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import squwid.obj.SellObj;
import squwid.util.TransactionInteractions;

/**
 * Created by Ben on 4/2/2018.
 */
public class SellCommand implements ShopCommand {
        
    @Override
    public void onCommand(Player p, String[] args) {
        //shop sell 50 cobblestone 1
        if (args.length < 3){
            mm.usage(p, "Usage: /shop sell <amount> [item] <totalPrice>");
            return;
        }
        
        // get amount
        int amount;
        try {
            amount = Integer.valueOf(args[0]);
        }
        catch (Exception e){
            mm.msg(p, "AMOUNT MUST BE A VALUE. EX 10");
            return;
        }

        //get total price
        double price;
        try {
            price = Double.parseDouble(args[2]);
        }
        catch (Exception e){
            mm.msg(p, "PRICE MUST BE A VALUE. EX 20.5");
            return;
        }
        if (price <= 0){
            mm.msg(p, "ITEM HAS TO BE PRICED MORE THAN 0");
            return;
        }
        if (amount <= 0){
            mm.msg(p, "ITEM HAS TO HAVE AN AMOUNT MORE THAN 0");
            return;
        }

        double per = price / (double)amount;
        if ( per < 0.00001){
            mm.msg(p, "MINIMUM PER ITEM VALUE OF 0.00001");
            return;
        } else if (per > 10000) {
            mm.msg(p, "MAXIMUM PER ITEM VALUE OF 10000");
            return;
        }
        
        String matName = args[1];
        int data = 0;
        if (matName.contains(":")) {
            String[] split = matName.split(":", 2);
            matName = split[0];
            try {
                data = Integer.valueOf(split[1]);
            }
            catch (Exception e){
                mm.error(p, e.getMessage());
                mm.msg(p, "For a material like Andesite, use stone:5");
                return;
            }
        }
        Material material = Material.getMaterial(matName.toUpperCase());
        if (material == null){
            mm.msg(p, "NO MATERIAL FOUND WITH THE NAME " + matName.toUpperCase());
            return;
        }
        
        // Check to see if the material is able to be sold on the market
        //TODO: Check to see if the material is enabled in the settings

        if (material.isRecord() || material.getMaxDurability()!= 0){
            mm.msg(p,"YOU CANNOT SELL THIS ITEM IN THE SHOP ");
            return;
        }
        
        
        
        ItemStack ni = new ItemStack(material, amount, (byte)data);
        
        Inventory pinv = p.getInventory();
        if (!pinv.containsAtLeast(ni, ni.getAmount())){
            int itemAmount = gi.getItemAmount(pinv, ni);
            mm.msg(p, "YOU ONLY HAVE " + itemAmount + " OF " + ni.getData());
            return;
        }

        // MarketObj(String uuid, int amount, double price, String id)
        SellObj sellItem = new SellObj(p, ni.getData().toString(), ni.getType(), amount, price);
        try {
            TransactionInteractions.getInstance().listSellItem(sellItem);
        }
        catch (Exception e){
            if (p.hasPermission("shop.admin")){
                mm.error(p, e.getMessage());
                return;
            }
            mm.error(e.getMessage());
        }

        mm.msg(p, "SUCCESSFULLY LISTED " + ni.getAmount() + " " + ni.getData() +  " ON THE MARKET FOR " + price);
        p.getInventory().removeItem(ni);
        p.updateInventory();
        
        return;
    }
    
    
    
    @Override
    public String name() {
        return "sell";
    }

    @Override
    public String alias() {
        return null;
    }

    @Override
    public String usage() {
        return "[amount] <item> [totalPrice]";
    }

    @Override
    public String desc() {
        return "Sell items from your inventory";
    }

    @Override
    public boolean adminOnly() {
        return false;
    }
}
