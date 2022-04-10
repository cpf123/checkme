package leecode;

public class a110 {


    // 平衡二叉树
    /**
     * 方法二：自底向上的递归
     * 时间复杂度：O(n)，其中 n 是二叉树中的节点个数。使用自底向上的递归，每个节点的计算高度和判断是否平衡都只需要处理一次，
     * 最坏情况下需要遍历二叉树中的所有节点，因此时间复杂度是 O(n)。
     *
     * 空间复杂度：O(n)，其中 n 是二叉树中的节点个数。空间复杂度主要取决于递归调用的层数，递归调用的层数不会超过 n。
     */

    class Solution2 {
        public boolean isBalanced(TreeNode root) {
            return height(root) >= 0;
        }
        public int height(TreeNode root) {
            //递归出口
            if (root == null) {
                return 0;
            }
            //调用本身，递归
            int leftHeight = height(root.left);
            int rightHeight = height(root.right);

            //每次出栈的函数处理
            if (leftHeight == -1 || rightHeight == -1 || Math.abs(leftHeight - rightHeight) > 1) {
                return -1;
            } else {
                return Math.max(leftHeight, rightHeight) + 1; // 最大深度 leftHeight, rightHeight 相等
            }
        }
    }


    /**
     *
     * 时间复杂度：O(n^2)，其中 nn 是二叉树中的节点个数。
     * 空间复杂度：O(n)，其中 nn 是二叉树中的节点个数。空间复杂度主要取决于递归调用的层数，递归调用的层数不会超过 nn。
     */
    class Solution {
        public boolean isBalanced(TreeNode root) {
            if (root == null) {
                return true;
            } else {
                return Math.abs(height(root.left) - height(root.right)) <= 1 && isBalanced(root.left) && isBalanced(root.right);
            }
        }

        public int height(TreeNode root) {
            if (root == null) {
                return 0;
            } else {
                return Math.max(height(root.left), height(root.right)) + 1;
            }
        }
    }


}
