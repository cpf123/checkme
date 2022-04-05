package leecode;

import java.util.*;

/**
 * 作为List使用时,采用add/get
 *
 * 作为Queue使用时,采用offer/poll
 */
public class a103 {

    class Solution2 {
        public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
            if (root == null) {
                return new ArrayList<>();
            }
            ArrayList<List<Integer>> lists = new ArrayList<>();
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);
            int count = 0;
            while (!queue.isEmpty()) {
                TreeNode poll = queue.poll();
                ArrayList<Integer> list = new ArrayList<>();
                if (count == 0) {
                    list.add(poll.val);
                    queue.add(poll);
                } else if (count % 2 == 0) {
                    // System.out.println(poll.val);
                    if (poll.left != null) {
                        list.add(poll.left.val);
                        queue.add(poll.left);
                    }
                    if (poll.right != null) {
                        list.add(poll.right.val);
                        queue.add(poll.right);
                    }
                } else {
                    // System.out.println(poll.val);

                    if (poll.right != null) {
                        list.add(poll.right.val);
                        queue.add(poll.right);
                    }
                    if (poll.left != null) {
                        list.add(poll.left.val);
                        queue.add(poll.left);
                    }
                }
                if(!list.isEmpty()){
                    lists.add(list);
                    count++;
                }

            }
            return lists;
        }
    }

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
                        TreeNode curNode = nodeQueue.poll();
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

}
