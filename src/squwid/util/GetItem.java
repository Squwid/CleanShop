package squwid.util;


import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ben on 4/2/2018.
 */
public class GetItem {
    static GetItem instance = new GetItem();
    public static GetItem getInstance() {return instance;}

    public static int getItemAmount(Inventory inv, ItemStack itemStack) {
        int amount = 0;
        for (ItemStack is : inv.getStorageContents()){
            if (is != null && is.getData().equals(itemStack.getData())/*is.getData().toString().equalsIgnoreCase(itemStack.getData().toString())*/){
                //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "HERE");
                amount = amount + is.getAmount();
            }
        }
        return amount;
    }
    
    public static int roomInInv(Inventory inv, ItemStack itemStack){
        int room = 0;
        for (ItemStack is : inv.getStorageContents()){
            if (is == null){
                room = room + 64;
                continue;
            }
            if (is.getType() == itemStack.getType() && is.getData() == itemStack.getData()) {
                room = room + (itemStack.getMaxStackSize() - is.getAmount());
            }
        }
        return room;
    }
    
    public void addInventoryItem(Player p, ItemStack itemStack){
        p.getInventory().addItem(itemStack);
        p.updateInventory();
    }
}
