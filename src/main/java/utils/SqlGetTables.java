package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class SqlGetTables {

    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {

        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append(" "); // 添加空格
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }

    public static String readFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        SqlGetTables.readToBuffer(sb, filePath);
        return sb.toString();

    }

    public static ArrayList<String> getTables(String text)  throws IOException {
        ArrayList<String> result = new  ArrayList<>();
        Set<String> st = new TreeSet<String>();
        String[] tmpwords = text.split(" ");
        ArrayList<String> words = new ArrayList<>();

        for(int i=0;i<tmpwords.length;i++){
            if(tmpwords[i].trim().length()>0){
                words.add(tmpwords[i]);
            }
        }
        for(int i=0;i<words.size();i++){
            if(i-1>0 && (words.get(i-1).equals("join") || words.get(i-1).equals("from"))) {
                if(!words.get(i).contains("(") && words.get(i).trim().length()>0&&words.get(i).contains(".")) {
                    //System.out.println(words[i]);
                    st.add(words.get(i));
                }
            }
        }
        for(String s:st){
            result.add(s);
        }

        return result;
    }

    public static void main(String args[])  throws IOException {
        String text = SqlGetTables.readFile("/Users/apple/IdeaProjects/warehouse/etl/ploan/dw_ads/bin/ads_risk_company_financing_info.sh");
        ArrayList<String> al = SqlGetTables.getTables(text);

        for(int i=0;i<al.size();i++){
            System.out.println(al.get(i).toString());
        }
    }
}
