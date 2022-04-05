package 网络.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class receive {
  public void  fun() throws IOException {
      ServerSocket ss =  new ServerSocket(13131);

//监听连接

      Socket s = ss.accept();

//获取输入流，读取数据
      InputStream inputStream = s.getInputStream(); byte[] bys = new byte[1024]; int len = inputStream.read(bys);

      System.out.println(new String(bys, 0, len));

//关闭客户端
      s.close();
//关闭服务端，一般服务端不关闭
      ss.close();
  }
}
