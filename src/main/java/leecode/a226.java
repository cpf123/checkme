package leecode;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class a226 {
    /**
     * 翻转二叉树
     * 递归方式遍历反转
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }

        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;

        invertTree(root.left);
        invertTree(root.right);
        return root;
    }


    /**
     * 层序遍历方式反转
     */
    public TreeNode invertTreeByQueue(TreeNode root) {
        if (root == null) {
            return null;
        }
        Queue<TreeNode> queue = new LinkedList<>(); //声明队列 继承双向队列的接口
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            TreeNode temp = node.left;
            node.left = node.right;  // 交换的时候要注意变量隔离
            node.right = temp;
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return root;
    }

    /**
     * 深度优先遍历的方式反转
     */
    private TreeNode invertTreeByStack(TreeNode root) {
        if (root == null) {
            return null;
        }
        Stack<TreeNode> stack= new Stack();
        stack.push(root);
        while(!stack.isEmpty()){
            TreeNode node= stack.pop();
            TreeNode tmp= node.left;
            node.left= node.right;
            node.right=tmp;
            if(node.left!=null){
                stack.push(node.left);
            }
            if(node.right!=null){
                stack.push(node.right);
            }
        }
        return root;
    }
}
