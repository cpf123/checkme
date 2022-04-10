package leecode.数组链表实现队列;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组实现队列
 *
   https://cloud.tencent.com/developer/article/1623644
 */
public class MyArrayQueueDemo {
    public static void main(String[] args) {
        MyQueueDemo<Integer> myQueueDemo = new MyQueueDemo<>();

        myQueueDemo.enQueue(1);
        System.out.println("-----向队列中添加元素-----");
        System.out.println("队列头元素 = " + myQueueDemo.getFront());
        System.out.println("队列尾元素 = " + myQueueDemo.getBack());
        System.out.println("队列大小 = " + myQueueDemo.size());
        myQueueDemo.enQueue(2);
        System.out.println("-----向队列中添加元素-----");
        System.out.println("队列头元素 = " + myQueueDemo.getFront());
        System.out.println("队列尾元素 = " + myQueueDemo.getBack());
        System.out.println("队列大小 = " + myQueueDemo.size());
        myQueueDemo.enQueue(3);
        System.out.println("-----向队列中添加元素-----");
        System.out.println("队列头元素 = " + myQueueDemo.getFront());
        System.out.println("队列尾元素 = " + myQueueDemo.getBack());
        System.out.println("队列大小 = " + myQueueDemo.size());
        myQueueDemo.enQueue(4);
        System.out.println("-----向队列中添加元素-----");
        System.out.println("队列头元素 = " + myQueueDemo.getFront());
        System.out.println("队列尾元素 = " + myQueueDemo.getBack());
        System.out.println("队列大小 = " + myQueueDemo.size());

        System.out.println("---------------------------");
        myQueueDemo.deQueue();
        System.out.println("-----从队列中取出元素-----");
        System.out.println("队列头元素 = " + myQueueDemo.getFront());
        System.out.println("队列尾元素 = " + myQueueDemo.getBack());
        System.out.println("队列大小 = " + myQueueDemo.size());
        myQueueDemo.deQueue();
        System.out.println("-----从队列中取出元素-----");
        System.out.println("队列头元素 = " + myQueueDemo.getFront());
        System.out.println("队列尾元素 = " + myQueueDemo.getBack());
        System.out.println("队列大小 = " + myQueueDemo.size());
        myQueueDemo.deQueue();
        System.out.println("-----从队列中取出元素-----");
        System.out.println("队列头元素 = " + myQueueDemo.getFront());
        System.out.println("队列尾元素 = " + myQueueDemo.getBack());
        System.out.println("队列大小 = " + myQueueDemo.size());
        myQueueDemo.deQueue();
        System.out.println("-----从队列中取出元素-----");
        System.out.println("队列头元素 = " + myQueueDemo.getFront());
        System.out.println("队列尾元素 = " + myQueueDemo.getBack());
        System.out.println("队列大小 = " + myQueueDemo.size());
    }
}

class MyQueueDemo<T> {
    private List<T> arrayList = new ArrayList<>();

    private int front;
    private int rear;

    public MyQueueDemo() {
        this.front = 0;
        this.rear = 0;
    }

    public boolean isEmpty() {
        return front == rear;
    }

    public int size() {
        return rear - front;
    }

    //获取对首元素
    public T getFront() {
        if (isEmpty()) {
            return null;
        }
        return arrayList.get(front);
    }

    //获取队列尾部元素
    public T getBack() {
        if (isEmpty()) {
            return null;
        }
        return arrayList.get(rear - 1);
    }

    //删除队列中头部元素
    public void deQueue() {
        if (rear > front) {
            front++;
        } else {
            System.out.println("队列已经不存在元素了");
        }
    }

    //把新元素添加到队列中（队列尾部）
    public void enQueue(T item) {
        arrayList.add(item);
        rear++;
    }
}
