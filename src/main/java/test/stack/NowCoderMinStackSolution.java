package test.stack;

/**
 * 第二种思路 是利用辅助栈 ,声明一个栈用来存放元素的实例,声明一个栈用来存放与之对应位置上的最小元素的值,这样不用考虑扩容的情况。代码如下：
 */

import java.util.Enumeration;
import java.util.Stack;

public class NowCoderMinStackSolution {
    private Stack<Integer> minStack = new Stack<Integer>();
    private Stack<Integer> dataStack = new Stack<Integer>();

    public void push(int node) {
        dataStack.push(node);
        if (minStack.isEmpty()) {
            minStack.push(node);
        } else {
            if (node <= min()) {
                minStack.push(node);
            } else {
                minStack.push(min());

            }
        }
    }

    public void pop() {
        if (dataStack.isEmpty()) {
            return;
        }
        dataStack.pop();
        minStack.pop();
    }

    public int top() {
        if (!empty()) {
            dataStack.peek();
        }
        return (Integer) null;
    }

    private boolean empty() {
        return dataStack.isEmpty();
    }

    public int min() {
        return minStack.peek();
//        相同点：大家都返回栈顶的值。
//        不同点：peek 不改变栈的值(不删除栈顶的值)，pop会把栈顶的值删除。
    }

    public static void main(String[] args) {

        int[] a = {3, 1, 2, 5, 3, 9, -1, -2, 5, 9, 11};
        NowCoderMinStackSolution stack = new NowCoderMinStackSolution();
        for (int i = 0; i < a.length; i++) {
            stack.push(a[i]);
        }
//        stack.pop();stack.pop();
        System.out.println(stack.min());
        while (!stack.minStack.empty()) {
            System.out.println(stack.minStack.pop());
        }
    }
}
