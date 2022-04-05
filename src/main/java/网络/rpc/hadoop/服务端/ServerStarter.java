package 网络.rpc.hadoop.服务端;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

/**
 * 服务启动类
 */
public class ServerStarter {

    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        RPC.Server server = new RPC.Builder(conf)
                .setInstance(new LoginServiceImpl())
                .setProtocol(LoginServiceInterface.class)
                .setBindAddress("192.168.112.100")
                .setPort(10000)
                .build();
        server.start();
    }

}

