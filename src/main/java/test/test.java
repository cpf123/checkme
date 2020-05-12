package test;

import com.alibaba.fastjson.*;
import jodd.io.StreamGobbler;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


//    public static void main(String[] args) {
//        String s = "a"+"b"+"c";
//        String s1 = "abc";
//        System.out.println(s == s1);//true
//        System.out.println(s.equals(s1));//true
/*
 *因为s是变量，那么s无论是和常量还是和其他变量相+，在源码里面得到的新串，都是new出来的一个新的String，这个String是放在堆里面的。既然是new 出来的，那自然不是同一个对象。
 * */

//        String s = "ab";
//        String s1 = "abc";
//        String s2 = s + "c";
//        System.out.println(s2 == s1);//false
//        System.out.println(s2.equals(s1));//true
//        String json = "{ \"a\": 1, \"b\": { \"c\": 2, \"d\": [3,4] } }";
//        JSONObject jsonObject1 = new JSONObject();
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(json);
//            for (String a : jsonObject.keySet()
//            ) {
//                if (jsonObject.get(a) != null && isJson(jsonObject.get(a))) {
//                    for (String b:jsonObject.getJSONObject(a).keySet()
//                         ) {
//                        jsonObject1.put(a+"."+b, jsonObject.getJSONObject(a).get(b));
//                        System.out.println(jsonObject.get(b));
//                    }
//                }else {
//                    jsonObject1.put(a, jsonObject.get(a));
//                }
//
//            }
//            System.out.println(jsonObject1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static boolean isJson(Object content) {
//        try {
//            JSONObject.parseObject(String.valueOf(content));
//
//            return true;
//        } catch (Exception e) {
//            return false;
//        }


//        String ss="123456";
//        char[] cc;
//        cc=ss.toCharArray();
//        for (char a:cc
//             ) {
//            int i = a - '0';
//            System.out.println(i);
//        }
//        System.out.println(cc);

//        new test().printFirstLetter2("aassddfg");
//    }

//用类似哈希表的方式用来统计字符出现的次数，时间复杂度O(n)
//    private void printFirstLetter2(String str){
//        int[] hash = new int[256];
//
//        //统计字符出现的次数，存在hash数组中
//        for (int i = 0; i < str.length(); i++) {
//            int temp = str.charAt(i);  // char=>int
//            System.out.println(temp);
//            hash[temp]++;
//        }
//
//        //按顺序进行遍历，将出现的此处为1的字符打印出来
//        for (int i = 0; i < str.length(); i++) {
//            int temp = str.charAt(i);
//            if (hash[temp] == 1){
//                char c = (char) temp;
//                System.out.println(c);
//                break;
//            }
//        }
//    }
//    //用哈希表的方式用来统计字符出现的字数，时间复杂度O(n)
//    private void printFirstLetter3(String str) {
//        HashMap<Character, Integer> hashMap = new HashMap<>();
//
//        for (int i = 0; i < str.length(); i++) {
//            if (hashMap.containsKey(str.charAt(i))){
//                int value = hashMap.get(str.charAt(i));
//                hashMap.put(str.charAt(i), value+1);
//            }else {
//                hashMap.put(str.charAt(i), 1);
//            }
//        }
//
//        for (int i = 0; i < str.length(); i++) {
//            if (hashMap.get(str.charAt(i)) == 1){
//                System.out.println(str.charAt(i));
//                break;
//            }
//
//        }
//
//    }


//    }

//    public static int run(int n) {
//        if (n == 1) {
//            return 1;
//        }
//        if (n == 2) {
//            return 2;
//        } else {
//            return run(n - 1) + run(n - 2);
//        }
//    }

//
//    public static int maxSubArray(int[] nums) {
//        int res = nums[0];
//        for (int i = 1; i < nums.length; i++) {
//            nums[i] = Math.max(nums[i - 1] + nums[i], nums[i]);
//
//            res = Math.max(res, nums[i]);
//        }
//        return res;
//    }

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;


public class test {
    /**
     * @author SHI
     * 求一个数组中相加值最大的连续序列元素
     */
//    public class MaxSequence {

//        public static void main(String[] args) {
//            int[] a=new int[]{-2,9,-3,4,-6,7,-6,4};
//            findBigSequence(a);
//        }

    /**
     * 思想： (1)计算出该数组的所有元素和，假设该值为最大
     * (2)从数组下标1到a.length-1依次求和，每循环求得一个值就与假设的最大值比较
     *
     * @param a
     */


    public static void findBigSequence(int[] a) {
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }

        int max = sum;
        int start = 1;
        int flag = 0;
        int sum1 = 0;
        //比较，满足条件就交换
        for (; start < a.length; start++) {
            sum1 += a[start];
            if (max < sum1) {
                max = sum1;
                flag = start;
            }
        }
        //打印找到的序列元素
        for (int i = 0; i < flag; i++) {
            System.out.print(a[i] + " ");
        }
    }

    /**
     * 1.版本号排序
     * 版本号是版本的标识，请实现一个函数对输入的所有版本号(主版本号.子版本号[.修正版本号[.编译版本号]])进行排序
     * 输入：["1.2.0.1234", "0.1", "1.0.0", "1.0", "1.0.0.build-160217"]
     * 输出：["0.1", "1.0", "1.0.0", "1.0.0.build-160217", "1.2.0.1234"]
     *
     * @paramargs
     */
    public String[] versionSort(String[] data) {
        Arrays.sort(data);
        return data;
    }

    /**
     * 2.通知到每个人花费的时间
     * 小区内有n个的人。每个人的id都不一样（从0到n-1）。
     * 在 leader 数组中，每个人都有一个直属负责人，对于总负责人leader[id] = -1。
     * 受疫情影响，总负责人想要给小区内的每个人发送一条紧急消息，他会首先通知他负责的人，由这些负责的人再通知他们负责的人，直到小区内所有人都收到了通知。
     * time[i]则是，编号为i的人将消息通知到所有他负责的人所需的时间。
     * 返回通知到每个人所花费的时间
     * 输入： n = 1, leader = [-1], time = [0]
     * 输出：0
     * <p>
     * 输入：n = 5, leader = [3,3,3,-1,3], time = [0,0,0,5,0]
     * 输出：5
     * <p>
     * 输入：n = 7, leader = [1,2,3,4,5,6,-1], time = [0,6,5,4,3,2,1]
     * 输出：21
     *
     * @paramargs
     */
//    我们找到每一个叶子节点，然后从下网上搜到根节点就好了，代码也很好理解，直接看代码吧。
    public int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {
        int[] arr = new int[n];
        int ans = 0;
        // 从0开始遍历
        for (int i = 0; i < n; i++) {
            int index = i;
            int sum = 0;
            if (informTime[index] == 0)    //表明是叶子节点
                while ((index = manager[index]) != -1) {  //循环往上找，直到头结点
                    sum += informTime[index];         //记录每次的和
                    if (sum <= arr[index])
                        break;      //如果某个叶结点走到这个结点时和小于之前一个叶结点走到这里的和，那么接下来不用走了，因为肯定比之前的小（why? 因为这个类似相交链表，他们后面一段路都是一样的。都是同一个manager）
                    arr[index] = sum;                 //否则就更新这个值
                    ans = Math.max(sum, ans);          //记录这个数组的最大值
                }
        }
        return ans;
    }


    private static int search(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == -1) {
                //在数组中
                return i;
            }
        }
        return -1;
    }

    public int minutes(int n, int[] leader, int[] time) {
        int res = 0;
        int headid = search(leader);
//        manager = [2,2,-1,2,2,2], informTime = [0,0,1,0,0,0]
        for (int i = 0; i < leader.length; i++) {
            if (time[i] == 0) {
                int tmp = 0;
                int index = i; // wei zhi suo yin
                while (index != headid) {
                    tmp += time[leader[index]];
                    index = leader[index]; //子节点 追踪root节点
                    System.out.println(index);
                }
                res = Math.max(res, tmp);
            }
        }
        return res;
    }

    class Solution {
        public int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {
            //最终结果
            int res = 0;

            for (int i = 0; i < manager.length; i++) {
                //判断是否为结束点，剪枝
                if (informTime[i] == 0) {
                    //临时值
                    int temp = 0;
                    int index = i;
                    //向上遍历
                    while (index != -1) {
                        temp += informTime[index];
                        index = manager[index];
                    }
                    res = Math.max(res, temp);
                }
            }
            return res;
        }
    }


    /**
     * 3.骰子概率
     * 有一颗神奇的筛子可以变为任意面数的筛子（1-K）。将该筛子连续扔n次。
     * 返回所有可以产生的点数，及其出现的概率（返回浮点数即可）
     * 输入：n = 1, k = 6
     * 返回：[[1,1/6], [2,1/6], [3,1/6], [4,1/6], [5,1/6], [6,1/6]]
     * <p>
     * 输入：n = 2, k = 6
     * 返回：[[2,1/36], [3,2/36], [4,3/36], [5,4/36], [6,5/36], [7,6/36], [8,5/36], [9,4/36], [10,3/36], [11,2/36], [12,1/36]]
     *
     * @paramargs
     */

    public List<List<Object>> check(int n, int k) {
        List<List<Object>> results = new ArrayList<>();

        double[][] f = new double[n + 1][6 * n + 1];
        for (int i = 1; i <= k; ++i)
            f[1][i] = 1.0 / k; // 掷一次骰子的概率。
//        System.out.println(Arrays.deepToString(f));
/**
 *
 *         [[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
 *         [0.0, 0.16666666666666666, 0.16666666666666666, 0.16666666666666666, 0.16666666666666666, 0.16666666666666666, 0.16666666666666666, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
 *         [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]]
 */

        for (int i = 2; i <= n; ++i) {
            System.out.println(i);
            for (int j = i; j <= k * n; ++j) {// j下一阶段骰子的情况。
                for (int m = 1; m <= k; ++m) { //m 上一阶段骰子的情况。
                    if (j > m) {
                        f[i][j] += f[i - 1][j - m];//令f[i][j]表示前i次投掷总和为j的方案数

                    }
                }
                f[i][j] /= k;
            }
        }

        System.out.println(Arrays.deepToString(f));
        /**
         *
         * [[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
         *   [0.0, 0.16666666666666666, 0.16666666666666666, 0.16666666666666666, 0.16666666666666666, 0.16666666666666666, 0.16666666666666666, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
         *   [0.0, 0.0, 0.027777777777777776, 0.05555555555555555, 0.08333333333333333, 0.1111111111111111, 0.13888888888888887, 0.16666666666666666, 0.13888888888888887, 0.1111111111111111, 0.08333333333333333, 0.05555555555555555, 0.027777777777777776]]
         *
         */
        for (int i = n; i <= 6 * n; ++i) {
            ArrayList<Object> objects = new ArrayList<Object>();
            objects.add(i);
            objects.add(f[n][i]);

            results.add(objects);
        }

        return results;
    }


    /**
     * 4.json解析器（不依赖于其他现有库，自己对json进行解析）
     * JSON是经常使用的轻量级数据交换格式，请实现一个函数，该函数接收一个字符串并将其转化为一个json对象（也可以是字典等）。
     * 输入：{"key1":"value1","key2":"value2"}
     * 输出：
     * {
     * "key1":"value1",
     * "key2":"value2"
     * }
     *
     * @paramargs
     */

    class jsonObject {
        public jsonObject() {
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder stringBuffer = new StringBuilder();
            for (String s : list.keySet()) {
//                System.out.println(s);
                stringBuffer.append("\"").append(s).append("\"").append(":").append("\"" + list.get(s) + "\"").append(",").append("\n");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 2);
            return "{\n" + stringBuffer.toString() +
                    "}";
        }

        String key;
        String value;
        HashMap<String, String> list;

        public HashMap<String, String> getList() {
            return list;
        }

        public void setList(HashMap<String, String> list) {
            this.list = list;
        }
    }

    public Object jsonparse(String str) {
        JSONObject jo = new JSONObject();
        String replace = str.replace("{\"", "").replace("\"}", "");
        String[] split = replace.split("\",\"");
        jsonObject jsonObject = new jsonObject();
        ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
//        System.out.println(Arrays.toString(split));
        HashMap<String, String> map = new HashMap<>();
        for (String s : split) {
            String[] split1 = s.split("\":\"");
//            System.out.println(split1[0]);
            map.put(split1[0], split1[1]);
            jsonObject.setList(map);
        }

        return jsonObject;
    }


    public static int[] merge(int[] data1, int[] data2) {
        int size1 = data1.length;
        int size2 = data2.length;
        int[] tmp = new int[size1 + size2];
        int n1 = 0, n2 = 0, n3 = 0;
        while (n1 < size1 && n2 < size2) {
            if (data1[n1] < data2[n2]) {
                tmp[n3++] = data1[n1++];
            } else {
                tmp[n3++] = data2[n2++];
            }
        }
        while (n1 < size1) {
            tmp[n3++] = data1[n1++];
        }
        while (n2 < size2) {
            tmp[n3++] = data2[n2++];
        }
        return tmp;
    }

    public static void sqrt(int n) {
        for (int i = 0; i <= n; i++) {
            if (Math.max(i * i, n) == i * i) {
                System.out.println(i - 1);
                break;
            }


        }
    }

    //    leetcode 33
    public int search(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        int start = 0;
        int end = nums.length - 1;
        int mid;
        while (start <= end) {
            mid = start + (end - start) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            //前半部分有序,注意此处用小于等于
            if (nums[start] <= nums[mid]) {
                //target在前半部分
                if (target >= nums[start] && target < nums[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else {
                if (target <= nums[end] && target > nums[mid]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return -1;
    }


    public static int[] fun(int[] arr) {
        int front = 0, end = arr.length - 1;
        if (arr.length == 0) {
            return null;
        }
        while (front < end) {
            while (front < arr.length && arr[front] % 2 == 1) {
                front++;
            }
            while (end >= 0 && arr[end] % 2 == 0) {
                end--;
            }
            if (front < end) {
                int tmp = arr[front];
                arr[front] = arr[end];
                arr[end] = tmp;
            }
        }
        return arr;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
//        new TreeMap<>()

//        ProcessBuilder processBuilder = new ProcessBuilder();
//        processBuilder.command(commands);
//        Process process = processBuilder.start();
//        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
//        Future<?> submit = Executors.newSingleThreadExecutor().submit(streamGobbler);
//        System.out.println(submit.isDone());
//        int exitCode = process.waitFor();
//        assert exitCode == 0;
//        try {
//            List<String> commands = new ArrayList<>();
//            commands.add("java");
//            commands.add("-version");
//            ProcessBuilder pb = new ProcessBuilder(commands);
//            Process ps = pb.start();
//            InputStream is = ps.getErrorStream();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.err.println(line);
//            }
//
//
//            InputStream is1 = ps.getInputStream();
//            BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
//
//            String line1;
//            while ((line1 = br1.readLine()) != null) {
//                System.out.println(line1);
//            }
//
//            int exitCode = ps.waitFor();
//            System.out.println(exitCode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String str1=new StringBuilder("58").append("ganji").toString();
        System.out.println(str1.intern() == str1);
        String str2=new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2);
        // 对str2 比较返回 false是因为“java”这个字符串在执行 StringBuilder.toString()之前就已经出现过。我们最后发现java这个字符串在System类中被初始化

        int[] arr = {1, 2, 3, 4, 5, 2, 7};
        Arrays.sort(arr);
        int[] data = fun(arr);
//        System.out.println(Arrays.toString(data));
//        System.out.println(String.valueOf(1)+1);
//        String[] split = "bdl_qiye_product".split(":");
//        for (String s : split) {
//            System.out.println(s);
//        }
        String s1 = "JOIN_COUNT:test|=|www";
        String substring = s1.substring(s1.indexOf(':') + 1);
//        System.out.println(substring);
//        System.out.println(substring.substring(0,substring.indexOf("|=|") ));
        String[] split = substring.split("|=|");
        ArrayList<String> arrayList = new ArrayList<>();

//        for (String sq:split) {
//            System.out.println(sq);
//            arrayList.add(sq);
//        }
//        String s11 = arrayList.get(0);
//        String s2 = arrayList.get(1);
//        System.out.println(s2);
//        System.out.println("JOIN_COUNT:test|=|www".substring(s1.charAt(':')));
//        System.out.println(run(1));
//        String[] data = {"1.2.0.1234", "0.1", "1.0.0", "1.0", "1.0.0.build-160217"};
        test test = new test();
        sqrt(5);
        String s = "qweqw qweq";
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                chars[i] = '%' + '2';
            }
        }
        for (char aChar : chars) {
//            System.out.println(aChar);
        }
//        System.out.println(Arrays.toString(test.versionSort(data)));
//        System.out.println(test.check(2, 6));
//        System.out.println(test.minutes(15, new int[]{-1,0,0,1,1,2,2,3,3,4,4,5,5,6,6}, new int[]{1,1,1,1,1,1,1,0,0,0,0,0,0,0,0}));
//        System.out.println(test.jsonparse("{\"key1\":\"value1\",\"key2\":\"value2\"}").toString());
//        System.out.println(Arrays.toString(merge(new int[]{3, 3, 3, -1, 3}, new int[]{0, 0, 0, 5, 0})));
    }
}

