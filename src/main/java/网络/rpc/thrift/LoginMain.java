package 网络.rpc.thrift;




import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

import java.net.ServerSocket;


public class LoginMain {
    public static void main(String[] args) throws Exception {
        // Transport
        ServerSocket socket = new ServerSocket(8888);
        TServerSocket serverTransport = new TServerSocket(socket);

        // Processor
        LoginService.Processor<LoginServiceImpl> processor = new  LoginService.Processor<>(new LoginServiceImpl());

        TServer.Args tServerArgs = new TServer.Args(serverTransport);
        tServerArgs.processor(processor);

        // Server
        TServer server = new TSimpleServer(tServerArgs);
        System.out.println("Starting the simple server...");
        server.serve();
    }
}
