package P115;

import Connect.Connector;
import Helper.Helper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class P115Service extends Helper {

    public StringBuilder importCyber(String fn) {
        StringBuilder txt = new StringBuilder();
        txt.append("<br>---import cyber start...");
        try {
            InputStream reader = retrieveFromFTP("import/DMS", fn);
            if (reader == null) {
                txt.append("<br>--ไม่มีไฟล์จาก (DMS)--");
                reader = retrieveFromFTP("import/DMS_FOR_TEST", fn);
            }

            if (reader == null) {
                txt.append("<br>--ไม่มีการส่งไฟล์--");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(reader));
                String sCurrentLine;
                BufferedWriter bw = null;
                FileWriter fw = null;
                fw = new FileWriter("D:\\pluginRVA\\P115\\tmp\\" + fn.replace(".gw", ".mnl"));
                bw = new BufferedWriter(fw);
                while ((sCurrentLine = br.readLine()) != null) {
                    String[] str = sCurrentLine.split("\t");
                    String stt = str[0];
                    if (stt.equals("403")) {
                        if (str[8].indexOf("1") < 0 && str[8].indexOf("2") < 0 && str[8].indexOf("3") < 0) {//ด่าน 404 
                            stt = "404";
                        }
                    }
                    bw.write(stt + "\t");
                    for (int i = 1; i < str.length; i++) {

                        if (i == 8 && str[i].length() > 10) {
                            bw.write(str[i].substring(0, 9) + "\t");
                            continue;
                        }
                        bw.write(str[i] + "\t");
                    }
                    bw.newLine();
                }
                br.close();
                txt.append("<br>--นำเข้า cyber เรียบร้อย--");
                bw.close();
                fw.close();
                uploadToFTP("D:\\pluginRVA\\P112\\tmp\\", fn.replace(".gw", ".mnl"));
            }
            reader.close();
        } catch (Exception e) {
            txt.append("<br>--Error --<br>").append(e.getMessage());
        } finally {
            //c.close();
            return txt;
        }

    }

    public static void main(String[] args) {
        P115Service p = new P115Service();
    }

    public StringBuilder convertData(String reqDate) throws ParseException, FileNotFoundException, IOException, Exception {
        StringBuilder strBuilder = new StringBuilder();
        String date2Y = DateUtil.DateUtil.convertFormat(reqDate, "MM/dd/yyyy", "yyMMdd");
        String date4Y = DateUtil.DateUtil.convertFormat(reqDate, "MM/dd/yyyy", "yyyyMMdd");
        String fileParentsName = date2Y + ".txt";
        if (!hasParentsFileShift(fileParentsName)) {
            strBuilder.append("<p style=\"color:red;\">ไม่พบไฟล์ " + fileParentsName + "</p>");
            return strBuilder;
        }
        ArrayList<String[]> arrList = readFile(fileParentsName, date4Y);
        if (arrList == null) {
            strBuilder.append("<br>ข้อมูลผิด");
            return strBuilder;
        }
        insertDataToTextBecl(arrList, "dms_text_becl", date4Y);
        selectData(date4Y, "TRF");
        selectData(date4Y, "REV");
        if (createFile("TRF", date4Y)) {
            strBuilder.append("<br>สร้างไฟล์" + "TL_02_TRF_OPN_" + date4Y + ".mnl" + " สำเร็จ");
        }
        if (createFile("REV", date4Y)) {
            strBuilder.append("<br>สร้างไฟล์ " + "TL_02_REV_OPN_" + date4Y + ".mnl" + " สำเร็จ");
        }
        return strBuilder;
    }

    public String checkDataExist(boolean req, String shiftDate) throws Exception {
        Connector connector = new Connect.Connector();
        connector.connect();
        long startTime = System.currentTimeMillis();
        String sql = "SELECT * FROM DMS_TEXT_BECL \n";
        sql += "WHERE SHIFT_DATE = TO_DATE(\'" + shiftDate + "\',\'MM/dd/yyyy\')";
        ResultSet result = connector.executeQuery(sql);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<script type=\"text/javascript\">");
        boolean isDataExist = false;
        while (result.next()) {
            strBuilder.append(" $(\"#result\").html(\"\"); \n");
            strBuilder.append("var isUpdate = confirm(\"มีการโหลดข้อมูลวันที่นี้แล้ว ต้องการโหลดทับข้อมูลเดิมหรือไม่\"); \n");
            strBuilder.append("if(isUpdate){"
                    + "$('#process').attr('disabled', 'disabled');\n"
                    + " $.post(\"/Helper/P115Controller\", {\n"
                    + "date: $(\"#date\").val(),\n"
                    + "update : true \n"
                    + "}, function (res) {\n"
                    + "$('#process').removeAttr('disabled');\n"
                    + "$('#spinner').hide();\n"
                    + "$(\"#result\").html(res);\n"
                    + "});}");
            isDataExist = true;
            break;
        }
        if (!isDataExist) {
            strBuilder.append(" $(\"#result\").html(\"\"); \n");
            strBuilder.append(" $.post(\"/Helper/P115Controller\", {\n"
                    + "date: $(\"#date\").val(),\n"
                    + "update : true \n"
                    + "}, function (res) {\n"
                    + "$('#process').removeAttr('disabled');\n"
                    + "$('#spinner').hide();\n"
                    + "$(\"#result\").html(res);\n"
                    + "}); \n");
        }

        strBuilder.append("</script>");
        connector.close();
        return strBuilder.toString();
    }

    private boolean createFile(String type, String date4Y) throws IOException, Exception {
        String fileName = "";
        String data = "";
        if (type == "TRF") {
            fileName = "TL_02_TRF_OPN_" + date4Y + ".mnl";
            data = getDataTraffic2(date4Y);
        }
        if (type == "REV") {
            fileName = "TL_02_REV_OPN_" + date4Y + ".mnl";
            data = getDataRevenue2(date4Y);
        }
        OutputStream fileOutputStr = createFileTLShift(fileName);
        Writer writer = new OutputStreamWriter(fileOutputStr, "UTF-8");
        writer.write(data);
        writer.close();
        fileOutputStr.close();
//        uploadToFTPFromShift(fileName);
        return true;
    }

    private ArrayList<String[]> readFile(String fileParentsName, String currentDate) throws IOException, ParseException {
        DataInputStream dataInStr = new DataInputStream(getFileShift(fileParentsName));
        BufferedReader bufferReade = new BufferedReader(new InputStreamReader(dataInStr));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String strLine;
        ArrayList<String[]> arrList = new ArrayList<String[]>();
        String nextDate = getNextDate(currentDate);
        while ((strLine = bufferReade.readLine()) != null) {
            String[] dataList = strLine.split(",");
            if (dataList.length != 66) {
                return null;
            }
            int shift = Integer.parseInt(dataList[dataList.length - 1]);
            if (shift < 1 || shift > 3) {
                return null;
            }
            String transactionDate = DateUtil.DateUtil.convertFormat(dataList[5].replaceAll("\"", ""), "dd/MM/yyyy", "yyyyMMdd");
            if (!transactionDate.equals(currentDate) && !transactionDate.equals(nextDate)) {
                return null;
            }
            dataList[0] = "\'" + dataList[0] + "\'";
            dataList[1] = dataList[1].replaceAll("\"", "'");
            dataList[5] = "TO_DATE(" + dataList[5].replaceAll("\"", "'") + ",\'dd/MM/yyyy\')";
            dataList[8] = "TO_DATE(" + dataList[8].replaceAll("\"", "'") + ",\'dd/MM/yyyy\')";
            dataList[10] = "TO_DATE(" + dataList[10].replaceAll("\"", "'") + ",\'dd/MM/yyyy\')";
            arrList.add(dataList);
        }
        return arrList;
    }

    private String getNextDate(String currentDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = format.parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return format.format(calendar.getTime());
    }

    private void insertDataToTextBecl(ArrayList<String[]> arrList, String dbName, String shiftDate) throws Exception {
        Connector connector = new Connect.Connector();
        try {
            Iterator iter = arrList.iterator();
            int index = 1;
            connector.connect();
            String sqlInsert;
            String sqlCheck = "SELECT DISTINCT SHIFT_DATE FROM DMS_TEXT_BECL ";
            sqlCheck += " WHERE  SHIFT_DATE = TO_DATE('" + shiftDate + "',\'yyyyMMdd\')";
            ResultSet sqlResult = connector.executeQuery(sqlCheck);
            while (sqlResult.next()) {
                String sqlDelete = "DELETE DMS_TEXT_BECL";
                sqlDelete += "  WHERE SHIFT_DATE = TO_DATE('" + shiftDate + "',\'yyyyMMdd\')";
                connector.executeQuery(sqlDelete);
                System.out.println("DMS_TEXT_BECL " + sqlDelete);
                break;
            }
            while (iter.hasNext()) {
                sqlInsert = "";
                String[] dataList = (String[]) iter.next();
                StringBuilder strBuilder = new StringBuilder();
                int indexStr = 0;
                for (String data : dataList) {
                    if (indexStr == 0) {
                        strBuilder.append(data);
                    } else {
                        strBuilder.append(", " + data);
                    }
                    indexStr++;
                }
                sqlInsert = "INSERT INTO " + dbName + " VALUES(" + strBuilder.toString().replaceAll("\"", "") + ",TO_DATE('" + shiftDate + "',\'yyyyMMdd\'))";
                System.out.println(index + " | " + sqlInsert);
                connector.addBatch(sqlInsert);
                index++;
            }
            connector.executeBatch();
        } catch (Exception exception) {
            connector.rollback();
            throw exception;
        } finally {
            connector.close();
        }
    }

    private void selectData(String date, String type) throws Exception {
        Connector connector = new Connect.Connector();
        String shiftDate = "TO_DATE('" + date + "',\'yyyyMMdd\')";
        try {
            connector.connect();
            String sql = "";
            if (type.equals("TRF")) {
                sql = "SELECT  T2.PTOL_NO, COLLECTOR, SHIFT, SUM(N_VIO) SN_VIO, SUM(N_DIS) SN_DIS, SUM(N_MIS) SN_MIS,";
                sql += "SUM(AXLE) SAXLE, SUM(LOOP) SLOOP, SUM(DUALOU) SDUALOU, SUM(C0CASH+C0VOUC) SC0CASHVOUC,";
                sql += "SUM(C1CASH) SC1CASH, SUM(C1VOUC) SC1VOUC, SUM(C2CASH) SC2CASH, SUM(C2VOUC) SC2VOUC,";
                sql += "SUM(C3CASH) SC3CASH, SUM(C3VOUC) SC3VOUC, SUM(C4CASH) SC4CASH, SUM(C4VOUC) SC4VOUC,";
                sql += "SUM(C5CASH+C5VOUC) SC5CASHVOUC, SUM(C6CASH) SC6CASH, SUM(C6VOUC) SC6VOUC,";
                sql += "SUM(C7CASH) SC7CASH, SUM(C7VOUC) SC7VOUC, SUM(C8CASH) SC8CASH, SUM(C8VOUC) SC8VOUC,";
                sql += "SUM(C9CASH+C9VOUC) SC9CASHVOUC \n";
                sql += "FROM DMS_TEXT_BECL T1,  DMS_PTOL_BECL T2 \n";
                sql += "WHERE T1.SHIFT_DATE =  " + shiftDate + " ";
                sql += " AND T1.PLAZA NOT IN (402, 403, 404, 405, 406, 408)";
                sql += "AND T1.PLAZA = T2.PLAZA \n";
                sql += "GROUP BY T2.PTOL_NO, COLLECTOR, SHIFT \n";
                sql += "ORDER BY T2.PTOL_NO, COLLECTOR, SHIFT ";
            } else if (type.equals("REV")) {
                sql = "SELECT  T2.PTOL_NO, COLLECTOR, SHIFT, ";
                sql += "SUM(C0CRVE+C0VRVE) SC0CRVEVRVE, SUM(C1CRVE) SC1CRVE, SUM(C1VRVE) SC1VRVE, ";
                sql += "SUM(C2CRVE) SC2CRVE, SUM(C2VRVE) SC2VRVE, SUM(C3CRVE) SC3CRVE, SUM(C3VRVE) SC3VRVE,";
                sql += "SUM(C4CRVE) SC4CRVE, SUM(C4VRVE) SC4VRVE, SUM(C5CRVE) SC5CRVE, SUM(C5VRVE) SC5VRVE,";
                sql += "SUM(C6CRVE) SC6CRVE, SUM(C6VRVE) SC6VRVE, SUM(C7CRVE) SC7CRVE, SUM(C7VRVE) SC7VRVE,";
                sql += "SUM(C8CRVE) SC8CRVE, SUM(C8VRVE) SC8VRVE, SUM(C9CRVE+C9VRVE) SC9CRVEVRVE \n";
                sql += " FROM DMS_TEXT_BECL T1,  DMS_PTOL_BECL T2 \n";
                sql += "WHERE T1.SHIFT_DATE =  " + shiftDate + " ";
                sql += " AND T1.PLAZA NOT IN (402, 403, 404, 405, 406, 408)";
                sql += "AND T1.PLAZA = T2.PLAZA \n";
                sql += "GROUP BY T2.PTOL_NO, COLLECTOR, SHIFT \n";
                sql += "ORDER BY T2.PTOL_NO, COLLECTOR, SHIFT ";
            }
            ResultSet result = connector.executeQuery(sql);
            if (type.equals("TRF")) {
                insertToTraffic2(result, shiftDate);
            } else if (type.equals("REV")) {
                insertToRevenue2(result, shiftDate);
            }
            connector.close();
        } catch (Exception e) {
            connector.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            connector.close();
        }
    }

    private void insertToTraffic2(ResultSet resultSet, String shiftDate) throws Exception {
        Connector connector = new Connect.Connector();
        int index = 1;
        String sqlInsertTRF = "";
        connector.connect();
        String sqlSelect = "SELECT DISTINCT TR_YMD FROM DMS_TRAFFIC2 \n";
        sqlSelect += "WHERE TR_YMD = " + shiftDate;
        ResultSet resultSelect = connector.executeQuery(sqlSelect);
        while (resultSelect.next()) {
            String sqlDeleteTRF = "DELETE DMS_TRAFFIC2";
            sqlDeleteTRF += " WHERE TR_YMD = " + shiftDate;
            connector.executeQuery(sqlDeleteTRF);
            System.out.println("DMS_TRAFFIC2 " + sqlDeleteTRF);
            break;
        }
        while (resultSet.next()) {
            String collector = resultSet.getString("COLLECTOR");
            String ptolNo = resultSet.getString("PTOL_NO");
            String shift = resultSet.getString("SHIFT");
            String lanes = getLanes(collector, ptolNo, shift, shiftDate);
            String shiftName = "พนักงานกะ" + resultSet.getString("SHIFT");
            System.out.print(index + " | ");
            sqlInsertTRF = "INSERT INTO DMS_TRAFFIC2 VALUES( ";
            //1 TR_STATN, TR_YMD, TR_CODE_G
            sqlInsertTRF += resultSet.getString("PTOL_NO") + " , " + shiftDate + " , " + resultSet.getString("COLLECTOR");
            //2 TR_NCODE_PREFIX, TR_NCODE_NAME, TR_NCODE_FNAME
            sqlInsertTRF += " , \',\' , \'" + shiftName + "\' , \',\' ";
            //3 TR_SHIFT, TR_LANE, TR_UAP, TR_NUAP, TR_DISC,
            sqlInsertTRF += " , " + resultSet.getString("SHIFT") + " , \'" + lanes + "\' , " + resultSet.getString("SN_VIO") + " , 0 , " + resultSet.getString("SN_DIS");
            //4 TR_MISR, TR_AXLE1, TR_AXLE2, TR_PRES_LOOP, TR_PASS_LOOP, TR_HEIGHT1, TR_HEIGHT2, TR_OPTICAL, TR_WINDOW, TR_AIR
            sqlInsertTRF += " , " + resultSet.getString("SN_MIS") + ", " + resultSet.getString("SAXLE") + " , 0, " + resultSet.getString("SLOOP") + " , 0, 0, 0, 0, 0, 0 ";
            //5 TR_DUAL, TR_TYPE0_1, TR_TYPE0_2, TR_TYPE0_3, TR_TYPE0_4, TR_TYPE0_5, TR_TYPE0_6, TR_TYPE0_7
            sqlInsertTRF += " , " + resultSet.getString("SDUALOU") + " , 0, 0, 0, 0, " + resultSet.getString("SC0CASHVOUC") + ", 0, 0";
            //6 TR_TYPE1_1, TR_TYPE1_2, TR_TYPE1_3, TR_TYPE1_4, TR_TYPE1_5, TR_TYPE1_6, TR_TYPE1_7
            sqlInsertTRF += " , " + resultSet.getString("SC1CASH") + " , " + resultSet.getString("SC1VOUC") + " , 0, 0, 0, 0, 0";
            //7 TR_TYPE2_1, TR_TYPE2_2, TR_TYPE2_3, TR_TYPE2_4, TR_TYPE2_5, TR_TYPE2_6, TR_TYPE2_7
            sqlInsertTRF += " , " + resultSet.getString("SC2CASH") + " , " + resultSet.getString("SC2VOUC") + " , 0, 0, 0, 0, 0";
            //8 TR_TYPE3_1, TR_TYPE3_2, TR_TYPE3_3, TR_TYPE3_4, TR_TYPE3_5, TR_TYPE3_6, TR_TYPE3_7
            sqlInsertTRF += " , " + resultSet.getString("SC3CASH") + " , " + resultSet.getString("SC3VOUC") + " , 0, 0, 0, 0, 0";
            //9 TR_TYPE4_1, TR_TYPE4_2, TR_TYPE4_3, TR_TYPE4_4, TR_TYPE4_5, TR_TYPE4_6, TR_TYPE4_7
            sqlInsertTRF += " , " + resultSet.getString("SC4CASH") + " , " + resultSet.getString("SC4VOUC") + " , 0, 0, 0, 0, 0";
            //10 TR_TYPE5_1, TR_TYPE5_2, TR_TYPE5_3, TR_TYPE5_4, TR_TYPE5_5, TR_TYPE5_6, TR_TYPE5_7
            sqlInsertTRF += " , 0, 0, 0, 0, " + resultSet.getString("SC5CASHVOUC") + ", 0, 0";
            //11 TR_TYPE6_1, TR_TYPE6_2, TR_TYPE6_3, TR_TYPE6_4, TR_TYPE6_5, TR_TYPE6_6, TR_TYPE6_7
            sqlInsertTRF += " , " + resultSet.getString("SC6CASH") + " , " + resultSet.getString("SC6VOUC") + " , 0, 0, 0, 0, 0";
            //12 TR_TYPE7_1, TR_TYPE7_2, TR_TYPE7_3, TR_TYPE7_4, TR_TYPE7_5, TR_TYPE7_6, TR_TYPE7_7
            sqlInsertTRF += " , " + resultSet.getString("SC7CASH") + " , " + resultSet.getString("SC7VOUC") + " , 0, 0, 0, 0, 0";
            //13 TR_TYPE8_1, TR_TYPE8_2, TR_TYPE8_3, TR_TYPE8_4, TR_TYPE8_5, TR_TYPE8_6, TR_TYPE8_7
            sqlInsertTRF += " , " + resultSet.getString("SC8CASH") + " , " + resultSet.getString("SC8VOUC") + " , 0, 0, 0, 0, 0";
            //14 TR_TYPE9_1, TR_TYPE9_2, TR_TYPE9_3, TR_TYPE9_4, TR_TYPE9_5, TR_TYPE9_6, TR_TYPE9_7
            sqlInsertTRF += " , 0, 0, 0, 0, " + resultSet.getString("SC9CASHVOUC") + ", 0, 0";
            //15 CREATE_DATE, RETRIEVE_DATE
            sqlInsertTRF += " , sysdate, null ";
            sqlInsertTRF += " )";
            System.out.println(sqlInsertTRF);
            connector.addBatch(sqlInsertTRF);
            index++;
        }
        connector.executeBatch();
        connector.close();
    }

    private void insertToRevenue2(ResultSet resultSet, String shiftDate) throws Exception {
        Connector connector = new Connect.Connector();
        connector.connect();
        int index = 1;
        String sqlInsertREV = "";
        String sqlSelect = "SELECT DISTINCT REV_YMD FROM DMS_REVENUE2 \n";
        sqlSelect += "WHERE REV_YMD = " + shiftDate;
        ResultSet resultSelect = connector.executeQuery(sqlSelect);
        while (resultSelect.next()) {
            String sqlDeleteREV = "DELETE DMS_REVENUE2";
            sqlDeleteREV += " WHERE REV_YMD = " + shiftDate;
            connector.executeQuery(sqlDeleteREV);
            System.out.println("DMS_REVENUE2 " + sqlDeleteREV);
            break;
        }
        while (resultSet.next()) {
            String collector = resultSet.getString("COLLECTOR");
            String ptolNo = resultSet.getString("PTOL_NO");
            String shift = resultSet.getString("SHIFT");
            String lanes = getLanes(collector, ptolNo, shift, shiftDate);
            String shiftName = "พนักงานกะ" + resultSet.getString("SHIFT");
            System.out.print(index + " | ");
            sqlInsertREV = "INSERT INTO DMS_REVENUE2 VALUES( ";
            //1 REV_STATN, REV_YMD, REV_CODE_G
            sqlInsertREV += resultSet.getString("PTOL_NO") + " , " + shiftDate + " , " + resultSet.getString("COLLECTOR");
            //2 REV_NCODE_PREFIX, REV_NCODE_NAME, REV_NCODE_FNAME
            sqlInsertREV += " , \',\' , \'" + shiftName + "\' , \',\' ";
            //3 REV_SHIFT, REV_LANE
            sqlInsertREV += " , " + resultSet.getString("SHIFT") + " , \'" + lanes + "\'  ";
            //4  REV_TYPE0_1, REV_TYPE0_2, REV_TYPE0_3, REV_TYPE0_4, REV_TYPE0_5, REV_TYPE0_6, REV_TYPE0_7
            sqlInsertREV += " , 0, 0, 0, 0, " + resultSet.getString("SC0CRVEVRVE") + ", 0, 0";
            //5 REV_TYPE1_1, REV_TYPE1_2, REV_TYPE1_3, REV_TYPE1_4, REV_TYPE1_5, REV_TYPE1_6, REV_TYPE1_7
            sqlInsertREV += " , " + resultSet.getString("SC1CRVE") + " , " + resultSet.getString("SC1VRVE") + " , 0, 0, 0, 0, 0";
            //6 REV_TYPE2_1, REV_TYPE2_2, REV_TYPE2_3, REV_TYPE2_4, REV_TYPE2_5, REV_TYPE2_6, REV_TYPE2_7
            sqlInsertREV += " , " + resultSet.getString("SC2CRVE") + " , " + resultSet.getString("SC2VRVE") + " , 0, 0, 0, 0, 0";
            //7 REV_TYPE3_1, REV_TYPE3_2, REV_TYPE3_3, REV_TYPE3_4, REV_TYPE3_5, REV_TYPE3_6, REV_TYPE3_7
            sqlInsertREV += " , " + resultSet.getString("SC3CRVE") + " , " + resultSet.getString("SC3VRVE") + " , 0, 0, 0, 0, 0";
            //8 REV_TYPE4_1, REV_TYPE4_2, REV_TYPE4_3, REV_TYPE4_4, REV_TYPE4_5, REV_TYPE4_6, REV_TYPE4_7
            sqlInsertREV += " , " + resultSet.getString("SC4CRVE") + " , " + resultSet.getString("SC4VRVE") + " , 0, 0, 0, 0, 0";
            //9 REV_TYPE5_1, REV_TYPE5_2, REV_TYPE5_3, REV_TYPE5_4, REV_TYPE5_5, REV_TYPE5_6, REV_TYPE5_7
            sqlInsertREV += " , " + resultSet.getString("SC5CRVE") + " , " + resultSet.getString("SC5VRVE") + " , 0, 0, 0, 0, 0";
            //10 REV_TYPE6_1, REV_TYPE6_2, REV_TYPE6_3, REV_TYPE6_4, REV_TYPE6_5, REV_TYPE6_6, REV_TYPE6_7
            sqlInsertREV += " , " + resultSet.getString("SC6CRVE") + " , " + resultSet.getString("SC6VRVE") + " , 0, 0, 0, 0, 0";
            //11 REV_TYPE7_1, REV_TYPE7_2, REV_TYPE7_3, REV_TYPE7_4, REV_TYPE7_5, REV_TYPE7_6, REV_TYPE7_7
            sqlInsertREV += " , " + resultSet.getString("SC7CRVE") + " , " + resultSet.getString("SC7VRVE") + " , 0, 0, 0, 0, 0";
            //12 REV_TYPE8_1, REV_TYPE8_2, REV_TYPE8_3, REV_TYPE8_4, REV_TYPE8_5, REV_TYPE8_6, REV_TYPE8_7
            sqlInsertREV += " , " + resultSet.getString("SC8CRVE") + " , " + resultSet.getString("SC8VRVE") + " , 0, 0, 0, 0, 0";
            //13 REV_TYPE9_1, REV_TYPE9_2, REV_TYPE9_3, REV_TYPE9_4, REV_TYPE9_5, REV_TYPE9_6, REV_TYPE9_7
            sqlInsertREV += " , 0, 0, 0, 0, " + resultSet.getString("SC9CRVEVRVE") + ", 0, 0";
            //14  REV_C1, REV_C2, REV_RLF
            sqlInsertREV += ", 0, 0, 0";
            //15 CREATE_DATE, RETRIEVE_DATE, REV_SEND
            sqlInsertREV += " , sysdate, null, 0 ";
            sqlInsertREV += " )";
            System.out.println(sqlInsertREV);
            connector.addBatch(sqlInsertREV);
            index++;
        }
        connector.executeBatch();
        connector.close();
    }

    private String getLanes(String collector, String ptolNo, String shift, String shiftDate) throws Exception {
        Connector connector = new Connect.Connector();
        connector.connect();
        String sqlSelectLane = "SELECT LANE FROM DMS_TEXT_BECL T1, DMS_PTOL_BECL T2 \n";
        sqlSelectLane += " WHERE COLLECTOR = " + collector;
        sqlSelectLane += " AND T2.PTOL_NO = " + ptolNo;
        sqlSelectLane += " AND SHIFT = " + shift;
        sqlSelectLane += " AND SHIFT_DATE = " + shiftDate;
        sqlSelectLane += " AND T1.PLAZA = T2.PLAZA \n";
        sqlSelectLane += " GROUP BY LANE \n";
        sqlSelectLane += " ORDER BY LANE ASC ";
        ResultSet resultLane = connector.executeQuery(sqlSelectLane);
        String lanes = "";
        int laneIndex = 0;
        while (resultLane.next()) {
            if (laneIndex == 0) {
                lanes += resultLane.getString("LANE").trim();
            } else {
                lanes += "," + resultLane.getString("LANE").trim();
            }
            laneIndex++;
        }
        connector.close();
        return lanes;
    }

    private String getDataTraffic2(String shiftDate) throws Exception {
        int size = 90;
        String[] arrIndex = {"12", "19", "26", "33", "40", "47", "54", "61", "68", "75"};
        List<String> listIndex = Arrays.asList(arrIndex);
        StringBuilder strBuilder = new StringBuilder();
        Connector connector = new Connect.Connector();
        try {
            connector.connect();
            String sql = "SELECT TR_STATN, TR_YMD, TR_CODE_G, TR_SHIFT,TR_LANE";
            sql += ", TR_TYPE0_1, TR_TYPE0_2, TR_TYPE0_3, TR_TYPE0_4, TR_TYPE0_5, TR_TYPE0_6, TR_TYPE0_7";
            sql += ", TR_TYPE1_1, TR_TYPE1_2, TR_TYPE1_3, TR_TYPE1_4, TR_TYPE1_5, TR_TYPE1_6, TR_TYPE1_7";
            sql += ", TR_TYPE2_1, TR_TYPE2_2, TR_TYPE2_3, TR_TYPE2_4, TR_TYPE2_5, TR_TYPE2_6, TR_TYPE2_7";
            sql += ", TR_TYPE3_1, TR_TYPE3_2, TR_TYPE3_3, TR_TYPE3_4, TR_TYPE3_5, TR_TYPE3_6, TR_TYPE3_7";
            sql += ", TR_TYPE4_1, TR_TYPE4_2, TR_TYPE4_3, TR_TYPE4_4, TR_TYPE4_5, TR_TYPE4_6, TR_TYPE4_7";
            sql += ", TR_TYPE5_1, TR_TYPE5_2, TR_TYPE5_3, TR_TYPE5_4, TR_TYPE5_5, TR_TYPE5_6, TR_TYPE5_7";
            sql += ", TR_TYPE6_1, TR_TYPE6_2, TR_TYPE6_3, TR_TYPE6_4, TR_TYPE6_5, TR_TYPE6_6, TR_TYPE6_7";
            sql += ", TR_TYPE7_1, TR_TYPE7_2, TR_TYPE7_3, TR_TYPE7_4, TR_TYPE7_5, TR_TYPE7_6, TR_TYPE7_7";
            sql += ", TR_TYPE8_1, TR_TYPE8_2, TR_TYPE8_3, TR_TYPE8_4, TR_TYPE8_5, TR_TYPE8_6, TR_TYPE8_7";
            sql += ", TR_TYPE9_1, TR_TYPE9_2, TR_TYPE9_3, TR_TYPE9_4, TR_TYPE9_5, TR_TYPE9_6, TR_TYPE9_7";
            sql += ", TR_DISC, TR_MISR, TR_AXLE1, TR_AXLE2, TR_PRES_LOOP, TR_PASS_LOOP";
            sql += ", TR_HEIGHT1, TR_HEIGHT2, TR_OPTICAL, TR_WINDOW, TR_AIR, TR_DUAL, TR_UAP, TR_NCODE_NAME, TR_NCODE_FNAME \n";
            sql += " FROM DMS_TRAFFIC2 T1 \n";
            sql += " WHERE ( T1.TR_STATN BETWEEN 200 AND 299) --AND T1.TR_STATN NOT IN (241, 242, 243, 244, 245)";
            sql += " \n AND TR_YMD = TO_DATE (\'" + shiftDate + "\',\'yyyyMMdd\')";
            sql += " AND T1.TR_CODE_G NOT LIKE \'2%\'";
            sql += " ORDER BY T1.TR_STATN, TR_CODE_G, TR_SHIFT";
            ResultSet result = connector.executeQuery(sql);
            while (result.next()) {
                for (int indexColumn = 1; indexColumn <= size; indexColumn++) {
                    if (indexColumn == 1) {
                        strBuilder.append(result.getString(indexColumn).trim());
                    } else if (indexColumn == 2) {
                        strBuilder.append("\t" + DateUtil.DateUtil.convertFormat(result.getString(2).trim(), "yyyy-MM-dd hh:mm:ss", "ddMMyyyy"));
                    } else if (indexColumn == 4) {
                        strBuilder.append("\t" + result.getString(4) + "\tM");
                    } else if (listIndex.contains("" + indexColumn)) {
                        strBuilder.append("\t" + result.getString(indexColumn).trim() + "\t0\t0");
                    } else {
                        strBuilder.append("\t" + result.getString(indexColumn).trim());
                    }
                }
                strBuilder.append("\n");
            }
            connector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strBuilder.toString();
    }

    private String getDataRevenue2(String shiftDate) throws Exception {
        int size = 78;
        String[] arrIndex = {"12", "19", "26", "33", "40", "47", "54", "61", "68", "75"};
        List<String> listIndex = Arrays.asList(arrIndex);
        StringBuilder strBuilder = new StringBuilder();
        Connector connector = new Connect.Connector();
        try {
            connector.connect();
            String sql = "SELECT REV_STATN, REV_YMD, REV_CODE_G, REV_SHIFT,REV_LANE";
            sql += ", REV_TYPE0_1, REV_TYPE0_2, REV_TYPE0_3, REV_TYPE0_4, REV_TYPE0_5, REV_TYPE0_6, REV_TYPE0_7";
            sql += ", REV_TYPE1_1, REV_TYPE1_2, REV_TYPE1_3, REV_TYPE1_4, REV_TYPE1_5, REV_TYPE1_6, REV_TYPE1_7";
            sql += ", REV_TYPE2_1, REV_TYPE2_2, REV_TYPE2_3, REV_TYPE2_4, REV_TYPE2_5, REV_TYPE2_6, REV_TYPE2_7";
            sql += ", REV_TYPE3_1, REV_TYPE3_2, REV_TYPE3_3, REV_TYPE3_4, REV_TYPE3_5, REV_TYPE3_6, REV_TYPE3_7";
            sql += ", REV_TYPE4_1, REV_TYPE4_2, REV_TYPE4_3, REV_TYPE4_4, REV_TYPE4_5, REV_TYPE4_6, REV_TYPE4_7";
            sql += ", REV_TYPE5_1, REV_TYPE5_2, REV_TYPE5_3, REV_TYPE5_4, REV_TYPE5_5, REV_TYPE5_6, REV_TYPE5_7";
            sql += ", REV_TYPE6_1, REV_TYPE6_2, REV_TYPE6_3, REV_TYPE6_4, REV_TYPE6_5, REV_TYPE6_6, REV_TYPE6_7";
            sql += ", REV_TYPE7_1, REV_TYPE7_2, REV_TYPE7_3, REV_TYPE7_4, REV_TYPE7_5, REV_TYPE7_6, REV_TYPE7_7";
            sql += ", REV_TYPE8_1, REV_TYPE8_2, REV_TYPE8_3, REV_TYPE8_4, REV_TYPE8_5, REV_TYPE8_6, REV_TYPE8_7";
            sql += ", REV_TYPE9_1, REV_TYPE9_2, REV_TYPE9_3, REV_TYPE9_4, REV_TYPE9_5, REV_TYPE9_6, REV_TYPE9_7";
            sql += ", REV_RLF, REV_NCODE_NAME, REV_NCODE_FNAME \n";
            sql += " FROM DMS_REVENUE2 T1 \n";
            sql += " WHERE ( T1.REV_STATN BETWEEN 200 AND 299) --AND T1.REV_STATN NOT IN (241, 242, 243, 244, 245)";
            sql += " \n AND REV_YMD = TO_DATE (\'" + shiftDate + "\',\'yyyyMMdd\')";
            sql += " AND T1.REV_CODE_G NOT LIKE \'2%\'";
            sql += " ORDER BY T1.REV_STATN, REV_CODE_G, REV_SHIFT";
            ResultSet result = connector.executeQuery(sql);
            while (result.next()) {
                for (int indexColumn = 1; indexColumn <= size; indexColumn++) {
                    if (indexColumn == 1) {
                        strBuilder.append(result.getString(indexColumn).trim());
                    } else if (indexColumn == 2) {
                        strBuilder.append("\t" + DateUtil.DateUtil.convertFormat(result.getString(2).trim(), "yyyy-MM-dd hh:mm:ss", "ddMMyyyy"));
                    } else if (indexColumn == 4) {
                        strBuilder.append("\t" + result.getString(4) + "\tM");
                    } else if (listIndex.contains("" + indexColumn)) {
                        strBuilder.append("\t" + result.getString(indexColumn).trim() + "\t0\t0");
                    } else {
                        strBuilder.append("\t" + result.getString(indexColumn).trim());
                    }
                }
                strBuilder.append("\n");
            }
            connector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strBuilder.toString();
    }

}
