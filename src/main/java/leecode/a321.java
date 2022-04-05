package leecode;

import java.util.ArrayList;
import java.util.List;

/**
 * 拼接最大数
 * 给定长度分别为 m 和 n 的两个数组，其元素由 0-9 构成，表示两个自然数各位上的数字。现在从这两个数组中选出 k (k <= m + n) 个数字拼接成一个新的数，要求从同一个数组中取出的数字保持其在原数组中的相对顺序。
 * <p>
 * 求满足该条件的最大数。结果返回一个表示该最大数的长度为 k 的数组。
 * <p>
 * 说明: 请尽可能地优化你算法的时间和空间复杂度。
 * <p>
 * 示例 1:
 * <p>
 * 输入:
 * nums1 = [3, 4, 6, 5]
 * nums2 = [9, 1, 2, 5, 8, 3]
 * k = 5
 * 输出:
 * [9, 8, 6, 5, 3]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/create-maximum-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class a321 {

    public static void main(String[] args) {
        int[] a = {3, 6, 8, 1, 2};
        int[] b = {3, 6, 8, 12, 3};
        Solution solution = new Solution();
//        int[] ints = solution.maxNumber(a, b,3);
        int[] ints = solution.maxSubsequence(a, 3);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    static class Solution {

        /**
         * 假设数组一为[3,4,6,5]、数组二为[9,1,2,5,8,3]、k = 5;
         * 组合情况有0 + 5、1 + 4、2 + 3、3 + 2、4 + 1五种情况,就是从此五种情况取出组合最大的一种;
         * Math.max(0, k - n)表示若数组二的元素个数 >= k,则数组一的元素个数可以从0开始取,否则在数组二的大小基础上补.
         * https://blog.csdn.net/qq_44461217/article/details/110490311
         * @param nums1
         * @param nums2
         * @param k
         * @return
         */

        public int[] maxNumber(int[] nums1, int[] nums2, int k) {
            int m = nums1.length, n = nums2.length;
            int[] maxSubsequence = new int[k];
            int start = Math.max(0, k - n), end = Math.min(k, m); //  start end 针对 nums1
            for (int i = start; i <= end; i++) { //  出错 <=
                int[] subsequence1 = maxSubsequence(nums1, i);
                int[] subsequence2 = maxSubsequence(nums2, k - i);
                int[] curMaxSubsequence = merge(subsequence1, subsequence2);
                if (compare(curMaxSubsequence, 0, maxSubsequence, 0) > 0) {
                    System.arraycopy(curMaxSubsequence, 0, maxSubsequence, 0, k);
                }
            }
            return maxSubsequence;
        }

        // 最大子序列
        public int[] maxSubsequence(int[] nums, int k) {
            int length = nums.length;
            int[] stack = new int[k];
            int top = -1; // top 最大子序列的索引
            int remain = length - k;
            for (int i = 0; i < length; i++) {
                int num = nums[i];
                while (top >= 0 && stack[top] < num && remain > 0) { // 栈 小于 遍历的数
                    top--; // 覆盖较小数 栈存储的是较大值 如果遇到更大值 则 栈指针多次回退
                    remain--; // 去掉一个的计数
                }
                if (top < k - 1) {
                    stack[++top] = num;
                } else {
                    remain--; // 如果没有添加到stack数组， remain--
                }
            }
            return stack;
        }

        public int[] merge(int[] subsequence1, int[] subsequence2) {
            int x = subsequence1.length, y = subsequence2.length;
            if (x == 0) {
                return subsequence2;
            }
            if (y == 0) {
                return subsequence1;
            }
            int mergeLength = x + y;
            int[] merged = new int[mergeLength];
            int index1 = 0, index2 = 0;
            for (int i = 0; i < mergeLength; i++) {
                if (compare(subsequence1, index1, subsequence2, index2) > 0) {
                    merged[i] = subsequence1[index1++];
                } else {
                    merged[i] = subsequence2[index2++];
                }
            }
            return merged;
        }

        // 比较两个数组大小
        public int compare(int[] subsequence1, int index1, int[] subsequence2, int index2) {
            int x = subsequence1.length, y = subsequence2.length;
            while (index1 < x && index2 < y) {
                int difference = subsequence1[index1] - subsequence2[index2];
                if (difference != 0) { // 跳过相等的
                    return difference; // 返回差值 直接跳出该compare
                }
                index1++;
                index2++;
            }
            return (x - index1) - (y - index2); // 111111 111
        }
    }

    class Solution2 {
        /**
         * 最大数来源于 nums1长度为s的子序列 和num2长度为k - s的子序列
         * 反证法可得 最大数来源于 nums1长度为s的最大子序列 和num2长度为k - s的最大子序列
         * 按最大值合并两个子序列， 即为结果
         * 时间复杂度 ： O(k * max(n, k) )
         */
        public int[] maxNumber(int[] nums1, int[] nums2, int k) {
            int[] ans = new int[k];
            if (nums1.length + nums2.length < k) return ans;
            for (int s = Math.max(k - nums2.length, 0); s <= Math.min(nums1.length, k); s++) {
                List<Integer> l1 = getKLargestNumber(nums1, s);   //O(n1)
                List<Integer> l2 = getKLargestNumber(nums2, k - s);  //O(n2)
                int[] merge = merge(l1, l2);    //O(k)
                if (isBigger(merge, ans)) {
                    ans = merge;  //O(k)
                }
            }
            return ans;
        }

        //获取长度为k的最大子序列
        public List<Integer> getKLargestNumber(int[] nums, int k) {
            int n = nums.length;
            int popCnt = n - k;
            List<Integer> res = new ArrayList<>();
            if (k == 0) return res;
            for (int i = 0; i < n; i++) {
                while (!res.isEmpty() && res.get(res.size() - 1) < nums[i] && popCnt > 0) {
                    res.remove(res.size() - 1);
                    popCnt--;
                }
                if (res.size() < k) res.add(nums[i]);
                else popCnt--;  //注意， 这里容易漏了， 如果没有添加到数组， pop--
            }
            return res;
        }

        //合并两个有序子序列， 成为一个最大值
        public int[] merge(List<Integer> l1, List<Integer> l2) {
            int[] res = new int[l1.size() + l2.size()];
            int l = 0, r = 0, idx = 0;
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (int i = l; i < l1.size(); i++) sb1.append(l1.get(i));
            for (int i = r; i < l2.size(); i++) sb2.append(l2.get(i));
            String a = sb1.toString();
            String b = sb2.toString();
            while (l < l1.size() || r < l2.size()) {
                if (l >= l1.size()) res[idx++] = l2.get(r++);
                else if (r >= l2.size()) res[idx++] = l1.get(l++);
                else if (a.substring(l, l1.size()).compareTo(b.substring(r, l2.size())) >= 0) {
                    res[idx++] = l1.get(l++);
                } else {
                    res[idx++] = l2.get(r++);
                }
            }
            return res;
        }

        //前面的数是否大于后面的数
        public boolean isBigger(int[] list, int[] ans) {
            if (ans.length == 0) return true;
            for (int i = 0; i < list.length; i++) {
                if (list[i] > ans[i]) return true;
                else if (list[i] < ans[i]) return false;
            }
            return false;
        }
    }


}
