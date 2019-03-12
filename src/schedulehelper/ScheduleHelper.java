/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulehelper;

import Connect.Connector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author chonpisit_klo
 */
public class ScheduleHelper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Connector conn = new Connect.Connector();
        try {
//            System.out.println(System.getProperty("java.class.path"));
            ScheduleService s = new ScheduleService();
            s.P115();
//            s.createFile();
//            s.add();
//            conn.connect();
//            System.out.println("Connect DB server");
        } catch (Exception ex) {
//            Logger.getLogger(ScheduleHelper.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("Cannot connect DB server");
            ex.printStackTrace();
        }
    }

}
