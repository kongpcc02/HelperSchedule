///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package P102_1;
//
//import P102_1.ETCRev;
//import java.io.IOException;
//import java.io.PrintWriter;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// *
// * @author siridet_suk
// */
//public class P102_1Controller extends HttpServlet {
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
//        try {
//            String fDate = request.getParameter("fDate");
//            String dateArr[] = fDate.split("/");
//            String dateFind = dateArr[2] + dateArr[0] + dateArr[1];
//            out.println("<br><br>===เริ่มสร้างการขาย===");
//            ETCRev etcRev = new ETCRev();
//            etcRev.setfRMTName("ET_02_ETC_RMT_" + dateFind + ".txt");
//            etcRev.setfTRXName("ET_02_ETC_TRX_" + dateFind + ".txt");
//
//            if (etcRev.hasFile(etcRev.getfRMTName()) && etcRev.hasFile(etcRev.getfTRXName())) {
//                // อ่าน และสร้างไฟล์
//                etcRev.readAndCreateFile(etcRev.getfRMTName());
//                etcRev.readAndCreateFile(etcRev.getfTRXName());
//
//                // อัพโหลด
//                etcRev.uploadToFTP(etcRev.localPath, etcRev.getfRMTName());
//                etcRev.uploadToFTP(etcRev.localPath, etcRev.getfTRXName());
//
//                out.print("<br>====นำเข้ารายได้การให้บริการเรียบร้อย");
//            } else {
//                out.print("<br>====ไม่เจอไฟล์รายได้การให้บริการ");
//            }
//
//            String txtTrx = "TL_02_ETC_OPN_TRF_" + dateFind + ".txt";
//            String txtRev = "TL_02_ETC_OPN_REV_" + dateFind + ".txt";
//            P102_1Service sv = new P102_1Service();
//            out.println("<br><br>===เริ่มสร้างการใช้===");
//            out.println("<br>===create trf file===");
//            out.println("<div style='color:blue'>" + sv.readAndCreateFile(txtTrx) + "</div>");
//            out.println("<br>===create rev file===");
//            out.println("<div style='color:blue'>" + sv.readAndCreateFile(txtRev) + "</div>");
//            out.println("<br><br>===create file success===<hr>");
//
//        } catch (Exception e) {
//            out.println("<br>===Error => " + e);
//
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
