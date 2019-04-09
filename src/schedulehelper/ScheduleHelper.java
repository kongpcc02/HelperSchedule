/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulehelper;

import Connect.Connector;

/**
 *
 * @author chonpisit_klo
 */
public class ScheduleHelper {
    public static void main(String[] args) {
        try {
            ScheduleService scheduleService = new ScheduleService();
            scheduleService.P102();
            scheduleService.P115();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
