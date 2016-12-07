
import accountbook.BarChartItem;
import accountbook.DatabaseConnector;
import accountbook.RevenueManager;
import accountbook.SpendingManager;
import accountbook.User;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChartServlet extends HttpServlet {

    private static String DB_NAME = "account_book";               // DB名
    private static String DB_USER = "root";                  // DBのユーザ名
    private static String DB_PASS = "duza11";

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

            if (user == null) {
                response.sendRedirect("AccountBookServlet");
            }

            String action = request.getParameter("action");
            int kind = Integer.parseInt(request.getParameter("category"));
            String date = request.getParameter("date");

            if (action.equals("show_rev_bar")) {
                barChartItemList = getRevenueBarCharItem(user, rm, kind, date);
            } else if (action.equals("show_spe_bar")) {
                barChartItemList = getSpendingBarCharItem(user, sm, kind, date);
            }

            DefaultCategoryDataset objDcd = new DefaultCategoryDataset();
            for (BarChartItem bci : barChartItemList) {
                objDcd.addValue(bci.getPrice(), bci.getKind(), bci.getMonth());
            }

            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            JFreeChart objCht = ChartFactory.createBarChart("", "月", "金額", objDcd, PlotOrientation.VERTICAL, true, false, false);

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

    private List<BarChartItem> getRevenueBarCharItem(User user, RevenueManager rm, int kind, String date) throws Exception {
        return rm.getBarChartItemList(user, kind, date);
    }

    private List<BarChartItem> getSpendingBarCharItem(User user, SpendingManager sm, int kind, String date) throws Exception {
        return sm.getBarChartItemList(user, kind, date);
    }
}