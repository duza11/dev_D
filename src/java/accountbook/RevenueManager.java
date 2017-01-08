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
                + "and (date_format(date, '%Y-%m') = ? or date_format(date, '%Y-%c') = ?) "
                + "group by rb.date;";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUserId());
        ps.setString(2, String.format("%d-%d", year, month));
        ps.setString(3, String.format("%d-%d", year, month));
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

    public void registerRevenueBlock(int userId, int blockId, RevenueBlock rb) throws Exception {
        String sql = "insert into revenue_item(block_id, item_name, kind_id, price, count) "
                + "select auto_increment, ?, ?, ?, ? "
                + "from information_schema.tables where table_name = 'revenue_block' "
                + "and table_schema = 'account_book'";

        if (blockId != 0) {
            sql = "insert into revenue_item(block_id, item_name, kind_id, price, count) "
                    + "values(?, ?, ?, ?, ?)";
        }

        dc.openConnection(sql);

        for (RevenueItem ri : rb.getRevenueItemList()) {
            ps = dc.getPreparedStatement();
            if (blockId == 0) {
                ps.setString(1, ri.getItemName());
                ps.setInt(2, ri.getKindId());
                ps.setInt(3, ri.getPrice());
                ps.setInt(4, ri.getCount());
            } else {
                ps.setInt(1, blockId);
                ps.setString(2, ri.getItemName());
                ps.setInt(3, ri.getKindId());
                ps.setInt(4, ri.getPrice());
                ps.setInt(5, ri.getCount());
            }
            ps.addBatch();
        }
        ps.executeBatch();

        sql = "insert into revenue_block values(" + ((blockId == 0) ? "null" : "?") + ", ?, ?, ?)";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        if (blockId == 0) {
            ps.setInt(1, userId);
            ps.setDate(2, new Date(rb.getDate().getTime()));
            ps.setString(3, rb.getPlace());
        } else {
            ps.setInt(1, blockId);
            ps.setInt(2, userId);
            ps.setDate(3, new Date(rb.getDate().getTime()));
            ps.setString(4, rb.getPlace());
        }
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
                + "and u.user_id = ? and (date_format(rb.date, '%Y-%m') = ? or date_format(rb.date, '%Y-%c') = ?)"
                + "and rk.kind_id = ? group by day(rb.date)";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUserId());
        ps.setString(2, date);
        ps.setString(3, date);
        ps.setInt(4, kind);
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

    public List<BarChartItem> getStackedBarChartItemList(User user, int kind, String date) throws Exception {
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

        String sql = "select kind_id, kind_name from revenue_item_kind order by kind_id asc";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();

        for (int i = 1; i <= maxDate; i++) {
            rs = ps.executeQuery();
            while (rs.next()) {
                BarChartItem bci = new BarChartItem(rs.getString("kind_name"), Integer.toString(i));
                barChartItemList.add(bci);
            }
        }

        sql = "select rk.kind_id, rk.kind_name, day(rb.date) day, sum(ri.price * ri.count) sum "
                + "from users u, revenue_block rb, revenue_item ri, revenue_item_kind rk "
                + "where u.user_id = rb.user_id and rb.block_id = ri.block_id "
                + "and ri.kind_id = rk.kind_id and u.user_id = ? "
                + "and (date_format(rb.date, '%Y-%m') = ? or date_format(rb.date, '%Y-%c') = ?) "
                + "group by day, rk.kind_id order by day, kind_id asc;";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUserId());
        ps.setString(2, date);
        ps.setString(3, date);
        rs = ps.executeQuery();

        if (!rs.next()) {
            rs.close();
            return barChartItemList;
        }

        for (BarChartItem bci : barChartItemList) {
            if (rs.getString("day").equals(bci.getDay()) && rs.getString("kind_name").equals(bci.getKind())) {
                bci.setPrice(rs.getInt("sum"));
                if (!rs.next()) {
                    break;
                }
            }
        }

        rs.close();

        return barChartItemList;
    }

    public List<BarChartItem> getYearlyBarChartItemList(User user, int kind, String date) throws Exception {
        List<BarChartItem> barChartItemList = new ArrayList<BarChartItem>();

        if (date == null) {
            java.util.Date d = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            date = sdf.format(d);
        }

        String sql = "select kind_name from revenue_item_kind where kind_id = ?";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, kind);
        rs = ps.executeQuery();

        if (rs.next()) {
            for (int i = 1; i < 13; i++) {
                BarChartItem bci = new BarChartItem(rs.getString("kind_name"), Integer.toString(i));
                barChartItemList.add(bci);
            }
        }

        sql = "select month(rb.date) as month, sum(ri.price * ri.count) as sum "
                + "from users as u, revenue_block as rb, revenue_item as ri, "
                + "revenue_item_kind as rk where u.user_id = rb.user_id "
                + "and rb.block_id = ri.block_id and ri.kind_id = rk.kind_id "
                + "and u.user_id = ? and date_format(rb.date, '%Y') = ? "
                + "and rk.kind_id = ? group by month;";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUserId());
        ps.setString(2, date);
        ps.setInt(3, kind);
        rs = ps.executeQuery();

        if (!rs.next()) {
            rs.close();
            return barChartItemList;
        }

        for (BarChartItem bci : barChartItemList) {
            if (rs.getString("month").equals(bci.getDay())) {
                bci.setPrice(rs.getInt("sum"));
                if (!rs.next()) {
                    break;
                }
            }
        }
        rs.close();
        return barChartItemList;
    }

    public List<BarChartItem> getYearlyStackedBarChartItemList(User user, int kind, String date) throws Exception {
        List<BarChartItem> barChartItemList = new ArrayList<BarChartItem>();

        if (date == null) {
            java.util.Date d = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            date = sdf.format(d);
        }

        String sql = "select kind_id, kind_name from revenue_item_kind order by kind_id asc";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();

        for (int i = 1; i < 13; i++) {
            rs = ps.executeQuery();
            while (rs.next()) {
                BarChartItem bci = new BarChartItem(rs.getString("kind_name"), Integer.toString(i));
                barChartItemList.add(bci);
            }
        }

        sql = "select rk.kind_id, rk.kind_name, month(rb.date) month, sum(ri.price * ri.count) sum "
                + "from users u, revenue_block rb, revenue_item ri, revenue_item_kind rk "
                + "where u.user_id = rb.user_id and rb.block_id = ri.block_id "
                + "and ri.kind_id = rk.kind_id and u.user_id = ? "
                + "and date_format(rb.date, '%Y') = ? group by month, rk.kind_id order by month, kind_id asc";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUserId());
        ps.setString(2, date);
        rs = ps.executeQuery();

        if (!rs.next()) {
            rs.close();
            return barChartItemList;
        }

        for (BarChartItem bci : barChartItemList) {
            if (rs.getString("month").equals(bci.getDay()) && rs.getString("kind_name").equals(bci.getKind())) {
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
                + "and u.user_id = ? and (date_format(rb.date, '%Y-%m') = ? "
                + "or date_format(rb.date, '%Y-%c') = ?) group by rk.kind_id";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUserId());
        ps.setString(2, date);
        ps.setString(3, date);
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

    public List<PieChartItem> getYearlyPieChartItemList(User user, String date) throws Exception {
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            date = sdf.format(d);
        }

        sql = "select rk.kind_id, rk.kind_name, sum(ri.price * ri.count) as sum "
                + "from users as u, revenue_block as rb, revenue_item as ri, "
                + "revenue_item_kind as rk where u.user_id = rb.user_id "
                + "and rb.block_id = ri.block_id and ri.kind_id = rk.kind_id "
                + "and u.user_id = ? and date_format(rb.date, '%Y') = ? "
                + "group by rk.kind_id";

        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUserId());
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

    public List<RevenueBlock> setDailyDataSet(User user, String date) throws Exception {
        List<RevenueBlock> revenueBlockList = new ArrayList<RevenueBlock>();
        String sql = "select ri.item_name, rk.kind_name, ri.price, ri.count, "
                + "ri.kind_id, rb.block_id, rb.place, rb.date from users as u, "
                + "revenue_block as rb, revenue_item as ri, revenue_item_kind as rk "
                + "where u.user_id = rb.user_id and rb.block_id = ri.block_id "
                + "and ri.kind_id = rk.kind_id and u.user_id = ? and rb.date = ? order by rb.block_id, ri.item_id asc";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, user.getUserId());
        ps.setDate(2, new Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime()));
        rs = ps.executeQuery();

        if (!rs.next()) {
            return revenueBlockList;
        }

        int counter = 0;
        while (true) {
            RevenueBlock rb = new RevenueBlock();
            rb.setBlockId(rs.getInt("block_id"));
            rb.setDate(rs.getString("date"));
            rb.setPlace(rs.getString("place"));
            List<RevenueItem> rIList = new ArrayList<>();
            while (counter == rs.getInt("block_id")) {
                RevenueItem ri = new RevenueItem();
                ri.setItemName(rs.getString("item_name"));
                ri.setKindName(rs.getString("kind_name"));
                ri.setPrice(rs.getInt("price"));
                ri.setCount(rs.getInt("count"));
                rIList.add(ri);
                if (!rs.next()) {
                    rb.setRevenueItemList(rIList);
                    revenueBlockList.add(rb);
                    return revenueBlockList;
                }
            }
            counter = rs.getInt("block_id");
            if (!rIList.isEmpty()) {
                rb.setRevenueItemList(rIList);
                revenueBlockList.add(rb);
            }
        }
    }

    public RevenueBlock getRevenueBlock(int userId, int blockId) throws Exception {
        String sql = "select * from users u, revenue_block rb, revenue_item ri "
                + "where u.user_id = rb.user_id and rb.block_id = ri.block_id "
                + "and u.user_id = ? and rb.block_id = ?;";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, userId);
        ps.setInt(2, blockId);
        rs = ps.executeQuery();

        RevenueBlock rb = new RevenueBlock();
        List<RevenueItem> riList = new ArrayList<>();

        while (rs.next()) {
            rb.setBlockId(rs.getInt("rb.block_id"));
            rb.setDate(rs.getString("date"));
            rb.setPlace(rs.getString("place"));
            RevenueItem ri = new RevenueItem();
            ri.setItemName(rs.getString("item_name"));
            ri.setKindId(rs.getInt("kind_id"));
            ri.setPrice(rs.getInt("price"));
            ri.setCount(rs.getInt("count"));
            riList.add(ri);
        }

        if (riList.isEmpty()) {
            return null;
        }
        rb.setRevenueItemList(riList);
        return rb;
    }

    public boolean deleteRevenue(int userId, int blockId) throws Exception {
        String sql = "delete rb, ri from "
                + "((users u left join revenue_block rb on u.user_id = rb.user_id) "
                + "left join revenue_item ri on rb.block_id = ri.block_id) "
                + "where u.user_id = ? and rb.block_id = ?";
        dc.openConnection(sql);
        ps = dc.getPreparedStatement();
        ps.setInt(1, userId);
        ps.setInt(2, blockId);
        if (ps.executeUpdate() == 0) {
            return false;
        }
        return true;
    }
}
