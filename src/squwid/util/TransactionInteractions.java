package squwid.util;

import org.bukkit.entity.Player;
import squwid.obj.MarketObj;
import squwid.obj.SellObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 4/2/2018.
 */
public class TransactionInteractions {
    static TransactionInteractions instance = new TransactionInteractions();
    public static TransactionInteractions getInstance() {return instance;}
    
    ShopSettingsManager ss = ShopSettingsManager.getInstance();
    MessageManager mm = MessageManager.getInstance();
    /*
        TransactionInteractions are all of the commands that use settings and overwrite them
     */
    
    
    /*
        public void withdraw(String uuid, int amount) {
            uuid = uuid.replaceAll("[-]", "");
            int current = ss.getPlayers().getInt(uuid + ".balance");
            ss.getPlayers().set(uuid + ".balance", current - amount);
            ss.savePlayers();
        }
     */
    
    public void buyBalanceChange(Player buyer, Double price, String item, Integer itemAmount,MarketObj m){
        String buyerUUID = buyer.getUniqueId().toString().replaceAll("[-]", "");
        String sellerUUID = m.getSellerUUID();
        double currentBuyer = ss.getPlayers().getDouble(buyerUUID + ".balance");
        double currentSeller = ss.getPlayers().getDouble(sellerUUID + ".balance");
        ss.getPlayers().set(buyerUUID + ".balance", currentBuyer - price);
        ss.getPlayers().set(sellerUUID + ".balance", currentSeller + price);
        ss.savePlayers();
        mm.messageSeller(m, item, itemAmount, price);
    }
    
    public int useAutoInt() {
        int autoInt = ss.getSettings().getInt("autoint");
        ss.getSettings().set("autoint", autoInt+1);
        ss.saveSettings();
        return autoInt;
    }

    // getBalance using a players UUID will change the id to have no '-' in it and then return the players balance
    //  rounded down
    public double getBalance(String uuid) {
        //String newId = id.replaceAll("[-]","");
        uuid = uuid.replaceAll("[-]","");
        if(ss.getPlayers().getDouble(uuid + ".balance") == 0){
            ss.getPlayers().set(uuid + ".balance", 0);
            ss.savePlayers();
            return 0;
        }
        return ss.getPlayers().getDouble(uuid + ".balance");
    }

    
    public void deposit(String uuid, double amount){
        uuid = uuid.replaceAll("[-]","");
        double current = ss.getPlayers().getDouble(uuid+".balance");
        ss.getPlayers().set(uuid + ".balance", amount + current);
        ss.savePlayers();
    }


    public void withdraw(String uuid, double amount) {
        uuid = uuid.replaceAll("[-]", "");
        double current = ss.getPlayers().getDouble(uuid + ".balance");
        ss.getPlayers().set(uuid + ".balance", current - amount);
        ss.savePlayers();
    }

    public void listSellItem(SellObj item) {
        int i = this.useAutoInt();
        String iString = String.valueOf(i);
        String loc = item.getData() + "." + iString;
        List<String> saleObjects = ss.getMarket().getStringList(item.getData() + ".salelist");
        saleObjects.add(iString);
        ss.getMarket().set(item.getData() + ".salelist", saleObjects);
        ss.getMarket().set(loc + ".uuid", item.getId());
        ss.getMarket().set(loc + ".amount", item.getMatAmount());
        ss.getMarket().set(loc + ".price", item.getPrice());
        ss.saveMarket();
    }

    
    // getItemPrice gets the price of an item using the MarketObject and the item amount, if there is enough
    //  of an item on the market it will return the price, if not it will return 0 telling the user that there is
    //  not enough of this item on the market
    public double getItemPrice(MarketObj item, int amount){
        String loc = item.getId();
        List<String> matString = ss.getMarket().getStringList(loc + ".salelist");
        // If there is none of that item on the market
        if (matString.size()==0) {
            return 0;
        }
        List<MarketObj> mktObjects = new ArrayList<>();
        for (int i=0; i<matString.size(); i++){
            String uuid = ss.getMarket().getString(loc + "." + matString.get(i) + ".uuid");
            int objAmount= ss.getMarket().getInt(loc + "." + matString.get(i) + ".amount");
            double price = ss.getMarket().getDouble(loc + "." + matString.get(i) + ".price");
            MarketObj obj = new MarketObj(uuid, objAmount, price, matString.get(i));
            mktObjects.add(obj);
        }
        // Once market objects are in a list, calculate how much each one costs
        double totalPrice = 0;
        int currentItems = 0;
        while(currentItems <= amount){
            if (mktObjects.size() == 0){
                return 0;
            }
            MarketObj cheapest = new MarketObj();
            // This loop gets the cheapest value in the market
            for (int i = 0; i < mktObjects.size(); i++){
                MarketObj current = mktObjects.get(i);
                // 0 would mean uninitialized
                if (cheapest.getPrice() == 0){
                    cheapest = current;
                }
                // if prices are same use the one with biggest amount (Will save looping time)
                else if(cheapest.getPrice() == current.getPrice() && cheapest.getAmount() < current.getPrice()){
                    cheapest = current;
                }
                // if current is cheaper than cheapest
                else if(cheapest.getPrice() > current.getPrice()){
                    cheapest = current;
                }
            }
            //mm.log("CHEAPEST PRICE: " + cheapest.getPrice());
            // If the current cheapest has enough stored in it to receive price
            if (currentItems + cheapest.getAmount() >= amount){
                int amountLeft = amount - currentItems;
                //mm.log("AMOUNT LEFT: " + amountLeft);
                totalPrice = totalPrice + (amountLeft * cheapest.getPrice());
                return totalPrice;
            }
            currentItems = currentItems + cheapest.getAmount();
            totalPrice = totalPrice + (cheapest.getAmount() * cheapest.getPrice());
            mktObjects.remove(cheapest);
        }
        return 0;
    }
    
    
    // buyItem takes a player and a MarketObj and removes it from the market list, this will error out if the 
    //  price is not checked first.
    public double buyItem(Player p, MarketObj item, int amount){
        String loc = item.getId();
        List<String> matString = ss.getMarket().getStringList(loc + ".salelist");
        // If there is none of that item on the market
        if (matString.size()==0) {
            return 0;
        }
        List<MarketObj> mktObjects = new ArrayList<>();
        for (int i=0; i<matString.size(); i++){
            String uuid = ss.getMarket().getString(loc + "." + matString.get(i) + ".uuid");
            int objAmount= ss.getMarket().getInt(loc + "." + matString.get(i) + ".amount");
            double price = ss.getMarket().getDouble(loc + "." + matString.get(i) + ".price");
            MarketObj obj = new MarketObj(uuid, objAmount, price, matString.get(i));
            mktObjects.add(obj);
        }
        // Once market objects are in a list, calculate how much each one costs
        double totalPrice = 0;
        int currentItems = 0;
        while(currentItems <= amount){
            if (mktObjects.size() == 0){
                return 0;
            }
            MarketObj cheapest = new MarketObj();
            // This loop gets the cheapest value in the market
            for (int i = 0; i < mktObjects.size(); i++){
                MarketObj current = mktObjects.get(i);
                // 0 would mean uninitialized
                if (cheapest.getPrice() == 0){
                    cheapest = current;
                }
                // if prices are same use the one with biggest amount (Will save looping time)
                else if(cheapest.getPrice() == current.getPrice() && cheapest.getAmount() < current.getPrice()){
                    cheapest = current;
                }
                // if current is cheaper than cheapest
                else if(cheapest.getPrice() > current.getPrice()){
                    cheapest = current;
                }
            }
            //mm.log("CHEAPEST PRICE: " + cheapest.getPrice());
            // If the current cheapest has enough stored in it to receive price
            if (currentItems + cheapest.getAmount() > amount){
                //mm.log("AMOUNT LEFT: " + amountLeft);
                int amountLeft = amount - currentItems;
                totalPrice = totalPrice + (amountLeft * cheapest.getPrice());
                ss.getMarket().set(loc + "." + cheapest.getId() + ".amount", cheapest.getAmount() - amountLeft);
                ss.saveMarket();
                buyBalanceChange(p, (amountLeft * cheapest.getPrice()), item.getId(), amountLeft, cheapest);
                return totalPrice;
            }
            if (currentItems + cheapest.getAmount() == amount){
                totalPrice = totalPrice + (cheapest.getAmount() * cheapest.getPrice());
                ss.getMarket().set(loc + "." + cheapest.getId(), null);
                matString.remove(cheapest.getId());
                ss.getMarket().set(loc + ".salelist", matString);
                ss.saveMarket();
                buyBalanceChange(p, (cheapest.getAmount() * cheapest.getPrice()), item.getId(), cheapest.getAmount(), cheapest);
                return totalPrice;
            }
            currentItems = currentItems + cheapest.getAmount();
            totalPrice = totalPrice + (cheapest.getAmount() * cheapest.getPrice());
            mktObjects.remove(cheapest);
            ss.getMarket().set(loc + "." + cheapest.getId(), null);
            //remove it from the string and save the string back
            buyBalanceChange(p, (cheapest.getAmount() * cheapest.getPrice()), item.getId(), cheapest.getAmount(), cheapest);
            matString.remove(cheapest.getId());
            ss.getMarket().set(loc + ".salelist", matString);
            ss.saveMarket();
        }
        return 0;
    }
    
}
