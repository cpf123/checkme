package testjava;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 方案描述:
 * 使用数组存储每个请求到来的时间，前10000次请求顺利通过，并填满数组。
 * 后续请求到来时，判断当前时间是否比数组中最早的时间晚1s，未晚，则打回，
 * 晚则替换数组中最早的值。循环。
 */
public class QpsControl {

    /**
     * 接受请求窗口
     */
    private Long[] accessWindow;
    /**
     * 窗口大小
     */
    private int limit;
    /**
     * 指针位置
     */
    private int curPosition;
    /**
     * 时间间隔
     */
    private long period;

    private final Object lock = new Object();

    /**
     * 1秒内最多10000次请求
     * @param limit    限制次数  10000
     * @param period   时间间隔  1
     * @param timeUnit 间隔类型  秒
     */
    public QpsControl(int limit, int period, TimeUnit timeUnit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + limit);
        }
        curPosition = 0;
        this.period = timeUnit.toMillis(period);
        this.limit = limit;
        accessWindow = new Long[limit];
        Arrays.fill(accessWindow, Long.parseLong("0") );
    }

    public boolean isPass() {
        long curTime = System.currentTimeMillis();
        synchronized (lock) {
            if (curTime >= period + accessWindow[curPosition]) {
                accessWindow[curPosition++] = curTime;
                curPosition = curPosition % limit;
                return true;
            } else {
                return false;
            }
        }
    }
}