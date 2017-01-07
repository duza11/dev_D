
import accountbook.BarChartItem;
import accountbook.DatabaseConnector;
import accountbook.RevenueManager;
import accountbook.SpendingManager;
import accountbook.User;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChartServlet extends HttpServlet {

    private static String DB_NAME = "account_book";               // DB名
    private static String DB_USER = "root";                  // DBのユーザ名
    private static String DB_PASS = "root";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            DatabaseConnector dc = null; // データベースへの接続を行うオブジェクト
            SpendingManager sm = null; // 支出に関する処理を担当
            RevenueManager rm = null; // 収入に関する処理を担当

            dc = new DatabaseConnector(DB_NAME, DB_USER, DB_PASS);
            dc.openConnection(); // DBへ接続

            sm = new SpendingManager(dc);
            rm = new RevenueManager(dc);

            List<BarChartItem> barChartItemList = null;
            User user = (User) request.getSession(true).getAttribute("user");
            String dataLabel = "";

            if (user == null) {
                request.setAttribute("message", "先にログインしてください");
                response.sendRedirect("AccountBookServlet");
            }

            String action = request.getParameter("action");
            int kind = Integer.parseInt(request.getParameter("category"));
            String date = request.getParameter("date");

            if (action.equals("show_monthly_rev_bar")) {
                barChartItemList = (kind == 0)? rm.getStackedBarChartItemList(user, kind, date) : rm.getBarChartItemList(user, kind, date);
                dataLabel = "日";
            } else if (action.equals("show_monthly_spe_bar")) {
                barChartItemList = (kind == 0)? sm.getStackedBarChartItemList(user, kind, date) : sm.getBarChartItemList(user, kind, date);
                dataLabel = "日";
            } else if (action.equals("show_yearly_rev_bar")) {
                barChartItemList = (kind == 0)? rm.getYearlyStackedBarChartItemList(user, kind, date) : rm.getYearlyBarChartItemList(user, kind, date);
                dataLabel = "月";
            } else if (action.equals("show_yearly_spe_bar")) {
                barChartItemList = (kind == 0)? sm.getYearlyStackedBarChartItemList(user, kind, date) : sm.getYearlyBarChartItemList(user, kind, date);
                dataLabel = "月";
            } else {
                request.setAttribute("message", "不正なパラメータが検出されました");
                response.sendRedirect("AccountBookServlet");
            }

            DefaultCategoryDataset objDcd = new DefaultCategoryDataset();
            for (BarChartItem bci : barChartItemList) {
                objDcd.addValue(bci.getPrice(), bci.getKind(), bci.getDay());
            }

            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            JFreeChart objCht = ChartFactory.createStackedBarChart("", dataLabel, "円", objDcd, PlotOrientation.VERTICAL, true, false, false);
            objCht.setBorderVisible(true);
            objCht.setBackgroundPaint(Color.WHITE);
            CategoryPlot cp = (CategoryPlot) objCht.getPlot();
            NumberAxis na = (NumberAxis) cp.getRangeAxis();
            na.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            na.setLowerBound(0);
            BarRenderer br = (BarRenderer) cp.getRenderer();
            br.setShadowVisible(false);
            response.setContentType("image/jpeg");
            ServletOutputStream objSos = response.getOutputStream();
            ChartUtilities.writeChartAsJPEG(objSos, objCht, 720, 480);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
