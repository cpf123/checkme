package rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Liuxd on 2018-09-14.
 */
public class RPCController {

    public static int port = 8902;

    public static void main(String[] args) throws IOException {
//      接口服务端
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    RPCServer serviceRPCServer = new RPCServerImpl(port);

                    serviceRPCServer.register(ProducerService.class, ProducerServiceImpl.class);

                    serviceRPCServer.startService();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//      消费者端调用
        ProducerService service = RPCClient.getRemoteProxyObj(ProducerService.class, new InetSocketAddress("localhost", port));
        System.out.println(service.produceCar("兰博基尼"));
    }
}