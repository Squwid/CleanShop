package squwid.cmds;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import squwid.obj.MarketObj;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Ben on 4/3/2018.
 */
public class BuyCommand implements ShopCommand {
    @Override
    public void onCommand(Player p, String[] args) {
        //shop sell 50 cobblestone 1
        if (args.length < 3){
            mm.usage(p, "Usage: /shop buy [amount] <item> [totalPrice]");
            return;
        }

        // get amount
        int amount;
        try {
            amount = Integer.valueOf(args[0]);
        }
        catch (Exception e){
            mm.msg(p, "AMOUNT MUST BE A WHOLE NUMBER. EX 10");
            return;
        }

        //get total price
        double buyPrice;
        try {
            buyPrice = Double.parseDouble(args[2]);
        }
        catch (Exception e){
            mm.msg(p, "PRICE MUST BE A VALUE. EX 20.5");
            return;
        }
        if (buyPrice <= 0){
            mm.msg(p, "ITEM HAS TO BE PRICED MORE THAN 0");
            return;
        }
        if (amount <= 0){
            mm.msg(p, "ITEM HAS TO HAVE AN AMOUNT MORE THAN 0");
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
        MarketObj mkt = new MarketObj(p.getUniqueId().toString(), amount, 0, ni.getData().toString());
        double totalPrice = t.getItemPrice(mkt, amount);
        if (totalPrice == 0){
            mm.msg(p, "THERE IS NOT ENOUGH " + ni.getData() + " CURRENTLY ON THE MARKET");
            return;
        }
        
        if (totalPrice > buyPrice) {
            mm.msg(p, amount + " " + ni.getData() + " COSTS " + totalPrice);
            return;
        }
        
        double currentBal = t.getBalance(p.getUniqueId().toString());
        if (totalPrice > currentBal){
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.FLOOR);
            mm.msg(p, "YOU ONLY HAVE " + df.format(currentBal) + " GOLD IN YOUR ACCOUNT");
            return;
        }
        
        int roomInInventory = gi.roomInInv(p.getInventory(), ni);

        //mm.msg(p, "ROOM: " + roomInInventory);
        if (roomInInventory < amount) {
            mm.msg(p, "NOT ENOUGH INVENTORY SPACE FOR " + amount + " " + ni.getData());
            return;
        }
        // public MarketObj(String uuid, int amount, double price, String id)
        MarketObj item = new MarketObj(p.getUniqueId().toString(), amount, buyPrice, ni.getData().toString());
        double bought = t.buyItem(p, item, amount);
        if (bought == 0) {
            mm.error(p,"AN INTERNAL ERROR HAS OCCURRED");
            return;
        }
        gi.addInventoryItem(p, ni);
        mm.msg(p, "BOUGHT " + amount + " " + item.getId() + " FOR " + bought);
    }

    @Override
    public String name() {
        return "buy";
    }

    @Override
    public String alias() {
        return null;
    }

    @Override
    public String usage() {
        return "[amount] <item> [maxPrice]";
    }

    @Override
    public String desc() {
        return "Buy an item from the market";
    }

    @Override
    public boolean adminOnly() {
        return false;
    }
}
