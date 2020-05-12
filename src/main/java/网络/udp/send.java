package 网络.udp;

import java.io.IOException;
import java.net.*;

public class send {
    public void udpsend() throws IOException {
        // 创建发送端Socket服务对象
        DatagramSocket dSocket = new DatagramSocket();

// 创建数据，打包数据
        String message = "hello ,are u UDP ?";
        byte[] bys = message.getBytes();
        int length = bys.length;
        InetAddress address = InetAddress.getByName("localhost");
        int port = 12621;
        DatagramPacket dPacket = new DatagramPacket(bys, length, address, port);
// 发送数据
        dSocket.send(dPacket);

// 资源释放
        dSocket.close();

    }



    public static void main(String[] args) throws IOException, InterruptedException {

        send test = new send();

//        Thread.sleep(1000);
        test.udpsend();

    }
}
