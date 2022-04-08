package leecode;

public class a704 {
    /**
     * 时间复杂度：O(\log n)O(logn)，其中 nn 是数组的长度。
     *
     * 空间复杂度：O(1)O(1)。
     * 元素有序的（升序）整型数组
     */
    class Solution {
        public int search(int[] nums, int target) {
            int low = 0, high = nums.length - 1;
            while (low <= high) { // <= !!!chu cuo
                int mid = (high - low) / 2 + low; // !!!chu cuo
                int num = nums[mid];
                if (num == target) {
                    return mid;
                } else if (num > target) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
            return -1;
        }
    }


}
