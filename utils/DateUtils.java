package utils;

/**
 * @Description: 时间格式转换
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date日期公共类
 *
 *
 */
public class DateUtils {

   private static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("YYYY-MM-DD");
    private static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("YYY.MM.DD");
    private static SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("YYYY/MM/DD");


    /**
     * 字符串日期转化为“yyy-MM-dd”格式的Date日期类型 如果字符串为null则返回null
     *
     * @param dateStr
     * @return
     */
    public static Date formateDate(String dateStr) {
        if (dateStr == null || "".equals(dateStr.trim()))
            return null;

        dateStr=dateStr.trim();

        try {
            Date date = simpleDateFormat1.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将YYYY.MM.DD格式的日期转换为YYYY-MM-DD格式
     * @param dateStr
     * @return
     */
    public static String formateDateStr2(String dateStr) {
        String  str=dateStr.trim();
        if (str == null || "".equals(str))
            return null;
        try {
            Date date = simpleDateFormat2.parse(str);
            String newD = simpleDateFormat1.format(date);
            return newD;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将YYYY/MM/DD格式的日期转换为YYYY-MM-DD格式
     * @param dateStr
     * @return
     */
    public static String formateDateStr3(String dateStr) {
        String  str=dateStr.trim();
        if (str == null || "".equals(str))
            return null;

        try {
            Date date = simpleDateFormat3.parse(str);
            String newD = simpleDateFormat1.format(date);
            return newD;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断时间是否为YYYY-MM-DD 或 YYYY.MM.DD 或 YYYY/MM/DD格式
     * @param sDate
     * @return
     */
    public static boolean isValidDate(String sDate) {
        String datePattern1 = "\\d{4}[\\-\\.\\/\\s]\\d{2}[\\-\\.\\/\\s]\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-\\.\\/\\s]?((((0?[13578])|(1[02]))[\\-\\.\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\.\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\.\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\.\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\.\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\.\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\.\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            }
            else {
                return false;
            }
        }
        return false;
    }
    /**
     * 判断时间是否为 YYYY-MM-DD 格式
     * @param sDate
     * @return
     */
    public static boolean isValidDate1(String sDate) {
        String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            }
            else {
                return false;
            }
        }
        return false;
    }
    /**
     * 判断时间是否为 YYYY.MM.DD 格式
     * @param sDate
     * @return
     */
    public static boolean isValidDate2(String sDate) {
        String datePattern1 = "\\d{4}\\.\\d{2}\\.\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-\\.\\/\\s]?((((0?[13578])|(1[02]))[\\-\\.\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\.\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\.\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\.\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\.\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\.\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\.\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            }
            else {
                return false;
            }
        }
        return false;
    }
    /**
     * 判断时间是否为 YYYY/MM/DD格式
     * @param sDate
     * @return
     */
    public static boolean isValidDate3(String sDate) {
        String datePattern1 = "\\d{4}/\\d{2}/\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-\\.\\/\\s]?((((0?[13578])|(1[02]))[\\-\\.\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\.\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\.\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\.\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\.\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\.\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\.\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            }
            else {
                return false;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String time1="2012-12-21";
        String time2="2012.12.21";
        String time3="2012/12/21";


 /*       if(DateUtils.isValidDate(time1)&&DateUtils.isValidDate(time2)&&DateUtils.isValidDate(time3))
           System.out.println("Yes");
        else{
            System.out.println("No");
        }

        if(DateUtils.isValidDate2(time2))
            System.out.println("Yes");
        else{
            System.out.println("No");
        }*/
        if(DateUtils.isValidDate2(time3))
            System.out.println("Yes");
        else{
            System.out.println("No");
        }
    }

}
