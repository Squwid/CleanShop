package squwid.cmds;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import squwid.obj.MarketObj;

/**
 * Created by Ben on 4/3/2018.
 */
public class PriceCommand implements ShopCommand {
    
    @Override
    public void onCommand(Player p, String[] args) {
        if (args.length <2){
            mm.usage(p, "Usage: /shop price [amount] <item>");
            return;
        }
        int amount;
        try{
            amount = Integer.valueOf(args[0]);
        }
        catch (Exception e){
            mm.msg(p, "AMOUNT MUST BE A VALUE. EX 10");
            return;
        }
        if (amount < 0) {
            mm.msg(p, "AMOUNT MUST BE MORE THAN 0");
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

        ItemStack ni = new ItemStack(material, amount, (byte)data);
        // SellObj sellItem = new SellObj(null, ni.getData().toString(), ni.getType(), amount, 0);
        MarketObj mkt = new MarketObj(p.getUniqueId().toString(), amount, 0, ni.getData().toString());
        double totalPrice = t.getItemPrice(mkt, amount);
        if (totalPrice == 0){
            mm.msg(p, "THERE IS NOT ENOUGH " + ni.getData() + " CURRENTLY ON THE MARKET");
            return;
        }
        
        mm.msg(p, amount + " " + ni.getData() + " COSTS " + totalPrice + " GOLD");
    }

    @Override
    public String name() {
        return "price";
    }

    @Override
    public String alias() {
        return null;
    }

    @Override
    public String usage() {
        return "[amount] <item>";
    }

    @Override
    public String desc() {
        return "Check the price of an item";
    }

    @Override
    public boolean adminOnly() {
        return false;
    }
}
