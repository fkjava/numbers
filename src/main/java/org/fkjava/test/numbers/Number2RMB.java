package org.fkjava.test.numbers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Number2RMB {

    public String convert(double num) {

        // 1.把数字转换为String
        //BigDecimal bigDecimal = BigDecimal.valueOf(num);
        String number = new DecimalFormat("#").format(num);
        System.out.println(number);

        // 2.把小数点分开两个部分
        String integer;//整数
        String decimal;//小数
        if (number.indexOf(".") > 0) {
            integer = number.substring(0, number.indexOf("."));
            decimal = number.substring(number.indexOf(".") + 1);
        } else {
            integer = number;
            decimal = "";
        }

        // 3.计算整数部分，每4位一段
        String[] unitPrefix = new String[]{"仟", "佰", "拾", ""};
        String[] unitSuffix = new String[]{"京", "兆", "亿", "萬", ""};
        String[] chineseNumbers = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        // 按照4位一段分成4个字符子串
        String[] sections = new String[integer.length() % 4 == 0 ? integer.length() / 4 : integer.length() / 4 + 1];
        if (integer.length() > 4) {
            for (int i = sections.length - 1; integer.length() > 0; i--) {
                int start = integer.length() - 4;
                int end = integer.length();
                if (start < 0) {
                    start = 0;
                }

                String section = integer.substring(start, end);
                integer = integer.substring(0, start);
                sections[i] = section;
            }
        } else {
            sections[0] = integer;
        }
        StringBuilder result = new StringBuilder();
        // 遍历每一段，从最左边开始遍历
        for (int i = sections.length - 1, unitSuffixPosition = unitSuffix.length - 1;
             i >= 0;
             i--, unitSuffixPosition--) {
            String section = sections[i];
            char[] cs = section.toCharArray();
            // 遍历一段的四个字符，从最左边开始
            for (int j = cs.length - 1, unitPrefixPosition = unitPrefix.length - 1;
                 j >= 0;
                 j--, unitPrefixPosition--) {
                char c = cs[j];
                int x = c - 48;
                // 把字符减去48以后，得到数字0~9，利用此数字去chineseNumbers数组中取中文数字
                String chineseNumber = chineseNumbers[x];
                // 由于单位前缀，是把最小单位放到最右边的，利用单位的位置获取出来即可
                String unit = unitPrefix[unitPrefixPosition];
                result.insert(0, unitSuffix[unitSuffixPosition])
                        .insert(0, unit)
                        .insert(0, chineseNumber);
            }
        }
        //System.out.println(result);

        result.append("圆");


        // 4.处理小数部分
        char[] cs = decimal.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];
            int x = c - 48;
            result.append(chineseNumbers[x]);
            if (i == 0) {
                result.append("角");
            } else if (i == 1) {
                result.append("分");
            }
        }

        // 5.把连续的【零】去掉，改为一个零即可
        String yuan = result.toString();
        // 替换4次：第一次和第二次是为了把连续的【零佰】之类的去掉，第三次则把多个【零】改为一个【零】。
        // 第四次，把【零圆】替换成【圆】
        yuan = yuan.replaceAll("(零[萬亿兆京仟佰拾])+", "零");
        yuan = yuan.replaceAll("(零[萬亿兆京仟佰拾])+", "零");
        yuan = yuan.replaceAll("零{2,}", "零");
        yuan = yuan.replaceAll("零圆", "圆");
        //System.out.println(yuan);


        return yuan;
    }

    public static void main(String[] args) {

        Number2RMB n2r = new Number2RMB();

        System.out.println(n2r.convert(1.00));
        System.out.println(n2r.convert(1.01));
        System.out.println(n2r.convert(1.11));
        System.out.println(n2r.convert(1.10));
        System.out.println(n2r.convert(10.00));
        System.out.println(n2r.convert(100.00));
        System.out.println(n2r.convert(100.01));
        System.out.println(n2r.convert(100.10));
        System.out.println(n2r.convert(101.00));
        System.out.println(n2r.convert(1001.00));
        System.out.println(n2r.convert(10001.00));
        System.out.println(n2r.convert(100001.00));
        System.out.println(n2r.convert(1000001.00));
        System.out.println(n2r.convert(10000001.00));
        System.out.println(n2r.convert(1001001001.00));
        System.out.println(n2r.convert(100100100100.00));
        System.out.println(n2r.convert(100100100001.00));
        System.out.println(n2r.convert(10010010000001.00));
        System.out.println(n2r.convert(100100100001.00));
        System.out.println(n2r.convert(100100000100001.00));
    }
}
