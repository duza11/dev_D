package accountbook;

public class DailyData {
    private int day;
    private int revenue;
    private int spending;

    public DailyData(int day) {
        this.day = day;
        this.revenue = -1;
        this.spending = -1;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getSpending() {
        return spending;
    }

    public void setSpending(int spending) {
        this.spending = spending;
    }
}
