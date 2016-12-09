
import accountbook.DatabaseConnector;
import accountbook.PieChartItem;
import accountbook.RevenueManager;
import accountbook.SpendingManager;
import accountbook.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartServlet extends HttpServlet {

    private static String DB_NAME = "account_book";
    private static String DB_USER = "root";
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

            List<PieChartItem> pieChartItemList = null;
            User user = (User) request.getSession(true).getAttribute("user");

            if (user == null) {
                response.sendRedirect("AccountBookServlet");
            }

            String action = request.getParameter("action");
            String date = request.getParameter("date");

            if (date == null) {
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                date = sdf.format(d);
            }
            
            if (action.equals("show_rev_pie")) {
                pieChartItemList = getRevenuePieCharItem(user, rm, date);
            } else if (action.equals("show_spe_pie")) {
                pieChartItemList = getSpendingPieCharItem(user, sm, date);
            }

            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            // 円グラフの基となるデータセットを用意
            DefaultPieDataset objDpd = new DefaultPieDataset();

            // データセットに項目名と値のを順にセット
            for (PieChartItem pci : pieChartItemList) {
                objDpd.setValue(pci.getKindName(), pci.getPrice());
            }

            // 3次元円グラフを生成（第1引数からグラフタイトル、
            // データセット、判例を表示するか、ツールチップを
            // 表示するか、URLを動的に生成するかを指定）
            JFreeChart objCht = ChartFactory.createPieChart3D(
                    "", objDpd, true, false, false);

            // バイナリ出力ストリームにJPEG形式で画像を出力
            // 600×400ピクセル）
            response.setContentType("image/jpeg");
            ServletOutputStream objSos = response.getOutputStream();
            ChartUtilities.writeChartAsJPEG(objSos, objCht, 600, 400);
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

    private List<PieChartItem> getRevenuePieCharItem(User user, RevenueManager rm, String date) throws Exception {
        return rm.getPieChartItemList(user, date);
    }

    private List<PieChartItem> getSpendingPieCharItem(User user, SpendingManager sm, String date) throws Exception {
        return sm.getPieChartItemList(user, date);
    }
}
