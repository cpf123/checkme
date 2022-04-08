package leecode;

import java.util.LinkedList;
import java.util.Queue;

public class a101 {
    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode() {}
     *     TreeNode(int val) { this.val = val; }
     *     TreeNode(int val, TreeNode left, TreeNode right) {
     *         this.val = val;
     *         this.left = left;
     *         this.right = right;
     *     }
     * }
     */

    // di gui
    class Solution {
        public boolean isSymmetric(TreeNode root) {
            return check(root, root);
        }

        public boolean check(TreeNode p, TreeNode q) {
            if (p == null && q == null) {
                return true;
            }
            if (p == null || q == null) {
                return false;
            }
            return p.val == q.val && check(p.left, q.right) && check(p.right, q.left);
        }
    }

    // die dai
    class Solution2 {
        public boolean isSymmetric(TreeNode root) {
            return check(root, root);
        }

        public boolean check(TreeNode u, TreeNode v) {
            Queue<TreeNode> q = new LinkedList<TreeNode>();
            q.offer(u);
            q.offer(v);
            while (!q.isEmpty()) {
                u = q.poll();
                v = q.poll();
                if (u == null && v == null) {
                    continue;
                }
                if ((u == null || v == null) || (u.val != v.val)) {
                    return false;
                }

                q.offer(u.left);
                q.offer(v.right);

                q.offer(u.right);
                q.offer(v.left);
            } // 全部遍历 直到有false
            return true;
        }
    }


}
