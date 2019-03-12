///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package P102_1;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// *
// * @author beer
// */
//public class EasyPassUsingProcess extends HttpServlet {
//
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        EasyPassUsing easyPassUsing = new EasyPassUsing();
//        try {
//            //File f = new File("\\\\172.16.22.19\\data\\becl\\ETC\\ET_02_ETC_RMT_20100712.txt");
//            //out.print(f.isFile());
//
//            String fDate = request.getParameter("fDate");
//            String dateArr[] = fDate.split("/");
//            String dateFind = dateArr[2] + dateArr[0] + dateArr[1];
//
//            easyPassUsing.setfPROBName("TL_02_ETC_OPN_PROB_" + dateFind + ".txt");
//            easyPassUsing.setfTRFName("TL_02_ETC_OPN_TRF_" + dateFind + ".txt");
//            easyPassUsing.setfREVName("TL_02_ETC_OPN_REV_" + dateFind + ".txt");
//            
//            // ตรวจสอบว่ามีสองไฟล์อยู่ใน server ของ BECL หรือไม่
//            if (easyPassUsing.hasFile(easyPassUsing.getfREVName()) && easyPassUsing.hasFile(easyPassUsing.getfTRFName())) {
//                // อ่าน และสร้างไฟล์
//                //easyPassUsing.readAndCreateFile(easyPassUsing.getfPROBName(), null, null);
//                easyPassUsing.readAndCreateFile(easyPassUsing.getfREVName(), "REV", easyPassUsing.getfTRFName());
//                easyPassUsing.readAndCreateFile(easyPassUsing.getfTRFName(), "TRF", easyPassUsing.getfTRFName());
//
//
//                //.getPc250(easyPassUsing.getfTRFName());
//                // อัพโหลด
//               // easyPassUsing.uploadToFTP(easyPassUsing.localPath, easyPassUsing.getfPROBName());
//                easyPassUsing.uploadToFTP(easyPassUsing.localPath, easyPassUsing.getfREVName());
//                easyPassUsing.uploadToFTP(easyPassUsing.localPath, easyPassUsing.getfTRFName());
//
//                out.print("นำเข้าข้อมูลการใช้เรียบร้อย");
//            } else {
//                out.print("ไม่เจอไฟล์ การใช้บริการ");
//            }
//        } catch (Exception e) {
//            System.out.println("=======================================");
//            System.out.println(e);
//            System.out.println("=======================================");
//
//        } finally {
//            out.close();
//        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP
//     * <code>GET</code> method.
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
//     * Handles the HTTP
//     * <code>POST</code> method.
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
//}
