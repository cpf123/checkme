package test.stack;



import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MinStack {

    /*
     * min[i]数组记录当前元素[0...i]区间内的最小值
     * min[i]=Min(min[i-1],a[i])
     */
    private List<Integer> min = new ArrayList<Integer>();
    private Stack<Integer> stack = new Stack<Integer>();

    public void push(int x) {
        stack.push(x);
        if (min.size() == 0)
            min.add(x);
        else {
            int tmp = min.get(min.size() - 1) > x ? x : min.get(min.size() - 1);
            min.add(min.get(tmp));
        }
    }

    public int pop() {
        if (stack.isEmpty())
            return -1;
        /*
         * 删除出栈的最小值记录，虽然不一定是当前弹栈的元素。
         */
        min.remove(stack.size() - 1);
        return stack.peek();
    }
}