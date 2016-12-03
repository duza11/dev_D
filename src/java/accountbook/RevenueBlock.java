package accountbook;

import java.util.ArrayList;
import java.util.List;

public class RevenueBlock {
    private List<SpendingItem> revenueItemList = new ArrayList<SpendingItem>();
    private String date;
    private String place;

    public List<SpendingItem> getRevenueItemList() {
        return revenueItemList;
    }

    public void setRevenueItemList(List<SpendingItem> revenueItemList) {
        this.revenueItemList = revenueItemList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}