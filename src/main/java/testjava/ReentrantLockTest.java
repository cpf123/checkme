package testjava;


import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 使用
 * lock finaly时无需判断是不是已经lock，其他情况都要判断isHeldByCurrentThread.
 */
public class ReentrantLockTest {
    private ReentrantLock lock = new ReentrantLock(false);
    private int value = 0;

    public static void main(String[] args) {
        ReentrantLockTest lt = new ReentrantLockTest();
        ArrayList<Callable<Void>> calllist = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            calllist.add(new AddRunnable(lt));
        }
        ExecutorService exec = Executors.newFixedThreadPool(10);
        try {
            exec.invokeAll(calllist);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exec.shutdown();
        System.out.println("result:" + lt.value);
    }

    private void testLock() {
        try {
            lock.lock();
            Thread.sleep(100);
            value++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void testTryLock() {
        boolean flag = lock.tryLock();
        if (flag) {
            try {
                Thread.sleep(100);
                value++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private void testTryLockTime() {
        boolean flag = false;
        try {
            flag = lock.tryLock(1001, TimeUnit.MILLISECONDS);
            if (flag) {
                Thread.sleep(100);
                value++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (flag) {
                lock.unlock();
            }
        }
    }

    private void testTrylockinterrupt() {
        Thread th0 = new Thread("th0") {
            @Override
            public void run() {
                super.run();
                try {
                    lock.tryLock();
                    Thread.sleep(5000);
                    value++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
        };

        Thread th1 = new Thread("th1") {
            @Override
            public void run() {
                super.run();
                try {
                    lock.lockInterruptibly();
                    value++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
        };

        th0.start();
        th1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        th1.interrupt();
    }

    static class AddRunnable implements Callable<Void> {
        private ReentrantLockTest lt;

        public AddRunnable(ReentrantLockTest lt) {
            this.lt = lt;
        }

        @Override
        public Void call() throws Exception {
            lt.testTrylockinterrupt();
            return null;
        }
    }


}
