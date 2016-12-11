/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.PrintWriter;
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

/**
 *
 * @author Admin
 */
public class SampleServlet extends HttpServlet {

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
        DefaultCategoryDataset objDcd = new DefaultCategoryDataset();
        objDcd.addValue(100, "NTTドコモ", "9月");
        objDcd.addValue(80, "au", "9月");
        objDcd.addValue(20, "ソフトバンク", "9月");
        objDcd.addValue(120, "NTTドコモ", "10月");
        objDcd.addValue(90, "au", "10月");
        objDcd.addValue(10, "ソフトバンク", "10月");
        objDcd.addValue(60, "NTTドコモ", "11月");
        objDcd.addValue(120, "au", "11月");
        objDcd.addValue(30, "ソフトバンク", "11月");
        
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        JFreeChart objCht = ChartFactory.createStackedBarChart("", "キャリア", "契約数", objDcd, PlotOrientation.VERTICAL, true, false, false);
        
        response.setContentType("image/jpeg");
        ServletOutputStream objSos = response.getOutputStream();
        ChartUtilities.writeChartAsJPEG(objSos, objCht, 720, 480);
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