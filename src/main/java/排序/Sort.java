package 排序;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Sort {
    public static void sort1(ArrayList<Integer> a) {
        // 降序
        Collections.sort(a, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2) {
                    return -1;
                } else if (o1 < o2) {
                    return 1;
                }
                return 0;
            }
        });
    }

    // o1 < o2 升序
    public static void sort2(ArrayList<Integer> a) {
        // 自定义排序规则，使其升序排列
        Collections.sort(a, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2) {
                    return 1; // 标识需要交换位置
                } else if (o1 < o2) {
                    return -1;
                }
                return 0;
            }
        });
    }

    // 降序
    public static void sort3(ArrayList<Integer> a) {

        Collections.sort(a, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
               return o2-o1;
            }
        });
    }

    // 升序
    public static void sort4(ArrayList<Integer> a) {

        Collections.sort(a, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        });
    }

    public static void main(String[] args) {
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(1);
        a.add(3);
        a.add(5);
        a.add(2);
        a.add(4);

/*        for (int i = 0; i < a.size(); i++) {
            System.out.println(a.get(i));
        }
        System.out.println("降序====================");

        sort1(a);*/



        System.out.println("升序====================");

        sort2(a);
        for (int i = 0; i < a.size(); i++) {
            System.out.println(a.get(i));
        }

        System.out.println("绛序====================");
        sort3(a);
        for (int i = 0; i < a.size(); i++) {
            System.out.println(a.get(i));
        }

        System.out.println("升序====================");
        sort4(a);
        for (int i = 0; i < a.size(); i++) {
            System.out.println(a.get(i));
        }
    }
}
