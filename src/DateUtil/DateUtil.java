/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Siridet
 */
public class DateUtil {

    public static String convertFormat(String d, String formatt) throws ParseException {
        SimpleDateFormat sdfSource = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdfSource.parse(d);
        SimpleDateFormat sdfDestination = new SimpleDateFormat(formatt);

        d = sdfDestination.format(date);

        return d;
    }

    public static String convertFormat(String d, String from, String to) throws ParseException {
        SimpleDateFormat sdfSource = new SimpleDateFormat(from);
        Date date = sdfSource.parse(d);
        SimpleDateFormat sdfDestination = new SimpleDateFormat(to);

        d = sdfDestination.format(date);

        return d;
    }

    public static String now(String f) {
        Locale.setDefault(new Locale("en", "US"));  //Import java.util.Locale;
        Calendar cal = Calendar.getInstance(Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat(f);
        return sdf.format(cal.getTime());
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(convertFormat("2012-01-05","yyyy-MM-dd", "dd/MM/yyyy"));
    }
}
