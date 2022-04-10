package leecode.数组链表实现队列;

/**
 *https://cloud.tencent.com/developer/article/1623644
 */
class QueueNode<T> {
    T data;
    QueueNode<T> next;

}

class MyNodeQueue<T> {
    private QueueNode<T> head;
    private QueueNode<T> end;

    //初始化队列
    public MyNodeQueue() {
        end = head = null;
    }

    boolean isEmpty() {
        return head == null;
    }

    int size() {
        int size = 0;
        QueueNode<T> temp = head;
        while (temp != null) {
            temp = temp.next;
            size++;
        }
        return size;
    }

    //入队
    void enQueue(T t) {
        QueueNode<T> temp = new QueueNode<>();
        temp.data = t;
        temp.next = null;
        if (head == null) {
            head = end = temp;
        } else {
            end.next = temp;
            end = temp;
        }
    }

    //出队
    void deQueue() {
        if (head == null) {
            System.out.println("不存在元素，出队失败");
            return;
        }
        head = head.next;
        if (head == null) {
            end = null;
        }
    }

    //获取队首元素
    T getFront() {
        if (head == null) {
            System.out.println("队列中不存在元素，获取为空");
            return null;
        }
        return head.data;
    }

    //获取队尾元素
    T getBack() {
        if (end == null) {
            System.out.println("队列中不存在元素，获取失败");
            return null;
        }
        return end.data;
    }
}

public class MyNodeQueueDemo {
    public static void main(String[] args) {
        MyNodeQueue<Integer> queue = new MyNodeQueue<>();
        queue.enQueue(1);
        System.out.println("向队列中添加元素");
        System.out.println("头 元素=" + queue.getFront());
        System.out.println("尾 元素=" + queue.getBack());
        System.out.println("大小=" + queue.size());
        queue.enQueue(2);
        System.out.println("向队列中添加元素");
        System.out.println("头 元素=" + queue.getFront());
        System.out.println("尾 元素=" + queue.getBack());
        System.out.println("大小=" + queue.size());
        queue.enQueue(3);
        System.out.println("向队列中添加元素");
        System.out.println("头 元素=" + queue.getFront());
        System.out.println("尾 元素=" + queue.getBack());
        System.out.println("大小=" + queue.size());
        queue.enQueue(4);
        System.out.println("向队列中添加元素");
        System.out.println("头 元素=" + queue.getFront());
        System.out.println("尾 元素=" + queue.getBack());
        System.out.println("大小=" + queue.size());

        System.out.println("------------------");

        queue.deQueue();
        System.out.println("从队列中取出元素");
        System.out.println("头 元素=" + queue.getFront());
        System.out.println("尾 元素=" + queue.getBack());
        System.out.println("大小=" + queue.size());
        queue.deQueue();
        System.out.println("从队列中取出元素");
        System.out.println("头 元素=" + queue.getFront());
        System.out.println("尾 元素=" + queue.getBack());
        System.out.println("大小=" + queue.size());
        queue.deQueue();
        System.out.println("从队列中取出元素");
        System.out.println("头 元素=" + queue.getFront());
        System.out.println("尾 元素=" + queue.getBack());
        System.out.println("大小=" + queue.size());
        queue.deQueue();
        System.out.println("从队列中取出元素");
        System.out.println("头 元素=" + queue.getFront());
        System.out.println("尾 元素=" + queue.getBack());
        System.out.println("大小=" + queue.size());
    }
}