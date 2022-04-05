package hadoop.排序;

import java.util.Arrays;
import java.util.Random;

public class bigmergesort {
    /**
     * 需求：有一个很大的文件需要对内容进行排序（ps:内容可简单理解为数字），如何在有限的内存下进行排序，内存很小。
     * 分析：
     * 1.文件很大我们需要分而治之，分为若干文件
     * 2.内存小，划分小文件的时候要注意，文件内容应该可以足够放入内存
     * 3.拆分小文件的时候，对改文件内容进行排序（ps:非本文章重点故省略）
     * 4.对有序的文件进行归并排序
     */


    //分片数组长度为多少时,开始排序
    public final static int SORT_NUM = 50;

    //数组的长度
    public final static int ARRAY_NUM = 10000000;

    public static void main(String[] args) {

        //声明数组长度
        int[] array = new int[ARRAY_NUM];

        //生成随机数据
        for (int j = 0; j < ARRAY_NUM; j++) {
            array[j] = new Random().nextInt(Integer.MAX_VALUE);
        }

        //复制数组,采用JDK自带排序
        int[] arrayCopy = new int[ARRAY_NUM];

        System.arraycopy(array, 0, arrayCopy, 0, ARRAY_NUM);

        //数据少的时候可以看看初始化结果,数据量大,ecplise直接就崩溃了。
        //System.out.println("init  :"+Arrays.toString(array));
        long time1 = System.currentTimeMillis();
        sort(array, 0, array.length);
        long time2 = System.currentTimeMillis();
        //System.out.println(Arrays.toString(array));
        System.out.println(time2 - time1);
        Arrays.sort(arrayCopy);
        long time3 = System.currentTimeMillis();
        System.out.println(time3 - time2);

    }

    public static void sort(int[] array, int begin, int end) {

        //若排序部门长度小于SORT_NUM
        if (end - begin <= SORT_NUM) {

            //循环排序部分的元素
            for (int i = begin + 1; i < end; i++) {

                //查找当前元素的位置
                int index = findIndex(array[i], begin, i - 1, array);

                //获取当前元素
                int value = array[i];

                //数组移位
                System.arraycopy(array, index, array, index + 1, i - index);

                //将当前数组插入到合适的位置
                array[index] = value;
            }
            return;
        }

        //作为中间轴参数使用
        int temp = array[begin];

        //获取当前的位置
        int currentIndex = begin;

        int left = begin;
        int right = end - 1;

        while (left < right) {

            //向右比较
            for (; left < right; right--) {
                if (left < right && array[right] < temp) {
                    swap(array, currentIndex, right);
                    currentIndex = right;
                    left++;
                    break;
                }
            }

            //向左比较
            for (; left < right; left++) {
                if (left < right && array[left] > temp) {
                    swap(array, currentIndex, left);
                    currentIndex = left;
                    right--;
                    break;
                }
            }
        }

        //左侧排序
        sort(array, begin, currentIndex + 1);
        //右侧排序
        sort(array, currentIndex + 1, end);

    }


    public static void swap(int[] array, int index1, int index2) {
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    //二分查找排序
    public static int findIndex(int thisval, int from, int to, int[] sortArray) {

        int index = 0;

        //当左侧数据大于当前数据,返回from的索引
        if (sortArray[from] >= thisval) {
            index = from;
            return index;
        }

        //当右侧数据小于当前数据,返回index后面的索引
        if (sortArray[to] <= thisval) {
            index = to + 1;
            return index;
        }

        if (to - from == 1) {
            return to;
        }

        //获取from和to的中间值
        int middle = (to - from) / 2 + from;

        //判断当前值在左侧还是右侧
        if (sortArray[middle] >= thisval) {
            //左侧
            return findIndex(thisval, from, middle, sortArray);
        } else {
            //右侧
            return findIndex(thisval, middle, to, sortArray);
        }
    }


}
