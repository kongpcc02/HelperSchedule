/**
 *
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package P102_1;

import Helper.Helper;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import jcifs.smb.SmbFile;

/**
 *
 * @author Administrator
 */
public class EasyPassUsing extends Helper {

    public final String localPath = "D:/pluginRVA/EasyPassUsing/read/";
    boolean returnType = true;
    private String fREVName = "";
    private String fPROBName = "";
    private String fTRFName = "";
//    public static String path = "172.16.22.19/data/becl";
    private int pc211 = 0;

    /**
     * @return the fREVName
     */
    public String getfREVName() {
        return fREVName;
    }

    /**
     * @param fREVName the fREVName to set
     */
    public void setfREVName(String fREVName) {
        this.fREVName = fREVName;
    }

    /**
     * @return the fPROBName
     */
    public String getfPROBName() {
        return fPROBName;
    }

    /**
     * @param fPROBName the fPROBName to set
     */
    public void setfPROBName(String fPROBName) {
        this.fPROBName = fPROBName;
    }

    /**
     * @return the fTRFName
     */
    public String getfTRFName() {
        return fTRFName;
    }

    /**
     * @param fTRFName the fTRFName to set
     */
    public void setfTRFName(String fTRFName) {
        this.fTRFName = fTRFName;
    }

    /**
     *
     * @param plazaId
     * @return post id
     * @deprecated map post id between BECL and EXAT
     */
    public String getPosId(String plazaId) {
        /*  String posId = "";

         if (plazaId.equals("247")) {
         posId = "266";
         }else  if (plazaId.equals("216")) {
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

    /**
     * อ่านไฟล์จากต้นฉบับ (BECL) แล้วนำมาจัดรูปแบบ format ใหม่เพื่อเตรียม upload
     *
     * @param fileName
     * @throws IOException
     */
    public void readAndCreateFile(String fileName, String fileTypeKey, String f) throws IOException {
        FileWriter fw = new FileWriter(this.localPath + fileName);//สร้างไฟล์ที่จะเขียนลง
        BufferedWriter bw = new BufferedWriter(fw);

        /*
         * อ่านไฟล์
         */
//        File fr = new File(super.BECLFilePath + fileName);//ไฟล์ที่จะอ่าน และแก้ไข
//        FileInputStream fisr = new FileInputStream(fr);
//        BufferedInputStream bisr = new BufferedInputStream(fisr);
//        DataInputStream disr = new DataInputStream(bisr);
        SmbFile file = new SmbFile("smb://" + super.newBECLFilePath + fileName, super.authentication);
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));

        String dataInFile;

        //อ่านค่าจากไฟล์มาทีละแถว
        while ((dataInFile = br.readLine()) != null) {
            String[] sa = dataInFile.split("\t");
            String posId = this.getPosId(sa[0]);

            //ปิดประเภทการผ่านทาง = 25
            if (!sa[4].equals("25")) {
                bw.write(posId + "\t");

                for (int i = 1; i < sa.length; i++) {
                    // if (sa[5].equals("01") && sa[6].equals("02")) {
                    //  sa[6] = "01";
                    //}
                    // if (sa[5].equals("02") && sa[6].equals("03")) {
                    //    sa[6] = "02";
                    // }
                    bw.write(sa[i] + "\t");

                }
                bw.newLine();
            }
        }
        if (fileTypeKey != null) {
            //bw.write(getPc250(f).get(fileTypeKey).toString());
        }
//        fisr.close();
//        bisr.close();
//        disr.close();
        br.close();
        bw.close();
        fw.close();
    }

    public Map getPc250(String trfFileName) throws IOException {
        String ddmmyyyy = "";
        int tt = 0;
        FileWriter fw = new FileWriter(this.localPath + trfFileName);//สร้างไฟล์ที่จะเขียนลง

        SmbFile file = new SmbFile("smb://" + super.newBECLFilePath + trfFileName, super.authentication);
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));

//        File fr = new File(super.BECLFilePath + trfFileName);//ไฟล์ที่จะอ่าน และแก้ไข
//        FileInputStream fisr = new FileInputStream(fr);
//        BufferedInputStream bisr = new BufferedInputStream(fisr);
//        DataInputStream disr = new DataInputStream(bisr);
        String dataInFile;
        Map m = new HashMap();
        while ((dataInFile = br.readLine()) != null) {
            String[] sa = dataInFile.split("\t");
            String posId = this.getPosId(sa[0]);

            if (sa[4].equals("01") && sa[0].equals("211")) {
                tt += Integer.parseInt(sa[9]);
                ddmmyyyy = sa[1];
            }

        }
        m.put("TRF", "250\t" + ddmmyyyy + "\t9999999999\t1\t01\t01\t01\t1\t0\t" + tt + "\t0\t0");
        m.put("REV", "250\t" + ddmmyyyy + "\t9999999999\t1\t01\t01\t01\t1\t0\t" + (tt * 15) + "\t0\t0");
//        fisr.close();
//        bisr.close();
//        disr.close();
        br.close();
        fw.close();
        return m;

    }
}
