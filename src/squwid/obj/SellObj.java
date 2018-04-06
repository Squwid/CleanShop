package squwid.obj;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Ben on 4/2/2018.
 */
public class SellObj {
    private Player player;
    private String playerName;
    private UUID uuid;
    private String id;
    private String data;
    private Material material;
    private String matString;
    private int matAmount;
    private double price;

    public SellObj(Player p, String data,  Material material, int amount, double price){
        this.player = p;
        this.data = data;
        this.playerName = p.getName().toLowerCase();
        this.uuid = p.getUniqueId();
        this.id = p.getUniqueId().toString();
        this.matString = material.name();
        this.material = material;
        this.matAmount = amount;
        this.price = price/amount;
    }
    
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        String newId = id.replaceAll("[-]","");
        return newId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getMatString() {
        return matString;
    }

    public void setMatString(String matString) {
        this.matString = matString;
    }

    public int getMatAmount() {
        return matAmount;
    }
    
    public void setMatAmount(int matAmount) {
        this.matAmount = matAmount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
