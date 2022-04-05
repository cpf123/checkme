package testjava;

public class BigDataMultiply {
    public static void main(String[] args) {
        String str1 = "753656566859769";
        String str2 = "9518598598486568654";
        // 字符串反转，希望后面生成的数组位置0对应个位，位置1对应十位,位置2对应百位
        str1 = reserve(str1);
        str2 = reserve(str2);
        int len1 = str1.length();
        int len2 = str2.length();
        int len = len1 + len2 + 3;

        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        int a[] = new int[len1];
        int b[] = new int[len2];
        int c[] = new int[len];
        for (int i = 0; i < len1; i++) {
            a[i] = Integer.valueOf(String.valueOf(chars1[i]));
        }
        for (int i = 0; i < len2; i++) {
            b[i] = Integer.valueOf(String.valueOf(chars2[i]));
        }
        String result = countMultiply(c, a, b, len, len1, len2);
        System.out.println(result);
    }


    private static String countMultiply(int[] c, int[] a, int[] b, int len, int len1, int len2) {
        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                // 核心代码：相乘结果各个位置上的值和乘数对应的位置有关联。
                // 例如：结果个位上的数值只能由乘数个位上的数相乘取个位。
                c[i + j] += a[i] * b[j];
            }
        }
        for (int i = 0; i < len; i++) {
            if (c[i] / 10 > 0) {// 判断是否需要进位
                // 更高位置进位
                c[i + 1] += c[i] / 10;
            }
            // 取该位置对应数字
            c[i] = c[i] % 10;
        }
        int m = 0;
        for (int i = len - 1; i >= 0; i--) {
            // 数组下标对应较高位置，即结果较高位数可能有多个0，不需要遍历应去除
            if (c[i] > 0) {
                m = i;
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        // 从结果最高不为0位置开始遍历
        for (int i = m; i >= 0; i--) {
            sb.append(c[i]);
        }

        return sb.toString();
    }

    private static String reserve(String str1) {
        if (str1 == null || str1.length() == 1) {
            return str1;
        } else {
            return reserve(str1.substring(1)) + str1.charAt(0);
        }
    }

}
