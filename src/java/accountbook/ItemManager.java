package accountbook;

import java.sql.*;
import java.util.*;

/*
 * データベースと連携し，商品の管理を担当するクラス
 */
public class ItemManager {

    private Statement st = null;
    private ResultSet rs = null;

    /**
     * コンストラクタ データベース名，パスワードテーブル名，DBのログイン名 DBログインパスワード名を引数にとる
     */
    public ItemManager(DatabaseConnector dc) {
        st = dc.getStatement();
    }

    /**
     * 全商品をDBから取得し，ArrayListとして返す．
     */
    public ArrayList<Item> getAllItems() throws Exception {

        ArrayList<Item> list = new ArrayList<Item>();
        ResultSet rs = null;

        String sql = "select * from item_tbl";
        rs = st.executeQuery(sql);

        while (rs.next()) {
            int id = Integer.parseInt(rs.getString("id"));
            String name = rs.getString("name");
            int price = Integer.parseInt(rs.getString("price"));
            Item item = new Item(id, name, price);
            list.add(item);
        }

        return list;
    }

    /**
     * 引数で指定されたid（String）の商品情報をDBから取得して呼び出し元へ返す．
     */
    public Item getItemById(String id) throws Exception {
        // 引数がint型のgetItemById（一つ下のメソッド）を呼ぶ
        return getItemById(Integer.parseInt(id));
    }

    /**
     * 引数で指定されたid（int）の商品情報をDBから取得して呼び出し元へ返す．
     */
    public Item getItemById(int id) throws Exception {

        Item item = null;

        ResultSet rs = null;

        String sql = "select * from item_tbl where id=" + id;
        rs = st.executeQuery(sql);

        if (!rs.next()) {
            return null;
        }

        item = new Item(id, rs.getString("name"), Integer.parseInt(rs.getString("price")));

        return item;
    }
}