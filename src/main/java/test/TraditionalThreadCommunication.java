package test;

/**
 * @author XXXX 次线程先执行10次，主线程执行20次，循环执行50次
 */
public class TraditionalThreadCommunication {

    /**
     * @param args
     */
    public static void main(String[] args) {

        final Business business = new Business();
        new Thread(new Runnable() {

            public void run() {

                for (int i = 1; i <= 10; i++) {
                    try {
                        Thread.sleep(1000);// 线程的sleep方法应该写在线程的run()方法里，就能让对应的线程睡眠。
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    business.sub(i);

                }

            }
        }).start();

        System.out.println("main");

        new Thread(new Runnable() {

            public void run() {
                for (int i = 1; i <= 10; i++) {
                    business.main(i);
                }
            }

        }).start();

    }

}

class Business {
    private boolean bShouldSub = true;

    public synchronized void sub(int i) {
        while (!bShouldSub) {
            try {
                this.wait(); // 线程等待后 后面不执行 等待其他线程运行 wait 配合while
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("sub " + i);
//        for (int j = 1; j <= 10; j++) {
//            System.out.println("sub thread sequence of " + j + ",loop of " + i);
//        }
        bShouldSub = false;
        this.notify(); // 唤醒 被唤醒并且被执行的线程是从上次阻塞的位置从下开始运行，也就是从wait()方法后开始执行
//        所以判断是否进入某一线程的条件 是用while判断，而不是用If判断判断。
    }

    public synchronized void main(int i) {
        while (bShouldSub) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("main " + i);
//        for (int j = 1; j <= 20; j++) {
//            System.out.println("main thread sequence of " + j + ",loop of " + i);
//        }
        bShouldSub = true;
        this.notify();
    }
}

