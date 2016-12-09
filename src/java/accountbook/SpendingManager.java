package accountbook;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                + "and (date_format(date, '%Y%m') = ?) group by sb.date";

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

    public List<BarChartItem> getBarChartItemList(User user, int kind, String date) throws Exception {
        List<BarChartItem> barChartItemList = new ArrayList<BarChartItem>();
        String sql = "select kind_name from spending_item_kind where kind_id = ?";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, kind);
        rs = ps.executeQuery();
        String dateArray[] = date.split("-");

        if (rs.next()) {
            for (int i = -11; i < 1; i++) {
                BarChartItem bci = new BarChartItem(rs.getString("kind_name"),
                        (Integer.parseInt(dateArray[1]) + i > 0)
                        ? String.format("%s/%d", dateArray[0], Integer.parseInt(dateArray[1]) + i)
                        : String.format("%d/%d", Integer.parseInt(dateArray[0]) - 1, Integer.parseInt(dateArray[1]) + i + 12));
                barChartItemList.add(bci);
            }
        }

        if (date == null) {
            java.util.Date d = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            date = sdf.format(d);
        }

        sql = "select day(sb.date) as d, sum(si.price * si.count) as sum "
                + "from users as u, spending_block as sb, spending_item as si, "
                + "spending_item_kind as sk where u.user_id = sb.user_id "
                + "and sb.block_id = si.block_id and si.kind_id = sk.kind_id "
                + "and u.user_id = ? and date_format(sb.date, '%Y%m') = ? "
                + "and sk.kind_id = ? group by day(sb.date)";

        ps.setInt(1, user.getUser_id());
        ps.setString(2, date);
        ps.setInt(3, kind);
        
        sql = "select date_format(sb.date, '%Y-%m') as month, sk.kind_name, sum(price * count) "
                + "as sum from users as u, spending_block as sb, "
                + "spending_item as si, spending_item_kind as sk "
                + "where u.user_id = sb.user_id and sb.block_id = si.block_id "
                + "and si.kind_id = sk.kind_id and u.user_id = ? and sk.kind_id = ? "
                + "and date_format(date, '%Y-%m') "
                + "between date_format(? - interval 11 month, '%Y-%m') "
                + "and date_format(?, '%Y-%m') group by month(sb.date)";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUser_id());
        ps.setInt(2, kind);
        ps.setDate(3, new Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime()));
        ps.setDate(4, new Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime()));
        rs = ps.executeQuery();

        if (!rs.next()) {
            rs.close();
            return barChartItemList;
        }

        for (BarChartItem bci : barChartItemList) {
            if (new SimpleDateFormat("yyyy/MM").parse(bci.getMonth()).
                    compareTo(new SimpleDateFormat("yyyy-MM").parse(rs.getString("month"))) == 0) {
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
        String sql = "select kind_id, kind_name from spending_item_kind order by kind_id asc";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        rs = ps.executeQuery();

        while (rs.next()) {
            PieChartItem pci = new PieChartItem(rs.getInt("kind_id"), rs.getString("kind_name"));
            pieChartItemList.add(pci);
        }

        sql = "select sk.kind_id, sk.kind_name, sum(si.price * si.count) as sum "
                + "from users as u, spending_block as sb, spending_item as si, "
                + "spending_item_kind as sk where u.user_id = sb.user_id "
                + "and sb.block_id = si.block_id and si.kind_id = sk.kind_id "
                + "and u.user_id = ? and date_format(sb.date, '%Y%m') = ? "
                + "group by sk.kind_id";

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
