package squwid.util;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import squwid.cmds.*;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by Ben on 4/2/2018.
 */

public class ShopCommandManager implements CommandExecutor {
    
    static ShopCommandManager instance = new ShopCommandManager();
    TreeMap<String, ShopCommand> commands = new TreeMap<>();
    MessageManager mm = MessageManager.getInstance();
    ShopSettingsManager settings = ShopSettingsManager.getInstance();
    
    public static ShopCommandManager getInstance() {
        return instance;
    }
    
    private ShopCommandManager(){}
    
    public void setup(){
        this.commands.put("sell", new SellCommand());
        this.commands.put("deposit", new DepositCommand());
        this.commands.put("balance", new BalanceCommand());
        this.commands.put("withdraw", new WithdrawCommand());
        this.commands.put("buy", new BuyCommand());
        this.commands.put("price", new PriceCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            this.mm.log("Unable to use teams in console");
            return true;
        }
        Player p = (Player)sender;
        if(cmd.getName().equalsIgnoreCase("shop")) {
            if (args.length == 0) {
                this.mm.msg(p, "*** All Players ***", true);
                for (ShopCommand command : this.commands.values()) {
                    if (command.adminOnly())continue;
                    p.sendMessage(ChatColor.LIGHT_PURPLE + "/" + cmd.getName() + " " + command.name() +" "+ (command.usage() != null ? command.usage() : "") + ChatColor.WHITE + " - " + command.desc());
                }
                //TODO: Enable this at a later point
                //this.mm.msg(p, "*** Admin Commands ***", true);
                for (ShopCommand command : this.commands.values()) {
                    if (!command.adminOnly())continue;
                    this.mm.admin(p, "/" + cmd.getName() + " " + command.name() + " "+(command.usage() != null ? command.usage() : "") + " - " + command.desc());
                }
                return true;
            }
            String sub = args[0];
            Vector<String> l = new Vector<String>();
            l.addAll(Arrays.asList(args));
            l.remove(0);
            args = l.toArray(new String[0]);
            ShopCommand c = this.getCommand(sub);
            if (c == null) {
                this.mm.msg(p, "/shop " + sub + " is not a valid command. Use /shop for help.");
                return true;
            }
            if (c.adminOnly() && !p.hasPermission("shop.admin")){
                this.mm.msg(p, "Invalid command"); //TODO: make this look like a normal minecraft message
                return true;
            }
            try {
                c.onCommand(p, args);
            }
            catch (Exception e) {
                this.mm.error(e.getMessage());
                this.mm.error(p, e.getMessage());
            }
            return true;
        }
        return true;
    }


    private ShopCommand getCommand(String key) {
        for (ShopCommand sc : this.commands.values()) {
            if (sc.name().equalsIgnoreCase(key)) {
                return sc;
            }
            if (sc.alias() == null || !sc.alias().equalsIgnoreCase(key)) continue;
            return sc;
        }
        return null;
    }
    
    
}
