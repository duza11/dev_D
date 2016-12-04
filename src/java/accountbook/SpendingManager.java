package accountbook;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SpendingManager {

    private Statement st = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private DatabaseConnector dc = null;

    public SpendingManager(DatabaseConnector dc) {
        this.dc = dc;
    }

    public void setDayListSpending(User user, int year, int month, List<DailyData> dayList) throws Exception {
        String sql = "select sb.date, sum(si.price * si.count) as sum "
                + "from users as u, spending_block as sb, spending_item as si "
                + "where u.user_id = sb.user_id and sb.block_id = si.block_id "
                + "and u.user_id = ? "
                + "and (date_format(date, '%Y%m') = ?) group by sb.date;";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUser_id());
        ps.setString(2, String.format("%d%d", year, month));
        rs = ps.executeQuery();

        if (!rs.next()) {
            rs.close();
            return;
        }

        for (DailyData dd : dayList) {
            if (rs.getDate("date").compareTo(new SimpleDateFormat("yyyy-MM-dd")
                    .parse(String.format("%d-%d-%d", year, month, dd.getDay()))) == 0) {
                dd.setSpending(rs.getInt("sum"));
                if (!rs.next()) {
                    break;
                }
            }
        }
        rs.close();
    }

    public void registerSpendingBlock(User user, SpendingBlock sb) throws Exception {
        String sql = "insert into spending_item(block_id, item_name, kind_id, price, count) "
                + "select auto_increment, ?, ?, ?, ? "
                + "from information_schema.tables where table_name = 'spending_block' "
                + "and table_schema = 'account_book'";
        dc.openConnection(sql);
        for (SpendingItem si : sb.getSpendingItemList()) {
            ps = dc.getPreparedStatement();
            ps.setString(1, si.getItemName());
            ps.setInt(2, si.getKindId());
            ps.setInt(3, si.getPrice());
            ps.setInt(4, si.getCount());
            ps.addBatch();
        }
        ps.executeBatch();

        sql = "insert into spending_block values(null, ?, ?, ?)";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUser_id());
        ps.setDate(2, new Date(sb.getDate().getTime()));
        ps.setString(3, sb.getPlace());
        ps.executeUpdate();
    }
    
    public Map<Integer, String> getSpendingKindMap() throws Exception {
        Map<Integer, String> spendingKindMap = new LinkedHashMap<Integer, String>();
        String sql = "select * from spending_item_kind order by kind_id asc";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        rs = ps.executeQuery();
        
        while (rs.next()) {
            spendingKindMap.put(rs.getInt("kind_id"), rs.getString("kind_name"));
        }
        rs.close();
        return spendingKindMap;
    }
}
