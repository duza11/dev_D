package accountbook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RevenueBlock {
    private List<RevenueItem> revenueItemList = new ArrayList<RevenueItem>();
    private Date date;
    private String place;
    private int blockId;

    public List<RevenueItem> getRevenueItemList() {
        return revenueItemList;
    }

    public void setRevenueItemList(List<RevenueItem> revenueItemList) {
        this.revenueItemList = revenueItemList;
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

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }
}