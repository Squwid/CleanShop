package squwid.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import squwid.obj.MarketObj;

/**
 * Created by Ben on 4/3/2018.
 */
public class MessageManager {
    
    static MessageManager instance = new MessageManager();
    public static MessageManager getInstance() { return instance; }

    public void msg(Player p, String msg) {p.sendMessage(ChatColor.LIGHT_PURPLE + msg);}
    public void msg(Player p, String msg, boolean urgent){p.sendMessage(ChatColor.LIGHT_PURPLE + msg);}
    public void usage(Player p, String msg) { p.sendMessage(ChatColor.RED + msg);}
    public void log(String msg) { Bukkit.getServer().getConsoleSender().sendMessage(msg);}
    public void broadcast(String msg) {Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + msg);}
    public void admin(Player p, String msg) {p.sendMessage(ChatColor.AQUA + msg);}
    public void error(Player p, String msg) {p.sendMessage(ChatColor.RED + msg);}
    public void error(String msg) {Bukkit.getServer().getConsoleSender().sendMessage("ERROR: " + msg);}

    public void messageSeller(MarketObj obj, String item, Integer amount, Double price){
        String sellerUUID = obj.getSellerUUID().toString().replaceAll("[-]", "");
        for (Object pl : Bukkit.getServer().getOnlinePlayers()) {
            if (!(pl instanceof Player)){ continue; }
            String uuid = ((Player) pl).getUniqueId().toString().replaceAll("[-]", "");
            if (uuid.equalsIgnoreCase(sellerUUID)){
                ((Player)pl).sendMessage(ChatColor.LIGHT_PURPLE + "YOU SOLD " + amount + " " + item + " FOR " + price + " GOLD");
                return;
            }
            
        }
        return;
    }

}
