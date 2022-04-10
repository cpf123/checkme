package leecode;


import java.util.*;

/**
 * https://blog.csdn.net/qq_14842117/article/details/89216438?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_antiscanv2&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_antiscanv2&utm_relevant_index=1
 * 找出数组中出现次数大于n/2、n/3，n/k的数
 */

public class o1 {

    public static void main(String[] args) {

        int[] array = {2, 3, 1, 2, 2, 6, 2, 2, 4, 2};
        int[] a = new int[]{1, 2, 5, 2, 3, 6, 7, 2, 4, 2, 8, 4, 3, 2};
//        int i = find(array);
        ArrayList arrayList = find2(array);
//        System.out.println(i);

        System.out.println(arrayList.get(0));
    }


    //    查找一维数组中出现次数最多的值及其出现次数
    public static int find(int[] a) {
        /*2.查找一维数组中出现次数最多的值及其出现次数
         {1,2,5,2,3,6,7,2,4,2,8,4,3,2}*/
        int[] b = new int[10];
        for (int i = 0; i < a.length; i++) {
            b[a[i]]++;
        }
        int max = b[0];   //定义次数
        int value = 0;    //定义最大值
        for (int i = 1; i < b.length; i++) {
            if (b[i] > max) {
                max = b[i];
                value = i;
            }
        }
        System.out.println("出现次数最多的值是:" + value + ", 出现了" + max + "次");
        return value;

    }

    // 找出数组中出现次数大于n/2、n/3，n/k的数
    public static ArrayList find2(int[] a) {
        /*2.查找一维数组中出现次数最多的值及其出现次数
         {1,2,5,2,3,6,7,2,4,2,8,4,3,2}*/
        ArrayList array = new ArrayList(a.length);
        int[] b = new int[10];
        for (int i = 0; i < a.length; i++) {
            b[a[i]]++;
        }
        int len = a.length / 2;
        for (int i = 0; i < b.length; i++) {
            if (b[i] > len) {
                array.add(i);
            }
        }
        return array;
    }

}
