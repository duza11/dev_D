package accountbook;

public class SpendingItem {
    private String itemName;
    private int kindId;
    private String kindName;
    private int price;
    private int count;

    public String getItemName() {
        return itemName;
    }

    public int getKindId() {
        return kindId;
    }

    public void setKindId(int kindId) {
        this.kindId = kindId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}