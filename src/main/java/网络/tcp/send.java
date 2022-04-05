package 网络.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class send {
public void fun() throws IOException {
/**
 *     核心API：Socket
 *
 *     流程： 创建客户端socket对象
 *
 *             向服务端请求建立tcp连接
 *
 * ​ 从tcp连接中获取输出流，写数据
 *
 *             释放资源
 */

//创建客户端的socket服务，指定目的主机和端口

    Socket s = new Socket("127.0.0.1", 13131);

//通过socket获取输出流，写数据

    OutputStream outputStream = s.getOutputStream(); outputStream.write("hello ,this is tcp?".getBytes());

//释放资源

    s.close();

}
}
