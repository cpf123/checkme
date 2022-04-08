package leecode;

import java.util.Arrays;

public class a611 {
    // 排序 双指针

    /**
     * 满足 a≤b≤c，那么 a + c > b 和 b + c > a 使一定成立的，我们只需要保证 a + b > c。
     * 在 [j + 1, k] 范围内的下标都可以作为边 c 的下标，我们将该范围的长度 k - j 累加入答案。
     */
    class Solution {
        public int triangleNumber(int[] nums) {
            int n = nums.length;
            Arrays.sort(nums);
            int ans = 0;
            for (int i = 0; i < n; ++i) {
                int k = i;
                for (int j = i + 1; j < n; ++j) {
                    while (k + 1 < n && nums[k + 1] < nums[i] + nums[j]) {
                        ++k;
                    }
                    ans += Math.max(k - j, 0);
                }
            }
            return ans;
        }
    }


}
