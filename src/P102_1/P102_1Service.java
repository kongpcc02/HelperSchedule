/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P102_1;

import Helper.Helper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class P102_1Service extends Helper {

    public String getStt(String plazaId) {
        /*  String posId = "";

         if (plazaId.equals("247")) {
         posId = "266";
         } else if (plazaId.equals("216")) {
         posId = "246";
         } else if (plazaId.equals("217")) {
         posId = "247";
         } else if (plazaId.equals("241")) {
         posId = "230";
         } else if (plazaId.equals("242")) {
         posId = "262";
         } else if (plazaId.equals("243")) {
         posId = "263";
         } else if (plazaId.equals("244")) {
         posId = "264";
         } else if (plazaId.equals("245")) {
         posId = "251";
         } else if (plazaId.equals("246")) {
         posId = "265";
         } else if (plazaId.equals("213")) {
         posId = "248";
         } else if (plazaId.equals("214")) {
         posId = "249";
         } else {
         posId = plazaId;
         }
         return posId;*/
        return plazaId;
    }

    public void readAndCreateFile(String fileName) throws IOException {
        InputStream reader = retrieveFromFTP("import/DMS", fileName);
//        StringBuilder txt = new StringBuilder();
        if (reader == null) {
            System.out.println("File not found");
            reader = retrieveFromFTP("import/DMS_FOR_TEST", fileName);
        }
        if (reader == null) {
            System.out.println("File not found");
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(reader));
            String sCurrentLine;
            BufferedWriter bw = null;
            FileWriter fw = null;
            fw = new FileWriter("D:\\data\\becl\\pluginRVA\\P102_1\\" + fileName.replace(".txt", ".mnl"));
            bw = new BufferedWriter(fw);

            while ((sCurrentLine = br.readLine()) != null) {
                String[] sa = sCurrentLine.split("\t");
                String posId = getStt(sa[0]);
                bw.write(posId + "\t");
                for (int i = 1; i < sa.length; i++) {
                    bw.write(sa[i] + "\t");
                }
                bw.newLine();

            }
            br.close();
            bw.close();
            fw.close();
            reader.close();
//            uploadToFTP("D:\\data\\becl\\pluginRVA\\P102_1\\", fileName.replace(".txt", ".mnl"));
        }

//        return txt;
    }

    public static void main(String[] args) throws IOException {
        P102_1Service sv = new P102_1Service();
        sv.readAndCreateFile("TL_02_ETC_OPN_TRF_20170225.txt");
        sv.readAndCreateFile("TL_02_ETC_OPN_REV_20170225.txt");
    }
}
