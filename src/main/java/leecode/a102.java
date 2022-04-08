package leecode;

import java.util.*;

public class a102 {
    // 层序遍历 广度优先遍历
    class Solution {
        public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
            List<List<Integer>> ans = new LinkedList<List<Integer>>();
            if (root == null) {
                return ans;
            }

            Queue<TreeNode> nodeQueue = new LinkedList<TreeNode>();
            nodeQueue.offer(root);
            boolean isOrderLeft = true;

            while (!nodeQueue.isEmpty()) {
                Deque<Integer> levelList = new LinkedList<Integer>();
                int size = nodeQueue.size();
                for (int i = 0; i < size; ++i) {
                    TreeNode curNode = nodeQueue.poll(); // 移除并返回
                    if (isOrderLeft) {
                        levelList.offerLast(curNode.val);
                    } else {
                        levelList.offerFirst(curNode.val);
                    }
                    if (curNode.left != null) {
                        nodeQueue.offer(curNode.left);
                    }
                    if (curNode.right != null) {
                        nodeQueue.offer(curNode.right);
                    }
                }
                ans.add(new LinkedList<Integer>(levelList));
                isOrderLeft = !isOrderLeft;
            }

            return ans;
        }
    }
// dfs实现 深度优先
    class Solution2 {
        List<List<Integer>> res = new ArrayList<>();

        public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
            if (root == null) {
                return res;
            }
            dfs(root, 0);
            return res;
        }

        private void dfs(TreeNode root, int depth) {
            if (root == null) {
                return;
            }
            if (depth == res.size()) {
                res.add(new LinkedList<>());
            }
            LinkedList<Integer> lk = (LinkedList<Integer>) res.get(depth);
            if (depth % 2 == 0) {
                lk.addLast(root.val);
            } else {
                lk.addFirst(root.val);
            }
            dfs(root.left, depth + 1);
            dfs(root.right, depth + 1);
        }
    }



}
