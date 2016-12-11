/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Admin
 */
public class SampleMapServlet extends HttpServlet {

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
        objDcd.addValue(100, "NTTドコモ&category=1", "9月");
        objDcd.addValue(80, "au&category=1", "9月");
        objDcd.addValue(20, "ソフトバンク&category=1", "9月");
        objDcd.addValue(120, "NTTドコモ&category=1", "10月");
        objDcd.addValue(90, "au&category=1", "10月");
        objDcd.addValue(10, "ソフトバンク&category=1", "10月");
        objDcd.addValue(60, "NTTドコモ&category=1", "11月");
        objDcd.addValue(120, "au&category=1", "11月");
        objDcd.addValue(30, "ソフトバンク&category=1", "11月");
        
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        JFreeChart objCht = ChartFactory.createStackedBarChart("", "キャリア", "契約数", objDcd, PlotOrientation.VERTICAL, true, true, true);
        
        CategoryPlot objCp = (CategoryPlot) objCht.getCategoryPlot();
        CategoryItemRenderer renderer = objCp.getRenderer();
        renderer.setItemURLGenerator(new StandardCategoryURLGenerator("a"));
        File objFl = File.createTempFile("tips", "jpg");
        objFl.deleteOnExit();
        ChartRenderingInfo objCri = new ChartRenderingInfo(new StandardEntityCollection());
        ChartUtilities.saveChartAsJPEG(objFl, objCht, 720, 480, objCri);
        request.setAttribute("map", ChartUtilities.getImageMap("map", objCri));
        this.getServletContext().getRequestDispatcher("/WEB-INF/sample.jsp").forward(request, response);
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
