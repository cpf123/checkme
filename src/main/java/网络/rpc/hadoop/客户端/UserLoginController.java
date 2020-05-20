package 网络.rpc.hadoop.客户端;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;


import java.io.IOException;
import java.net.InetSocketAddress;


/**
 * 登录控制类
 *
 */
public class UserLoginController {

    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        LoginServiceInterface proxy = RPC.getProxy(
                LoginServiceInterface.class,
                1L,
                new InetSocketAddress("192.168.112.100", 10000),
                conf
        );
        String result = proxy.login("laowang", "123456");
        System.out.println(result);

    }
}

