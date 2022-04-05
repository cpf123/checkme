package 类加载;

public class Problem1 {
    public static void main(String[] args) {
        // 热替换测试代码
        Thread t;
        t = new Thread(new Multirun());
        t.start();
    }
}
