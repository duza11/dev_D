package accountbook;

public class BarChartItem {
    int price;
    String kind;
    String month;

    public BarChartItem(String kind, String month) {
        this.price = 0;
        this.kind = kind;
        this.month = month;
    }

    public BarChartItem(int price, String kind, String month) {
        this.price = price;
        this.kind = kind;
        this.month = month;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
