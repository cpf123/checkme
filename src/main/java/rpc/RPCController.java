package rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 可以采用基于JSON数据传输的RPC框架；
 * 可以使用NIO或直接使用Netty替代BIO实现；
 * 使用开源的序列化机制，如Hadoop Avro与Google protobuf等；
 * 服务注册可以使用Zookeeper进行管理，能够让应用更加稳定。
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