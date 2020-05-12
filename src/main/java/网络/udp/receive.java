package 网络.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class receive {
    public void udprecived() throws IOException {
        //创建接收端Socket服务对象
        DatagramSocket dSocket = new DatagramSocket(12621);

        //创建数据包（接收容器）

        byte[] bys = new byte[1024];

        DatagramPacket dPacket = new DatagramPacket(bys, bys.length);

        //调用接收方法
        dSocket.receive(dPacket);

        //数据包解析

        InetAddress address = dPacket.getAddress();

        String hostAddress = address.getHostAddress();

        byte[] data = dPacket.getData();
        String message = new String(data);
        System.out.println(hostAddress + "*********:" + message);

        //资源释放
        dSocket.close();

    }
    public static void main(String[] args) throws IOException {
        receive test02 = new receive();

//        Thread.sleep(1000);
        test02.udprecived();
    }
}
