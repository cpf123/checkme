package testjava;

import java.io.File;
import java.util.BitSet;

public class BloomFilter
{
    private static int defaultSize = 2147483647;  //设置Bloom filter m位数组
    private int basic = defaultSize -1;
    private String key = null;
    private static BitSet bits = new BitSet(defaultSize);
    int k = 8;      //设置的hash函数的个数

    public BloomFilter(String key){
        this.key = key;
    }
    //将信息指纹置为1，bitset充当了再用一个随机数产生器 G
    private int changeInteger(int h) {
        return basic & h;
    }
    //k个不同的随机数产生器，用Q控制不同产生k个信息指纹
    private int hashCode(String key,int Q){
        int h = 0;
        int off = 0;
        char val[] = key.toCharArray();
        int len = key.length();
        for (int i = 0; i < len; i++){
            h = (30 + Q) * h + val[off++];
        }
        return changeInteger(h);
    }

    //计算处理的文件或者字符串的hash值
    private int[] lrandom(){
        int[] randomsum = new int[k];
        for(int i = 0; i < k; i++)
            randomsum[i] = hashCode(key,i + 1);
        return randomsum;
    }

    //判断一个是否在Bloom filter中
    private boolean exist(){
        int keyCode[] = lrandom();
        boolean flag = true;
        for(int i = 0; i < k; i++)
            flag = flag&&bits.get(keyCode[i]);
        if(flag){
            return true;
        }
        return false;
    }

    //添加hash值到Bloom filter中
    private void add(){
        if(exist()){
            //System.out.println("Bloom filter已经包含" + key);
            return;
        }
        int keyCode[] = lrandom();
        for(int i = 0; i < k; i++)
            bits.set(keyCode[i]);
    }

    //对Bloom filter初始化
    private boolean set0(){
        if(exist()){
            int keyCode[] = lrandom();
            for(int i = 0; i < k; i++)
                bits.clear(keyCode[i]);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        String str = null;
        BloomFilter f;
        String filePath ="I:/Primary_result/CDCCheck_file/CDCSplited_file_1";
        int p = 0;
        int l = 0;
        File file = new File(filePath);
        File[] tempList = file.listFiles();
        for(int i = 0; i < tempList.length; i++){
            str = tempList[i].getPath();
            f = new BloomFilter(str);
            if(f.exist()){
                System.out.println(tempList[i].getPath()+ "相同文件");
                l ++;
            }
            f.add();
            System.out.println(p++);
        }
        System.out.println(l *1.0/p);
        //System.out.println("Bloom filter为1的位置有：" + bits.toString());
    }
}
