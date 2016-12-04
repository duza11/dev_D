package accountbook;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RevenueManager {

    private Statement st = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private DatabaseConnector dc = null;

    public RevenueManager(DatabaseConnector dc) {
        this.dc = dc;
    }

    public void setDayListRevenue(User user, int year, int month, List<DailyData> dayList) throws Exception {
        String sql = "select rb.date, sum(ri.price * ri.count) as sum "
                + "from users as u, revenue_block as rb, revenue_item as ri "
                + "where u.user_id = rb.user_id and rb.block_id = ri.block_id "
                + "and u.user_id = ? "
                + "and (date_format(date, '%Y%m') = ?) group by rb.date;";

        System.out.println("\n\n" + user.getUser_id());
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
                dd.setRevenue(rs.getInt("sum"));
                if (!rs.next()) {
                    break;
                }
            }
        }
        rs.close();
    }

    public void registerRevenueBlock(User user, RevenueBlock rb) throws Exception {
        String sql = "insert into revenue_item(block_id, item_name, kind_id, price, count) "
                + "select auto_increment, ?, ?, ?, ? "
                + "from information_schema.tables where table_name = 'revenue_block' "
                + "and table_schema = 'account_book'";
        dc.openConnection(sql);

        for (RevenueItem ri : rb.getRevenueItemList()) {
            ps = dc.getPreparedStatement();
            ps.setString(1, ri.getItemName());
            ps.setInt(2, ri.getKindId());
            ps.setInt(3, ri.getPrice());
            ps.setInt(4, ri.getCount());
            ps.addBatch();
        }
        ps.executeBatch();

        sql = "insert into revenue_block values(null, ?, ?, ?)";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUser_id());
        ps.setDate(2, new Date(rb.getDate().getTime()));
        ps.setString(3, rb.getPlace());
        ps.executeUpdate();
    }
    
    public Map<Integer, String> getRevenueKindMap() throws Exception {
        Map<Integer, String> revenueKindMap = new LinkedHashMap<Integer, String>();
        String sql = "select * from revenue_item_kind order by kind_id asc";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        rs = ps.executeQuery();
        
        while (rs.next()) {
            revenueKindMap.put(rs.getInt("kind_id"), rs.getString("kind_name"));
        }
        rs.close();
        return revenueKindMap;
    }
}