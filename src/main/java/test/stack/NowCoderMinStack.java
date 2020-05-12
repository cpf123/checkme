package test.stack;

/**
 *
 * 首先声明一个数组,用来存放 入栈的数据,在申请一个数组,大小和前一个一样,此数组用来存放入栈过程中,依次存放对应位置的最小值。
 * 这个过程需要一个额外的变量表示指向栈中的栈顶元素,同时也表示指向存放排序后的数组中的第一个元素。
 * 代码如下:(为了方便写代码,就没有考虑如果元素超过最大容量是进行对数组扩容的情况,小伙伴们可以自行进行扩容,在这里只是提供一个思路,虽然不是最好的)。
 */
class StackEx {
    private int[] data;
    private int[] min;
    private int pos;
    private int len;

    public StackEx() {
        this(10);
    }

    public StackEx(int n) {
        pos = 0;
        len = n;
        data = new int[n];
        min = new int[n];
    }

    boolean push(int value) {//入栈操作的时候如果考虑完全应该进行扩容,在这里只是提供一个思路。
        if (pos < len) {
            if (pos == 0) {
                min[pos] = value;
            } else {
                if (value < min[pos - 1]) {
                    min[pos] = value;
                } else {
                    min[pos] = min[pos - 1];
                }
            }
            data[pos] = value;
            pos++;
            return true;
        }
        return false;
    }

    int pop() {
        return data[--pos];
    }

    int min() {
        return min[pos - 1];
    }
}

public class NowCoderMinStack {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int[] a = {1, 3, 19, 23, 29, 41, 2, 35, 9, 10};
        StackEx stack = new StackEx(a.length);
        for (int i = 0; i < a.length; i++) {
            stack.push(a[i]);
        }
        stack.pop();
        System.out.println(stack.min());
    }
}
