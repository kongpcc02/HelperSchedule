/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author Administrator
 */
public abstract class Helper {

    protected final String BECLFilePath = "D:\\pluginRVA\\BECLFile\\ETC\\";
    protected final String newBECLFilePath = "172.16.22.19/data/becl/ETC/";
    protected final String newShiftBECLFilePath = "172.16.22.19/data/becl/shift/";
    public static final String domain = "172.16.22.19";
    public static final String domainUserName = "administrator";
    public static final String domainPassword = "P@ssw0rd";
    public static final NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(domain, domainUserName, domainPassword);

    /**
     * upload ขึ้นสู่ 1.3.4.3
     *
     * @throws IOException
     * @param fName = ชื่อไฟล์ที่จะ upload
     * @param pName = ชื่อพาธที่จะ upload
     */
    public void uploadToFTP(String pName, String fName) {
        try {

            FTPClient ftpClient = new FTPClient();
            ftpClient.connect("1.3.4.3", 21);
            System.out.println("Reply from FTP : " + ftpClient.getReplyCode());
            if (!ftpClient.login("appint", "apPInt")) {
                System.out.println("เกิดข้อผิดพลาด : รหัสผ่านในการเข้าใช้ FTP ผิด ");
            }
            ftpClient.enterLocalPassiveMode();
            //เข้าไปใน Dir ที่ต้องการ
            ftpClient.changeWorkingDirectory("import/DMS");

            //ถ้าอยากรู้ว่าทันทีที่เข้าไปใน FTP แล้วเราไปอยู่ใน Dir ไหน ก็ต้องใช้ .printWorkingDirectory()
            //System.out.println(ftpClient.printWorkingDirectory());

            /*
             FTPClient ftpClient = new FTPClient();

             //ถ้าต่อไม่ได้ภายใน 10 วิ แสดงว่ามีปัญหาอะไรซักอย่างกับเซิร์ฟเวอร์ หรือเน็ตเวิร์ค
             ftpClient.setConnectTimeout(10000);

             ftpClient.connect("192.168.56.102", 21);
             if (!ftpClient.login("root", "jolojie")) {
             result += "- รหัสผ่านในการเข้าใช้ FTP ผิดพลาด";
             System.out.println(result);
             }
             */
            //*** ถ้าไฟล์ที่อัพโหลด ไม่ใช่ไฟล์ที่ Stream เป็น Text ต้องเซ็ตประเภทของไฟล์เป็น BINARY_FILE_TYPE ด้วย
            //แต่ถ้าเซ็ตเอาไว้ ก็จะสามารถอัพโหลดไฟล์ที่เป็น Text ได้เหมือนกัน
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);

            //File filePDF = new File(serverProcessPath + "WUVPNSetup.pdf");
            //FileInputStream Testfis = new FileInputStream(filePDF);
            //File fileTRF = new File(TRFFilePath);
//                FileInputStream TRFfis = new FileInputStream(TRFFile);
//                //File fileREV = new File(REVFilePath);
//                FileInputStream REVfis = new FileInputStream(REVFile);
            File file_in = new File(pName + fName);
            FileInputStream is = new FileInputStream(file_in);

            //อัพโหลดไฟล์โดยที่ใช้ชื่อแบบไม่ร่วมเลขที่สุ่ม 2 หลักแรก จึงต้อง subString ออกไป
            ftpClient.storeFile(fName, is);

            //System.out.println(ftpClient.storeFile(TRFS.getName(), TRFfis));
            //System.out.println(ftpClient.storeFile(REVS.getName(), REVfis));
            //System.out.println(ftpClient.storeFile("1" + PDFS.getName(), Testfis));
            //Logout ออกจาก FTP Server
            ftpClient.logout();

            //ต้อง Close Stream ก่อน ถึงจะสามารถลบได้ เพราะเหมือนกับว่าเราปิดการ Edit ไฟล์นั้น
            is.close();

//            FtpClient ftpClient = new FtpClient();
//            ftpClient.openServer("1.3.4.3");
//            ftpClient.login("appint", "apPInt");
//            ftpClient.cd("import/DMS/");
//            ftpClient.binary();
//
//            TelnetOutputStream os = ftpClient.put(fName);
//            File file_in = new File(pName + fName);
//
//            FileInputStream is = new FileInputStream(file_in);
//            byte[] bytes = new byte[1024];
//            int c;
//
//            while ((c = is.read(bytes)) != -1) {
//                os.write(bytes, 0, c);
//            }
//            is.close();
//            os.close();
//            ftpClient.closeServer();
        } catch (Exception ex) {
            System.out.println("Helper : " + ex.getMessage());
        }
    }

    public InputStream retrieveFromFTP(String pName, String fName) throws SocketException, IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("1.3.4.3", 21);
        System.out.println("Reply from FTP : " + ftpClient.getReplyCode());
        if (!ftpClient.login("appint", "apPInt")) {
            System.out.println("เกิดข้อผิดพลาด : รหัสผ่านในการเข้าใช้ FTP ผิด ");
        }
        ftpClient.enterLocalPassiveMode();

        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);

        ftpClient.changeWorkingDirectory(pName);

        return ftpClient.retrieveFileStream(fName);
    }

    /**
     * ตรวจสอบไฟล์จากที่อยู่ของ BECL
     *
     * @param fName
     * @return
     */
    public boolean hasFile(String fName) {
        try {
            SmbFile fRMT = new SmbFile("smb://" + this.newBECLFilePath + fName, this.authentication);
            if (fRMT.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Error Exception : " + ex.getMessage());
            return false;
        }
//        File fRMT = new File(this.BECLFilePath + fName);
//
//        if (fRMT.isFile()) {
//            return true;
//        } else {
//            return false;
//        }
    }

    /**
     * แปลง ASI -> Unicode
     *
     * @param ascii
     * @return String
     */
    public String ASCII2Unicode(String ascii) {
        StringBuffer unicode = new StringBuffer(ascii);
        int code;
        for (int i = 0; i < ascii.length(); i++) {
            code = (int) ascii.charAt(i);
            if ((0xA1 <= code) && (code <= 0xFB)) // ตรวจสอบว่าอยู่ในช่วงภาษาไทยของ ASCII หรือไม่
            {
                unicode.setCharAt(i, (char) (code + 0xD60)); // หากใช้แปลงเป็นภาษาไทยในช่วงของ Unicode
            }
        }
        return unicode.toString(); // แปลงข้อมูลกลับไปเป็นแบบ String เพื่อใช้งานต่อไป
    }

    public boolean hasParentsFileShift(String fileName) {
        try {
            SmbFile fRMT = new SmbFile("smb://" + this.newShiftBECLFilePath + fileName, this.authentication);
            if (fRMT.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Error Exception : " + ex.getMessage());
            return false;
        }
    }

    public InputStream getFileShift(String fileName) {
        InputStream inputStr = null;
        try {
            SmbFile fRMT = new SmbFile("smb://" + this.newShiftBECLFilePath + fileName, this.authentication);
            return fRMT.getInputStream();
        } catch (Exception ex) {
            System.out.println("Error Exception : " + ex.getMessage());
            return inputStr;
        }
    }

    public OutputStream createFileTLShift(String fileName) {
        OutputStream outputStr = null;
        try {
            SmbFile fRMT = new SmbFile("smb://" + this.newShiftBECLFilePath + fileName, this.authentication);
            if (!fRMT.exists()) {
                fRMT.createNewFile();
                return fRMT.getOutputStream();
            } else {
                return fRMT.getOutputStream();
            }
        } catch (Exception ex) {
            System.out.println("Error Exception : " + ex.getMessage());
            return outputStr;
        }
    }

    public void uploadToFTPFromShift(String fileName) {
        try {
            long startTime = System.currentTimeMillis();
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect("1.3.4.3", 21);
//            System.out.println("Reply from FTP : " + ftpClient.getReplyCode());
            if (!ftpClient.login("appint", "apPInt")) {
                System.out.println("เกิดข้อผิดพลาด : รหัสผ่านในการเข้าใช้ FTP ผิด ");
            }
            ftpClient.enterLocalPassiveMode();
            //เข้าไปใน Dir ที่ต้องการ
            ftpClient.changeWorkingDirectory("import/DMS");

            //ถ้าอยากรู้ว่าทันทีที่เข้าไปใน FTP แล้วเราไปอยู่ใน Dir ไหน ก็ต้องใช้ .printWorkingDirectory()
            //System.out.println(ftpClient.printWorkingDirectory());

            /*
             FTPClient ftpClient = new FTPClient();

             //ถ้าต่อไม่ได้ภายใน 10 วิ แสดงว่ามีปัญหาอะไรซักอย่างกับเซิร์ฟเวอร์ หรือเน็ตเวิร์ค
             ftpClient.setConnectTimeout(10000);

             ftpClient.connect("192.168.56.102", 21);
             if (!ftpClient.login("root", "jolojie")) {
             result += "- รหัสผ่านในการเข้าใช้ FTP ผิดพลาด";
             System.out.println(result);
             }
             */
            //*** ถ้าไฟล์ที่อัพโหลด ไม่ใช่ไฟล์ที่ Stream เป็น Text ต้องเซ็ตประเภทของไฟล์เป็น BINARY_FILE_TYPE ด้วย
            //แต่ถ้าเซ็ตเอาไว้ ก็จะสามารถอัพโหลดไฟล์ที่เป็น Text ได้เหมือนกัน
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);

            //File filePDF = new File(serverProcessPath + "WUVPNSetup.pdf");
            //FileInputStream Testfis = new FileInputStream(filePDF);
            //File fileTRF = new File(TRFFilePath);
//                FileInputStream TRFfis = new FileInputStream(TRFFile);
//                //File fileREV = new File(REVFilePath);
//                FileInputStream REVfis = new FileInputStream(REVFile);
//            File file_in = new File(pName + fName);
//            InputStream inp = new InputStream(getFileShift(fileName));
//            FileInputStream is = new FileInputStream(file_in);
            //อัพโหลดไฟล์โดยที่ใช้ชื่อแบบไม่ร่วมเลขที่สุ่ม 2 หลักแรก จึงต้อง subString ออกไป
//            ftpClient.storeFile(fName, is);
            ftpClient.setBufferSize(1024000);
            InputStream inputStr = getFileShift(fileName);
            ftpClient.storeFile(fileName, inputStr);
//            ftpClient.
            //System.out.println(ftpClient.storeFile(TRFS.getName(), TRFfis));
            //System.out.println(ftpClient.storeFile(REVS.getName(), REVfis));
            //System.out.println(ftpClient.storeFile("1" + PDFS.getName(), Testfis));
            //Logout ออกจาก FTP Server
            ftpClient.logout();
            ftpClient.disconnect();
//            System.out.println("Reply from FTP : " + ftpClient.getReplyCode());
            inputStr.close();
            //ต้อง Close Stream ก่อน ถึงจะสามารถลบได้ เพราะเหมือนกับว่าเราปิดการ Edit ไฟล์นั้น
//            is.close();
//            FtpClient ftpClient = new FtpClient();
//            ftpClient.openServer("1.3.4.3");
//            ftpClient.login("appint", "apPInt");
//            ftpClient.cd("import/DMS/");
//            ftpClient.binary();
//
//            TelnetOutputStream os = ftpClient.put(fName);
//            File file_in = new File(pName + fName);
//
//            FileInputStream is = new FileInputStream(file_in);
//            byte[] bytes = new byte[1024];
//            int c;
//
//            while ((c = is.read(bytes)) != -1) {
//                os.write(bytes, 0, c);
//            }
//            is.close();
//            os.close();
//            ftpClient.closeServer();
            System.out.println("Upload " + fileName + " success.Use time : " + (System.currentTimeMillis() - startTime)+" ms.");
        } catch (Exception ex) {
            System.out.println("Helper : " + ex.getMessage());
        }
    }
}
