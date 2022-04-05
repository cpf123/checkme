package 网络;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Port_check {
    public static void main(String args[]) {
        System.out.println("请输入ip地址和端口区间");
        Scanner sc = new Scanner(System.in);
        String ip = sc.next();
        Integer begin = sc.nextInt();
        Integer end = sc.nextInt();
        if (check(ip, begin, end)) {
            System.out.println("该区间里有可连接的端口并已经被标记为open");
        } else {
            System.out.println("该区间里没有可以连接的端口");
        }
    }

    public static boolean check(String ip, Integer begin, Integer end) {
        if (begin > end) {
            System.out.println("该区间不存在");
            return false;
        }
        ArrayList<Integer> open = new ArrayList<>();
        //实现区间判断
        for (int i = begin; i <= end; i++) {
            if (isConnectable(ip, i)) {
                open.add(i);
            }
        }
        if (open.isEmpty())
            return false;
        else {
            System.out.println("可连接的端口如下：");
            for (int i = 0; i < open.size(); i++)
                System.out.print(open.get(i));
            return true;
        }

    }

    public static boolean isConnectable(String ip, Integer port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}