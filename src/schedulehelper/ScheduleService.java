/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulehelper;

import Connect.Connector;
import P102_1.ETCRev;
import P115.P115Service;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import Helper.Helper;
import HttpClient.HttpClient;
import P102_1.P102_1Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
//import javax.

/**
 *
 * @author chonpisit_klo
 */
public class ScheduleService extends Helper {

    public String urlHelper = "http://172.20.1.106/Helper/P115Controller";

    //Request date MM/dd/yyyy 
//    private String getLastShiftDate() {
//        Connector connector = new Connect.Connector();
//        try {
//            connector.connect();
//            String sql = "SELECT SHIFT_DATE FROM (";
//            sql += "SELECT DISTINCT SHIFT_DATE FROM DMS_TEXT_BECL  ORDER BY SHIFT_DATE DESC )";
//            sql += "WHERE ROWNUM = 1";
//            ResultSet res = connector.executeQuery(sql);
//            String date = null;
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            while (res.next()) {
//                date = sdf.format(res.getDate("SHIFT_DATE"));
//                break;
//            }
////            System.out.println("Last shift date " + date);
//            return date;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return "";
//        } finally {
//            connector.close();
//        }
//    }

    public static void main(String[] arg) {
        try {
            ScheduleService s = new ScheduleService();
            s.P102();
            s.P115();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void P102() throws Exception {
        System.out.println("Start P102");
        try{
            //http://172.20.1.106
            //172.20.35.68:8081/Helper/P115Schedule
            HttpClient httpClient = new HttpClient("http://172.20.1.106/Helper/P102_1Schedule");
            httpClient.getConnection();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println("End P102");
    }

    public void P115() {
        System.out.println("Start P115");
        try {
            HttpClient httpClient = new HttpClient("http://172.20.1.106/Helper/P115Schedule");
            httpClient.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("End P115");
    }

//    private static String getNextDate(String currentDate) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        Date date = sdf.parse(currentDate);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.DAY_OF_YEAR, 1);
//        return sdf.format(calendar.getTime());
//    }

//    private boolean compareBeforeDate(String lastDate, String yesterday) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        Date lastShiftDate = sdf.parse(lastDate);
//        Date yesterday1 = sdf.parse(yesterday);
//        if (lastShiftDate.before(yesterday1)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private String getLastP102Date() {
//        Connector connector = new Connect.Connector();
//        try {
//            connector.connect();
//            String sql = "SELECT P102_DATE FROM (SELECT DISTINCT P102_DATE FROM P102_TMP ORDER BY P102_DATE DESC) WHERE ROWNUM = 1 ";
//            ResultSet res = connector.executeQuery(sql);
//            String date = null;
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            while (res.next()) {
//                date = sdf.format(res.getDate("P102_DATE"));
//                break;
//            }
//            return date;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return "";
//        } finally {
//            connector.close();
//        }
//    }
}
