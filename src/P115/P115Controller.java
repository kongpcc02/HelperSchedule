///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package P115;
//
////import P112.P112Service;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.concurrent.TimeUnit;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// *
// * @author siridet_suk
// */
//public class P115Controller extends HttpServlet {
//
//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        long startTime = System.currentTimeMillis();
//        try {
//            out.println("<p><br>==============start===============");
//            String reqDate = request.getParameter("date");
//            boolean isUpdate = Boolean.parseBoolean(request.getParameter("update"));
//            P115Service pService = new P115Service();
//            boolean a = false;
//            if (!isUpdate) {
//                out.println(pService.checkDataExist(isUpdate, reqDate));
//            } else {
//                out.println(pService.convertData(reqDate));
//            }
//            out.println("<br>ใช้เวลา : " + (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - startTime) + 1) + " นาที");
//            out.println("<br>==============success end.===============</p>");
//        } catch (Exception e) {
//            out.println("<br><p> Error ==> <p style=\"color:red\">" + e + "</p>");
//            out.println("<br>Use time : " + (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - startTime) + 1) + " นาที");
//            out.println("<br>==============fail end.===============</p>");
//        } finally {
//            out.close();
//        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
