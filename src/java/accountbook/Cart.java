package accountbook;

import java.util.*;
import java.sql.*;

/**
 * ショッピングサイトでのカートを担当するクラス
 */
public class Cart {

    ArrayList<Item> itemList = null; //カート内の商品

    /**
     * コンストラクタ
     */
    public Cart() {
        //商品を格納する新しいArrayListを生成する
        itemList = new ArrayList<Item>();
    }

    /**
     * カート内のすべての商品をArrayListとして返す．
     */
    public ArrayList<Item> getAllItems() {
        return itemList;
    }

    /**
     * カートに格納された商品の数を返す
     */
    public int getSize() {
        return itemList.size();
    }

    /**
     * 指定された番号の商品を返す
     */
    public Item getItem(int index) {
        return itemList.get(index);
    }

    /**
     * カートへ商品を加える．
     */
    public void add(Item item) {
        int num = -1;
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId() == item.getId()) {
                num = i;
            }
        }
        if (num == -1) {
            itemList.add(item);
        } else {
            item.setCount(itemList.get(num).getCount() + 1);
            itemList.set(num, item);
        }
    }

    /**
     * 引数のindexで指定された添え字番号の商品をカートから削除する．
     */
    public void delete(int index) {
        itemList.remove(index);
    }

    /**
     * カートからすべての商品を削除する．
     */
    public void clear() {
        itemList.clear();
    }

    /**
     * カート内の商品の合計金額を返す．
     */
    public int getSumPrice() {
        int total = 0;

        for (int i = 0; i < itemList.size(); i++) {
            total += itemList.get(i).getPrice() * itemList.get(i).getCount();
        }

        return total;
    }
}
