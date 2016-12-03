package accountbook;

import java.util.ArrayList;
import java.util.List;

public class SpendingBlock {
    private List<SpendingItem> spendingItemList = new ArrayList<SpendingItem>();
    private String date;
    private String place;

    public List<SpendingItem> getSpendingItemList() {
        return spendingItemList;
    }

    public void setSpendingItemList(List<SpendingItem> spendingItemList) {
        this.spendingItemList = spendingItemList;
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