package hive.UDF;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.HashMap;
import java.util.Map;

public class NationUDF extends UDF {
    Text text = new Text();
    public static Map<String,String> nationMap = new HashMap<>();
    static {
        nationMap.put("China", "中国");
        nationMap.put("Japan", "日本");
        nationMap.put("U.S.A", "美国");
    }


    public Text evaluate(Text nation){
        String nationStr = nation.toString();
        String result = nationMap.get(nationStr);
        if(result == null){
            result = "未知";
        }
        text.set(result);
        return text;
    }
}
