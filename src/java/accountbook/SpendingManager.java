package accountbook;

import java.sql.*;

public class SpendingManager {

    private Statement st = null;
    private ResultSet rs = null;

    public SpendingManager(DatabaseConnector dc) {
        st = dc.getStatement();
    }

    public void registerSpendingBlock(SpendingBlock sb) {
        String sql = "insert all "
                + "into spending_block "
                + "values(user_id, '" + sb.getDate() + "', '" + sb.getPlace() + "') "
                + "select auto_increment as user_id from information_schema.tables "
                + "where table_name = 'users' and table_schema = 'account_book'";

        for (SpendingItem si : sb.getSpendingItemList()) {
            sql += "into spending_item values(block_id, "
                    + "'" + si.getItemName() + "', " + si.getKindId() + ", "
                    + si.getPrice() + ", " + si.getCount() + ") "
                    + "select auto_increment as block_id from information_schema.tables "
                    + "where table_name = 'spending_block' and table_schema = 'account_book'";
        }
        System.out.println(sql);
//        "select auto_increment from information_schema.tables where table_name = 'users' and table_schema = 'account_book'";
    }
}
