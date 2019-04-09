/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HttpClient;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author chonpisit_klo
 */
public class HttpClient {

    private URL url;

    public HttpClient(String reqUrl) {
        try {
            url = new URL(reqUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getUrl() {
        return url.toString();
    }

    public void getConnection() {
        try {
            HttpURLConnection urlConection = (HttpURLConnection) url.openConnection();
            urlConection.setRequestMethod("GET");
            System.out.println("Response | " + urlConection.getResponseMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void postConnection() {
        try {
            HttpURLConnection urlConection = (HttpURLConnection) url.openConnection();
            urlConection.setRequestMethod("POST");
            System.out.println("Response | " + urlConection.getResponseMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] a) {
        HttpClient aa = new HttpClient("http://172.20.35.68:8081/Helper/P115Schedule");
        aa.getConnection();
    }

}
