package testjava;

import java.util.Scanner;

public class BigNumMuti {
    public static void main(String[] args) {
        Scanner can = new Scanner(System.in);

        try {
            String line1 = can.nextLine().trim();
            String line2 = can.nextLine().trim();
            String result = multiBigInteger(line1, line2);
            System.out.println(result);
        } finally {
            can.close();
        }
    }

    public static String multiBigInteger(String num1, String num2) {
        //字符串的长度
        int num1Len = num1.length();        //num1的长度
        int num2Len = num2.length();       //num2的长度

        int i, j, k;                       //循环计数器
        int res;                           //每次一位相乘/相加的结果
        int carry = 0;                     //进位
        int offset = 0;                    //加法的偏移位

        //每次相乘的结果
        int tempResLen = num1Len;                   //每次相乘结果的最大长度 ,每次num1乘以num2每一位的结果最大长度是num1Len+1,由于下标从0开始，所以减一后约去1,只剩num1Len
        char[] tempRes = new char[tempResLen + 2];              //用来保存每次相乘的结果

        //最终结果
        //结果的最大长度
        int resultLen = num1Len + num2Len - 1; //结果长度最大为num1长度和num2长度之和，由于下标从0开始，所以要减一
        char[] result = new char[resultLen + 1];

        for (j = num2Len - 1; j >= 0; j--) {
            for (i = num1Len - 1; i >= 0; i--) {
                res = Integer.parseInt(num1.charAt(i) + "") * Integer.parseInt(num2.charAt(j) + "") + carry;
                tempRes[tempResLen--] = toChar(res % 10);// 把结果的个位放在结果集里面
                carry = res / 10;//得到结果集进入的数
            }

            //乘数的最后一位数与被乘数的一个数字想成的进位没有算
            //tempRes第一位为进位，刚刚的循环是没有算的，最后把进位算上
            tempRes[tempResLen] = toChar(carry);
            tempResLen = num1Len;
            carry = 0;

            //乘数与被乘数的一位数乘完后，与之前的数字相加
            //由result的末尾开始计算和，算完一次，向左偏移一位
            for (k = resultLen - offset; k > (resultLen - offset - num1Len); k--) {
                res = toInt(result[k]) + toInt(tempRes[tempResLen--]) + carry;
                result[k] = toChar(res % 10);
                carry = res / 10;
            }
            //最后一个相加数的进入
            result[k] = toChar(toInt(result[k]) + toInt(tempRes[tempResLen]) + carry);
            carry = 0;
            tempResLen = num1Len;
            offset++;
        }

        String str = new String(result);
        while (str.startsWith("0")) {
            str = str.substring(1);
        }
        return str;
    }

    public static char toChar(int c) {
        return String.valueOf(c).charAt(0);
    }

    public static int toInt(char c) {
        try {
            return Integer.valueOf(c + "");

        } catch (Exception e) {
            return 0;
        }
    }
}
