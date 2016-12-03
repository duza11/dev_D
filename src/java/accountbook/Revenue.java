/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountbook;

import java.util.ArrayList;
import java.util.List;

public class Revenue {
    private List<RevenueBlock> revenueBlockList = new ArrayList<RevenueBlock>();

    public List<RevenueBlock> getRevenueBlockList() {
        return revenueBlockList;
    }

    public void setRevenueBlockList(List<RevenueBlock> revenueBlockList) {
        this.revenueBlockList = revenueBlockList;
    }
}