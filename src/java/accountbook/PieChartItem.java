package accountbook;

public class PieChartItem {
    private int kindId;
    private String kindName;
    private int price;

    public PieChartItem(int kindId, String kindName) {
        this.kindId = kindId;
        this.kindName = kindName;
        this.price = 0;
    }
    
    public int getKindId() {
        return kindId;
    }

    public void setKindName(int kindId) {
        this.kindId = kindId;
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
}