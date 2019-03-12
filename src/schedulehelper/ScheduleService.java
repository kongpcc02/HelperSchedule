/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulehelper;

import Connect.Connector;
import P102_1.ETCRev;
import P115.P115Service;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author chonpisit_klo
 */
public class ScheduleService {

    public String getLastShiftDate() {
        Connector connector = new Connect.Connector();
        try {
            connector.connect();
            String sql = "SELECT DISTINCT SHIFT_DATE FROM DMS_TEXT_BECL WHERE ROWNUM = 1 ORDER BY SHIFT_DATE DESC";
            ResultSet res = connector.executeQuery(sql);
            String date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            while (res.next()) {
                date = sdf.format(res.getDate("SHIFT_DATE"));
                break;
            }
            return date;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            connector.close();
        }
    }

    public void add() {
        Connector connector = new Connect.Connector();
        try {
            connector.connect();
            String sql = "INSERT INTO KONG VALUES(\'11\')";
            connector.addBatch(sql);
            connector.executeBatch();
            System.out.println("Insert success");
        } catch (Exception ex) {
//            System.out.println(ex);
            ex.printStackTrace();

        } finally {
            connector.close();
        }
    }

    public static void main(String[] arg) {
        ScheduleService s = new ScheduleService();
        s.P115();
//        s.getLastShiftDate();
//        System.out.println(s.getYesterDay(C));
//        ;
//        int i = 1;
//        while (i <= 20) {
//            System.out.println(getDate(i));
//            i++;
//        }
    }

    public static String getDate() {
        try {
            String currentDate = "20191220";
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date date = format.parse(currentDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, (-1 * 1));
            return format.format(calendar.getTime());
        } catch (Exception ex) {

            return "";
        }

    }

    public void createFile() throws IOException {
        File f = new File("./test.txt");
        if (!f.exists()) {
            f.createNewFile();
        }
    }

    public void P102() throws Exception {

//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
        try {
            String fDate = "";
            String dateArr[] = fDate.split("/");
            String dateFind = dateArr[2] + dateArr[0] + dateArr[1];
//            out.println("<br><br>===เริ่มสร้างการขาย===");
            ETCRev etcRev = new ETCRev();
            etcRev.setfRMTName("ET_02_ETC_RMT_" + dateFind + ".txt");
            etcRev.setfTRXName("ET_02_ETC_TRX_" + dateFind + ".txt");

            if (etcRev.hasFile(etcRev.getfRMTName()) && etcRev.hasFile(etcRev.getfTRXName())) {
                // อ่าน และสร้างไฟล์
                etcRev.readAndCreateFile(etcRev.getfRMTName());
                etcRev.readAndCreateFile(etcRev.getfTRXName());

                // อัพโหลด
                etcRev.uploadToFTP(etcRev.localPath, etcRev.getfRMTName());
                etcRev.uploadToFTP(etcRev.localPath, etcRev.getfTRXName());

//                out.print("<br>====นำเข้ารายได้การให้บริการเรียบร้อย");
            } else {
//                out.print("<br>====ไม่เจอไฟล์รายได้การให้บริการ");
            }

            String txtTrx = "TL_02_ETC_OPN_TRF_" + dateFind + ".txt";
            String txtRev = "TL_02_ETC_OPN_REV_" + dateFind + ".txt";
//            P102_1Service sv = new P102_1.P102_1Service();
//            out.println("<br><br>===เริ่มสร้างการใช้===");
//            out.println("<br>===create trf file===");
//            out.println("<div style='color:blue'>" + sv.readAndCreateFile(txtTrx) + "</div>");
//            out.println("<br>===create rev file===");
//            out.println("<div style='color:blue'>" + sv.readAndCreateFile(txtRev) + "</div>");
//            out.println("<br><br>===create file success===<hr>");

        } catch (Exception e) {
//            out.println("<br>===Error => " + e);
            throw e;
        } finally {
//            out.close();
        }
    }

    public void P115() {
        System.out.println("Start P115");
        try {
            System.out.println("Yester Day : " + getYesterDay());
            System.out.println("Last Shift Date : " + getLastShiftDate());
//            P115Service p115Service = new P115Service();
//            p115Service.convertData("01/01/2018");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("End P115");
    }

    public Date getYesterDay() {
        try {
            Date toDay = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            Date toDay = sdf.parse("20190311");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toDay);
            
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            System.out.println(calendar.getTime());
//            System.out.println(toDay);
            return calendar.getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
