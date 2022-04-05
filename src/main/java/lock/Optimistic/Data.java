package lock.Optimistic;

public class Data {
    //数据版本号
    static int version = 1;
    //真实数据
    static String data = "java的架构师技术栈";
    public static int getVersion(){
        return version;
    }
    public static void updateVersion(){
        version = version + 1;
    }
}
