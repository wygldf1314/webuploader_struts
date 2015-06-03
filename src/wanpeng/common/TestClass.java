/* 
 * @(#)TestClass.java    Created on 2015-4-15
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.common;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-4-15 上午11:00:28 $
 */
public class TestClass {

    @Test
    public void testMap() {
        Map<String, Integer> maps = new HashMap<String, Integer>();
        maps.put("A", 1);
        maps.put("B", 1);
        maps.put("C", 1);
        maps.put("D", 1);
        Set<String> keys = maps.keySet();
        /*
         * Iterator<String> _keys = keys.iterator(); if(_keys.hasNext()) { String key = _keys.next(); }
         */
        if (keys.contains("A")) {
            int value = maps.get("A");
            value++;
            maps.put("A", value);
        }
        System.out.println(maps.get("A"));
    }

    @Test
    public void TestString() {
        String str = "B1";
        String _answer = "C#E";
        String[] _ansers = _answer.split("#");
        // for (String _anser : _ansers) {
        // if (str.contains(_anser)) {
        // String oldStr = str.substring(str.indexOf(_anser)).substring(0,
        // str.substring(str.indexOf(_anser)).indexOf("#"));
        // String valueStr = str.substring(str.indexOf(_anser))
        // .substring(0, str.substring(str.indexOf(_anser)).indexOf("#")).substring(1);
        // int value = Integer.parseInt(valueStr);
        // value++;
        // String newAnswer = _anser + value;
        // str = str.replace(oldStr, newAnswer);
        // }
        // }
        System.out.println(str);
    }

    @Test
    public void testStr() {
        String s = "xinwen32";
        StringBuffer sbuffer = new StringBuffer();
        StringBuffer nbuffer = new StringBuffer();
        try {
            Pattern pattern = Pattern.compile("[0-9]");// [A-Z]");
            Matcher m = null;
            char[] chars = s.toCharArray();
            String str = null;
            for (char c : chars) {
                str = String.valueOf(c);
                m = pattern.matcher(str);
                if (m.matches()) {
                    nbuffer.append(str);
                }
                else {
                    sbuffer.append(str);
                }
            }
            System.out.println(s + "---getStr---" + sbuffer.toString() + "--getNum---" + nbuffer);
        }
        catch (Exception ioEx) {
            ioEx.printStackTrace();
        }
    }

    @Test
    public void testTime() {
        Calendar date = Calendar.getInstance();
        int yearDate = date.get(Calendar.YEAR);
        System.out.println(yearDate);
    }

    @Test
    public void testStringToDate() throws ParseException {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String str = "2011-5-31 14:40:50";
        Date d = sim.parse(str);
        System.out.println(d);
    }

    @Test
    public void testDateToString() throws ParseException {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy");
        Date d = new Date();
        System.out.println(sim.format(d));
    }

    @Test
    public void testStrings() {
        String[] strs = new String[] { "1", "2" };
        String[] _strs = new String[] { "3", "4" };
        String[] strss = Arrays.copyOf(strs, strs.length + _strs.length);
        System.arraycopy(_strs, 0, strss, strs.length, _strs.length);
        for (String str : strss) {
            System.out.println(str);
        }
    }

    @Test
    public void testInt() {
        int a = 2;
        int b = 3;
        String c = accuracy(a, b, 0);
        System.out.println(c);
    }

    public static String accuracy(double num, double total, int scale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        // 可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        // 模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num) + "%";
    }

    @Test
    public void testASCII() {
        int a = 66;
        char c = (char) a;
        String d = Character.toString(c);
        System.out.println(d);
    }

    @Test
    public void testMap1() {
        Map<String, Integer> maps = new HashMap<String, Integer>();
        Map<String, Integer> map1 = new HashMap<String, Integer>();
        Map<String, Integer> map2 = new HashMap<String, Integer>();
        System.out.println(maps.isEmpty());
    }

    @Test
    public void testInteger() {
        Integer value = 7;
        Integer _value = null;
        System.out.println(value.toString());
        System.out.println(_value.toString());
    }

    @Test
    public void handleStrings() {
        String unionCode = "330000000000";
        if (unionCode.length() < 12) {
            System.out.println(unionCode);
        }
        char[] arr = unionCode.toCharArray();
        int lastIndex = 0;
        for (int index = arr.length - 1; index >= 0; index--) {
            if (Integer.valueOf(String.valueOf(arr[index])) != 0) {
                lastIndex = index;
                break;
            }
        }
        System.out.println(unionCode.substring(0, lastIndex + 1));

    }

    @Test
    public void TestMD5() {
        String inStr = "J4FNFMMXNJ779RDGVEPVU5GACCXM7YFD3FS3K4RYZ6ETW5LGEDSWGCDJGGCFRFF3";
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        System.out.println(s);
    }

    @Test
    public void testStringSub() {
        String str = "123456";
        System.out.println(str.substring(0, 2));
    }
}
