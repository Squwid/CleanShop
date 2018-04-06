package squwid.obj;

/**
 * Created by Ben on 4/3/2018.
 */
public class MarketObj {
    private String sellerUUID;
    private String id;
    private int amount;
    private double price;
    
    public MarketObj(){
        this.amount = 0;
        this.price = 0;
    }
    
    public MarketObj(String uuid, int amount, double price, String id){
        this.id = id;
        this.sellerUUID = uuid;
        this.amount = amount;
        this.price = price;
    }

    public String getSellerUUID() {
        return sellerUUID;
    }

    public void setSellerUUID(String sellerUUID) {
        this.sellerUUID = sellerUUID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
