package 加密;

import java.security.MessageDigest;

public class md5 {
    public static String encrypt(String src) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(src.getBytes());
        StringBuilder result = new StringBuilder();
        //将字节数组转换成16进制式的字符串
        for (byte b : bytes) {
            //1个byte为8个bit,一个hex(16)进制为4个bit,故1个byte可以用2个hex表示
            String temp = Integer.toHexString(b & 0xff);
            //不足2长度的用0来补充
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            result.append(temp);
        }
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        String imei="-";
        String imei_md5 = encrypt(imei);
        System.out.println(imei_md5);
    }
}
