package accountbook;

public class BarChartItem {
    int price;
    String kind;
    String month;
    String day;

    public BarChartItem(String kind, String day) {
        this.price = 0;
        this.kind = kind;
        this.day = day;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
