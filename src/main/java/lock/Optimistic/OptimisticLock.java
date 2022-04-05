package lock.Optimistic;

import org.junit.jupiter.api.Test;

@lombok.Data
public class OptimisticLock extends Thread {

    public int version;
    public String data;

    public OptimisticLock(String valueOf, int i, String fdd) {
    }

    //构造方法和getter、setter方法
    public void run() {
        // 1.读数据
        String text = Data.data;
        System.out.println("线程"+ getName() + "，获得的数据版本号为：" + Data.getVersion());
        System.out.println("线程"+ getName() + "，预期的数据版本号为：" + getVersion());
        System.out.println("线程"+ getName()+"读数据完成=========data = " + text);
        // 2.写数据：预期的版本号和数据版本号一致，那就更新
        if(Data.getVersion() == getVersion()){
            System.out.println("线程" + getName() + "，版本号为：" + version + "，正在操作数据");
            synchronized(OptimisticLock.class){
                if(Data.getVersion() == this.version){
                    Data.data = this.data;
                    Data.updateVersion();
                    System.out.println("线程" + getName() + "写数据完成=========data = " + this.data);
                    return ;
                }
            }

        }else{
            // 3. 版本号不正确的线程，需要重新读取，重新执行
            System.out.println("线程"+ getName() + "，获得的数据版本号为：" + Data.getVersion());
            System.out.println("线程"+ getName() + "，预期的版本号为：" + getVersion());
            System.err.println("线程"+ getName() + "，需要重新执行。==============");
        }
    }

}
