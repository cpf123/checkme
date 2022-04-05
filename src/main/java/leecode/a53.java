package leecode;

public class a53 {

    public class Solution {
        public int maxSubArray(int[] nums) {
            int pre = 0;
            int res = nums[0];
            for (int num : nums) {
                pre = Math.max(pre + num, num); // 当前加和 状态
                res = Math.max(res, pre); // 当前状态 与 上一个状态比较
            }
            return res;
        }
    }
}
