package squwid.cmds;

import org.bukkit.entity.Player;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Ben on 4/2/2018.
 */
public class BalanceCommand implements ShopCommand {
    //balance
    @Override
    public void onCommand(Player p, String[] args) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.FLOOR);
        mm.msg(p, "CURRENT BALANCE: " + df.format(t.getBalance(p.getUniqueId().toString())));
    }

    @Override
    public String name() {
        return "balance";
    }

    @Override
    public String alias() {
        return "bal";
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public String desc() {
        return "Check your balance";
    }

    @Override
    public boolean adminOnly() {
        return false;
    }
}
