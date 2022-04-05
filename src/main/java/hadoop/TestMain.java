package hadoop;


import java.io.*;
import java.security.SecureRandom;
import java.util.*;


/**
 * 问题分析：
 * Step 1. 首先将这天访问百度的IP从log中读取出来，并写入到一个大文件中，这个过程可以通过程序生成大量随机的IP来模拟；
 * Step 2. 以IPV4为例，IP是32位的（因为IP格式为a.b.c.d，其中a/b/c/d都是0-255的整数），因此可能有2^32个不同的IP，
 * 占据的空间为4 * 4G = 16GB，对于32位的操作系统而言无法完全加载到内存中处理；
 * 解决方案：
 * 分而治之 + hash
 * <p>
 * 分而治之：既然所有的IP数量太大，那么可以将这些IP分在多个不同的小文件中，这样每次处理的小文件所需要的内存空间就小多了；
 * hash：对IP取hash值并取余数（比如1000，就有1000个小文件），这样的话相同的IP可以分配在同一个小文件中；当然，不同的IP也可能会有同样的hash值；
 * 然后依次读取每个小文件，对于单个小文件遍历其中的IP，统计出每个小文件中出现次数最多的IP（放在Hashmap中，IP为key，次数为value）；
 * 从Hashmap中找出value最大的key-value对；
 * <p>
 * 作者：alexlee666
 * 链接：https://www.jianshu.com/p/7171d19b6b41
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
class IpUtil {

    private List<String> keyList = new LinkedList<String>();   //保存每个小文件中次数出现最多的IP
    private int ipMaxNum = 0;   //次数出现最多的值
    private int callNum = 0;

    /**
     * 模拟生成大量的IP（1亿个）并批量写入到同一个大文件中
     */
    public void genIP2BigFile(File ipFile, long numberOfLine) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        long startTime = System.currentTimeMillis();
        try {
            fw = new FileWriter(ipFile, true);
            bw = new BufferedWriter(fw);

            SecureRandom random = new SecureRandom();
            for (int i = 0; i < numberOfLine; i++) {
                bw.write("10." + random.nextInt(255) + "." + random.nextInt(255) + "." + random.nextInt(255) + "\n");
                if ((i + 1) % 1000 == 0) {
                    // 每1000条批量写入文件中，提高效率
                    bw.flush();
                }
            }
            bw.flush();
            long endTime = System.currentTimeMillis();
            System.err.println((endTime - startTime) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * check这个大文件是否已经存在
     */
    public void checkFileExists(File ipFile) {
        if (!ipFile.exists()) {
            try {
                ipFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            gernBigFile(ipFile, 100000000);
            genIP2BigFile(ipFile, 100000000);
            System.out.println(">>>>>> Ip file generated.");
        } else {
            System.out.println(">>>>>> Ip file already existed.");
        }
    }


    /**
     * 对IP求取hash值并取余数（比如1000），将hash值相同的IP放入到同一个小文件中，得到1000个小文件
     */
    public void splitFile(File ipFile, int numberOfFile) {
        Map<Integer, BufferedWriter> bwMap = new HashMap<Integer, BufferedWriter>();//保存每个文件的流对象
        Map<Integer, List<String>> dataMap = new HashMap<Integer, List<String>>();//分隔文件用
        BufferedReader br = null;
        FileReader fr = null;
        BufferedWriter bw = null;
        FileWriter fw = null;
        long startTime = System.currentTimeMillis();
        try {
            fr = new FileReader(ipFile);
            br = new BufferedReader(fr);
            String ipLine = br.readLine();
            //先创建文件及流对象方便使用
            for (int i = 0; i < numberOfFile; i++) {
                File file = new File("/Users/ycaha/Desktop/tmpIps/" + i + ".log");
                bwMap.put(i, new BufferedWriter(new FileWriter(file, true)));
                dataMap.put(i, new LinkedList<String>());
            }
            while (ipLine != null) {
                // 对每个ip求取hash
                int hashCode = ipLine.hashCode();
                hashCode = hashCode < 0 ? -hashCode : hashCode;
                // 对hash值取余数，根据余数分配文件地址
                int fileNum = hashCode % numberOfFile; // 0-1000
                List<String> list = dataMap.get(fileNum);
                list.add(ipLine + "\n");
                if (list.size() % 1000 == 0) { // list满1000 数据量
                    BufferedWriter writer = bwMap.get(fileNum);
                    for (String line : list) {
                        writer.write(line); // 1000 list 刷入 file
                    }
                    writer.flush();
                    list.clear();
                }
                ipLine = br.readLine();
            }
            for (int fn : bwMap.keySet()) {
                List<String> list = dataMap.get(fn);
                BufferedWriter writer = bwMap.get(fn);
                for (String line : list) {
                    writer.write(line);
                }
                list.clear();
                writer.flush();
                writer.close();
            }
            bwMap.clear();
            long endTime = System.currentTimeMillis();
            System.err.println((endTime - startTime) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 读取单个小文件split，并获取小文件中的出现次数最大的IP
     */
    public void read(File split) {
        Map<String, Integer> ipNumMap = new HashMap<String, Integer>();   //保存每个文件中的每个IP出现的次数
        //使用局部变量，不要使用全局变量，以免OOM
        callNum++;
        BufferedReader br = null;
        FileReader fr = null;
        long startTime = System.currentTimeMillis();
        try {
            fr = new FileReader(split);
            br = new BufferedReader(fr);
            String ipLine = br.readLine();
            while (ipLine != null) {
                ipLine = ipLine.trim();
                if (ipNumMap.containsKey(ipLine)) {
                    Integer count = ipNumMap.get(ipLine);
                    count++;
                    ipNumMap.replace(ipLine, count);
                } else {
                    ipNumMap.put(ipLine, 1);
                }
                ipLine = br.readLine();
            }
            Set<String> keys = ipNumMap.keySet();
            for (String key : keys) {
                int value = ipNumMap.get(key);
                if (value > ipMaxNum) {
                    ipMaxNum = value;
                    keyList.add(key); // 链表取最后一个节点
                }
            }

            long endTime = System.currentTimeMillis();
//            totalTime += (endTime - startTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(">>>>>> FileOrder: " + callNum + ", ipMaxNum: " + ipMaxNum + ", key: " + keyList.get(keyList.size() - 1));
    }

}


// 主类
public class TestMain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        File ipFile = new File("/Users/ycaha/Desktop/ipAddr.log");
        IpUtil genIp = new IpUtil();
        genIp.checkFileExists(ipFile);
        long start = System.currentTimeMillis();
        genIp.splitFile(ipFile, 1000);
        File files = new File("/Users/ycaha/Desktop/tmpIps/");
        for (File split : files.listFiles()) {
            genIp.read(split);
        }
        long end = System.currentTimeMillis();
        System.out.println(">>>>>> The whole consumed time in seconds: " + (end - start) / 1000);
    }
}





