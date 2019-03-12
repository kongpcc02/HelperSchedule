/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Connect;

/**
 *
 * @author bundit_put
 */
public class ConfigGateway {
//    private static String host = "172.20.1.208";
//    private static String user = "sa";
//    private static String pass = "HRgrF]";
//    private static String dbname = "EXATDB";    
    
    private static String host = "172.20.1.195";
    private static String user = "set_user";
    private static String pass = "set_pass";
    private static String dbname = "exatdb";

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        ConfigGateway.host = host;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        ConfigGateway.user = user;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        ConfigGateway.pass = pass;
    }

    public static String getDbname() {
        return dbname;
    }

    public static void setDbname(String dbname) {
        ConfigGateway.dbname = dbname;
    }
}
