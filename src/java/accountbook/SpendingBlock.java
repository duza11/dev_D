package accountbook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpendingBlock {
    private List<SpendingItem> spendingItemList = new ArrayList<SpendingItem>();
    private Date date;
    private String place;

    public List<SpendingItem> getSpendingItemList() {
        return spendingItemList;
    }

    public void setSpendingItemList(List<SpendingItem> spendingItemList) {
        this.spendingItemList = spendingItemList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) throws Exception {
        this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}