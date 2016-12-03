/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountbook;

import java.util.ArrayList;
import java.util.List;

public class Spending {
    private List<SpendingBlock> spendingBlockList = new ArrayList<SpendingBlock>();

    public List<SpendingBlock> getSpendingBlockList() {
        return spendingBlockList;
    }

    public void setSpendingBlockList(List<SpendingBlock> spendingBlockList) {
        this.spendingBlockList = spendingBlockList;
    }
}