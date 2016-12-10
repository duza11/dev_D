package accountbook;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        ps.setString(2, String.format("%d%02d", year, month));
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

    public List<BarChartItem> getBarChartItemList(User user, int kind, String date) throws Exception {
        List<BarChartItem> barChartItemList = new ArrayList<BarChartItem>();
        
        if (date == null) {
            java.util.Date d = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            date = sdf.format(d);
        }
        String dateArray[] = date.split("-");
        
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]) - 1, 1);
        int maxDate = c.getActualMaximum(Calendar.DATE);
        String sql = "select kind_name from revenue_item_kind where kind_id = ?";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, kind);
        rs = ps.executeQuery();

        if (rs.next()) {
            for (int i = 1; i <= maxDate; i++) {
                BarChartItem bci = new BarChartItem(rs.getString("kind_name"), Integer.toString(i));
                barChartItemList.add(bci);
            }
        }

        sql = "select day(rb.date) as d, sum(ri.price * ri.count) as sum "
                + "from users as u, revenue_block as rb, revenue_item as ri, "
                + "revenue_item_kind as rk where u.user_id = rb.user_id "
                + "and rb.block_id = ri.block_id and ri.kind_id = rk.kind_id "
                + "and u.user_id = ? and date_format(rb.date, '%Y-%m') = ? "
                + "and rk.kind_id = ? group by day(rb.date)";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUser_id());
        ps.setString(2, date);
        ps.setInt(3, kind);
        rs = ps.executeQuery();

        if (!rs.next()) {
            rs.close();
            return barChartItemList;
        }

        for (BarChartItem bci : barChartItemList) {
            if (rs.getString("d").equals(bci.getDay())) {
                bci.setPrice(rs.getInt("sum"));
                if (!rs.next()) {
                    break;
                }
            }
        }
        rs.close();
        return barChartItemList;
    }

    public List<PieChartItem> getPieChartItemList(User user, String date) throws Exception {
        List<PieChartItem> pieChartItemList = new ArrayList<PieChartItem>();
        String sql = "select kind_id, kind_name from revenue_item_kind order by kind_id asc";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        rs = ps.executeQuery();

        while (rs.next()) {
            PieChartItem pci = new PieChartItem(rs.getInt("kind_id"), rs.getString("kind_name"));
            pieChartItemList.add(pci);
        }

        if (date == null) {
            java.util.Date d = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            date = sdf.format(d);
        }

        sql = "select rk.kind_id, rk.kind_name, sum(ri.price * ri.count) as sum "
                + "from users as u, revenue_block as rb, revenue_item as ri, "
                + "revenue_item_kind as rk where u.user_id = rb.user_id "
                + "and rb.block_id = ri.block_id and ri.kind_id = rk.kind_id "
                + "and u.user_id = ? and date_format(rb.date, '%Y-%m') = ? "
                + "group by rk.kind_id";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUser_id());
        ps.setString(2, date);
        rs = ps.executeQuery();

        if (!rs.next()) {
            rs.close();
            return pieChartItemList;
        }

        for (PieChartItem pci : pieChartItemList) {
            if (pci.getKindId() == rs.getInt("kind_id")) {
                pci.setPrice(rs.getInt("sum"));
                if (!rs.next()) {
                    break;
                }
            }
        }
        rs.close();

        return pieChartItemList;
    }
}
