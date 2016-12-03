/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.jfree.chart.*;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Admin
 */
public class ChartMapServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String[][] aryDat = {
            {"A-08", "150000"},
            {"B-07", "55500"},
            {"B-01", "75000"},
            {"B-03", "83100"},
            {"A-12", "22500"}
        };

        DefaultPieDataset objDpd = new DefaultPieDataset();
        for (int i = 0; i < aryDat.length; i++) {
            objDpd.setValue(aryDat[i][0], Integer.parseInt(aryDat[i][1]));
        }
        JFreeChart objCht = ChartFactory.createPieChart3D("サイトアクセスログ", objDpd, true, true, true);
        // クリッカブル・マップ用のリンクを生成
        PiePlot objPp = (PiePlot) objCht.getPlot();
        objPp.setURLGenerator(new StandardPieURLGenerator("ChartMapServlet"));
        // マップ用に生成された画像を保存するためにダミー・ファイルを生成
        File objFl = File.createTempFile("tips", ".jpg");
        objFl.deleteOnExit();
        // イメージを生成
        ChartRenderingInfo objCri = new ChartRenderingInfo(new StandardEntityCollection());
        ChartUtilities.saveChartAsJPEG(objFl, objCht, 600, 400, objCri);
        // リクエスト属性"map"に<map>タグを含む文字列データをセット
        request.setAttribute("map", ChartUtilities.getImageMap("map", objCri));
        this.getServletContext().getRequestDispatcher("/WEB-INF/chart.jsp").forward(request, response);
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
