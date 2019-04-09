/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package P102_1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import Helper.Helper;
import java.io.BufferedReader;
import java.io.FileFilter;
import java.io.InputStreamReader;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileFilter;
import jcifs.smb.SmbFilenameFilter;

/**
 *
 * @author Administrator
 */
public class ETCRev extends Helper {

    public final String localPath = "D:/data/becl/ETC/ETCRva/";
//    D:\data\becl\ETC\ETCRva
    boolean returnType = true;
    private String fRMTName = "";
    private String fTRXName = "";

    /**
     * @return the fRMTName
     */
    public String getfRMTName() {
        return fRMTName;
    }

    /**
     * @param fRMTName the fRMTName to set
     */
    public void setfRMTName(String fRMTName) {
        this.fRMTName = fRMTName;
    }

    /**
     * @return the fTRXName
     */
    public String getfTRXName() {
        return fTRXName;
    }

    /**
     * @param fTRXName the fTRXName to set
     */
    public void setfTRXName(String fTRXName) {
        this.fTRXName = fTRXName;
    }

    /**
     * @param plazaId
     * @deprecated map the post between BECL and EXAT
     */
    public String getPosId(String plazaId) {
        /*  String posId = "";

         if (plazaId.equals("247")) {
         posId = "266";
         }else if (plazaId.equals("216")) {
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
     *
     * @param fileName
     * @throws IOException
     * @deprecated read and create file
     */
    public void readAndCreateFile(String fileName) throws IOException {
        FileWriter fw = new FileWriter(this.localPath + fileName);//สร้างไฟล์ที่จะเขียนลง
//        SmbFile smbFile = new SmbFile("smb://" + super.newBECLFilePath + "/ETCRva/" + fileName, super.authentication);
        BufferedWriter bw = new BufferedWriter(fw);

        /* อ่านไฟล์ */
//        File fr = new File(super.BECLFilePath + fileName);//ไฟล์ที่จะอ่าน และแก้ไข
//        FileInputStream fisr = new FileInputStream(fr);
//        BufferedInputStream bisr = new BufferedInputStream(fisr);
//        DataInputStream disr = new DataInputStream(bisr);
        SmbFile file = new SmbFile("smb://" + super.newBECLFilePath + fileName, super.authentication);
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));

        String dataInFile;

        while ((dataInFile = br.readLine()) != null) {//อ่านค่าจากไฟล์มาทีละแถว
            String[] sa = dataInFile.split("\t");
            String posId = this.getPosId(sa[0]);
            if (!sa[5].equals("01")) {
                bw.write(posId + "\t");

                for (int i = 1; i < sa.length; i++) {
                    //if (sa[6].equals("02") && sa[7].equals("03")) {//แก้ไข service provider and issuer
                    // sa[7] = "02";
                    //}
                    // if (sa[6].equals("01") && sa[7].equals("02")) {//แก้ไข service provider and issuer
                    //    sa[7] = "01";
                    // }
                    bw.write(sa[i] + "\t");
                }
                bw.newLine();
            }
        }
//        fisr.close();
//        bisr.close();
//        disr.close();
        br.close();
        bw.close();
        fw.close();
    }

//    public void checkFile() {
//        try {
//            SmbFile file = new SmbFile("smb://" + super.newBECLFilePath + "/ETCRva/", super.authentication);
//            SmbFile[] listFiles = file.listFiles(new SmbFilenameFilter() {
//                @Override
//                public boolean accept(SmbFile sf, String fileName) throws SmbException {
////                    boolean isDate = fileName.contains("");
//                    boolean isName = fileName.contains("ET_02_ETC");
//                    return isName;
//                }
//            });
//            String yesterday = getYesterday();
//            String previousDay = getPreviousDate(yesterday);
//            for (int index = 0; index < listFiles.length; index++) {
//                if (listFiles[index].getName().contains(previousDay)) {
//
//                }
////                System.out.println(listFiles[index].lastModified());
//            }
////            break;
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }

    public static void main(String[] arg) {
//        boolean b = "a".contains("aasdbdfa");
//        System.out.println(b);
        ETCRev a = new ETCRev();
//        a.checkFile();
    }
}
