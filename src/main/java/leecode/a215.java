package leecode;

import java.util.PriorityQueue;
import java.util.Random;

public class a215 {

    /**
     * 方法一：暴力解法，这是根据题目意思能得到的最直接的解法，时间复杂度较高；
     * 方法二：通过 partition 减治，这是快速排序 partition 的应用；
     * 方法三：动态求出最值元素，是「优先队列」的应用。
     */

    public static void main(String[] args) {
        int[] ints = {1, 3, 3,4, 2};
        a215 a215 = new a215();

        int kthLargest =  new a215().findKthLargest(ints,2);
        System.out.println(kthLargest);
    }

    /**
     * 时间复杂度：O(n)O(n)，如上文所述，证明过程可以参考「《算法导论》9.2：期望为线性的选择算法」。
     * 空间复杂度：O(\log n)O(logn)，递归使用栈空间的空间代价的期望为 O(\log n)O(logn)。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/kth-largest-element-in-an-array/solution/shu-zu-zhong-de-di-kge-zui-da-yuan-su-by-leetcode-/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */
    // 法3 快速选择算法，o（n）的复杂度，n指的是元素个数。。。
    public int findKthLargest(int[] nums, int k) {
        int len = nums.length;
        int index = len - k;
        int left = 0, right = len - 1;

        while (left <= right) {
            int res = partition(nums, left, right); // 返回 左边小于 res，右边大于res的数组
            if (res < index) {
                left = res + 1;
            } else if (index < res) {
                right = res - 1;
            } else if (index == res) { // 索引一样则返回
                return nums[index];
            }
        }

        return -1;
    }
    public int partition(int[] nums, int left, int right) {
        // 改成 每次选 中间的 索引后，从8ms变成了1ms。。。
        // 不能每次都选择 最左边的 元素
        int tmp = left;
        left = left + (right - left) / 2;
        swap(nums, left, tmp);
        left = tmp;

        int pivot = nums[left];// 中间值 left = left + (right - left) / 2;
        int i = left + 1;
        int j = right;
        while (i <= j) {
            while (i <= j && nums[i] <= pivot) {
                i++;
            }
            while (i <= j && nums[j] > pivot) {
                j--;
            }
            if (i > j) { // i j 是否反向交叉
                break;
            }
            swap(nums, i, j);
        }
        swap(nums, left, j);
        return j;
    }

    // 法1 api堆 4ms
        public int findKthLargest1(int[] nums, int k) {
            // 默认是小顶堆，api一般都是从小到大升序排列的。
            PriorityQueue<Integer> pq = new PriorityQueue<>();

            for (int num : nums) {
                // 加入堆后，api会自己调整堆
                pq.offer(num);
                // 超过k个元素了的话，就pop出第 k + 1 个
                if (pq.size() > k) {
                    pq.poll();
                }
            }
            // 剩下的就是前k大的元素了，返回最小的就行了~
            return pq.peek();
        }

    /**
     * 时间复杂度：O(nlogn)，建堆的时间代价是 O(n)O(n)，删除的总代价是 O(k \log n)O(klogn)，因为 k < nk<n，
     * 故渐进时间复杂为 O(n + k \log n) = O(n \log n)O(n+klogn)=O(nlogn)。
     * 空间复杂度：O(\log n)O(logn)，即递归使用栈空间的空间代价。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/kth-largest-element-in-an-array/solution/shu-zu-zhong-de-di-kge-zui-da-yuan-su-by-leetcode-/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */        public int findKthLargest2(int[] nums, int k) {
            int len = nums.length;
            // 对前k个元素，构成小顶堆
            for (int i = 0; i < k; i++) {
                // 加入就上浮
                swim(nums, i);
            }
            // 剩下的元素与堆顶比较，若大于堆顶则去掉堆顶，add后下沉调整堆
            for (int i = k; i < len; i++) {
                if (nums[i] > nums[0]) {
                    swap(nums, 0, i);
                    // 因为是top k，所以下标是从0到k-1
                    sink(nums, 0, k - 1);
                }
            }
            // 比较完所有的元素后，堆顶就是第k大的元素。
            return nums[0];
        }

        // 上浮 从下到上调整
        public void swim(int[] nums, int child) {
            // 因为是 小顶堆，所以比父节点小的话，要换上去！
            // 如果到了0的话，那就是 堆顶了，最小了，不用比了
            while (child > 0 && nums[child] < nums[(child - 1) / 2]) {
                swap(nums, child, (child - 1) / 2);
                child = (child - 1) / 2;
            }
        }

        // 比小顶堆的堆顶元素大的话，就swap一下，然后下沉到正确的位置
        public void sink(int[] nums, int parent, int end) {
            // 从0开始的话，左节点 是 2 * parent + 1，从1开始就直接乘2
            // 左节点还没到最后的end的话，就还需要比较下 谁更小
            while (2 * parent + 1 <= end) {
                int left = 2 * parent + 1;
                int right = 2 * parent + 2;
                int minIndex = left;
                // 选出一个更小的
                if (right <= end && nums[right] < nums[left]) {
                    minIndex = right;
                }

                if (nums[minIndex] < nums[parent]) {
                    swap(nums, parent, minIndex);
                    parent = minIndex;
                } else {
                    break;
                }

            }
        }

        public void swap(int[] nums, int i, int j) {
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }



    }


