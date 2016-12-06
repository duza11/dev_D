/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

/**
 *
 * @author Admin
 */
public class PieChartServlet extends HttpServlet {

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
        // グラフ生成用の元データを用意
        String[][] aryDat = {
            {"書籍紹介", "150000"},
            {"Q＆A掲示板", "55500"},
            {"関連サイト", "75000"},
            {"レンタルサーバ", "83100"},
            {"RSSフィード", "22500"}
        };

        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        // 円グラフの基となるデータセットを用意
        DefaultPieDataset objDpd = new DefaultPieDataset();

        // データセットに項目名と値のを順にセット
        for (int i = 0; i < aryDat.length; i++) {
            objDpd.setValue(aryDat[i][0], Integer.parseInt(aryDat[i][1]));
        }

        // 3次元円グラフを生成（第1引数からグラフタイトル、
        // データセット、判例を表示するか、ツールチップを
        // 表示するか、URLを動的に生成するかを指定）
        JFreeChart objCht = ChartFactory.createPieChart3D(
                "サイトアクセスログ", objDpd, true, false, false);

        // バイナリ出力ストリームにJPEG形式で画像を出力
        // 600×400ピクセル）
        response.setContentType("image/jpeg");
        ServletOutputStream objSos = response.getOutputStream();
        ChartUtilities.writeChartAsJPEG(objSos, objCht, 600, 400);
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
