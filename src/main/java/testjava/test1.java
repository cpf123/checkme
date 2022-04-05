package testjava;

import akka.dispatch.Foreach;
import com.bj58.geo.common.MapType;
import com.bj58.geo.common.geometry.Circle;
import com.bj58.geo.common.geometry.Point;
import com.bj58.geo.poi.contract.IPoiService;
import com.bj58.geo.poi.entity.*;
import com.bj58.spat.scf.client.SCFInit;
import com.bj58.spat.scf.client.proxy.builder.ProxyFactory;
import com.bj58.zhaopin.zcmbase.contract.service.IZcmUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class test1 {

    public static Date dateParse(String pattern, String source) throws ParseException {
        if (null != source && source.trim().length() > 0) {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(source);
        } else {
            return null;
        }
    }

    public String[] testMap2List() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("1", "AA");
        map.put("2", "BB");
        map.put("3", "CC");
        map.put("4", "DD");

        Collection<String> valueCollection = map.values();
        final int size = valueCollection.size();

        List<String> valueList = new ArrayList<String>(valueCollection);

        String[] valueArray = new String[size];
        map.values().toArray(valueArray);
        return valueArray;
    }

    public static void main(String[] args) throws Exception {

//        Object o = null;
//        String s = o + "";
//        System.out.println(s);
//        System.out.println(StringUtils.isNotBlank(s));

        if (true) {
//            System.out.println("1");
        } else if (true) {
            System.out.println("2");
        } else if (true) {
            System.out.println("3");
        }
//        String s = "|1|2";
//        String[] split = s.split("\\|");
//        List<String> strings = Arrays.asList(split);
//        for(String str:split){
//            if(StringUtils.isNotBlank(str)){
//                System.out.println(str);
//            }
//        }
   /*     ArrayList<String> list = Lists.newArrayList();
        list.add(0,"1");
        list.add(1,"3");

        System.out.println(System.currentTimeMillis());*/
//        for (int i=0;i<list.size();i++){
//            System.out.println(list.get(i));
//        }
/*        String[] strings = new test1().testMap2List();
        for (int i=0;i<strings.length;i++){
            System.out.println(strings[i]);
        }*/
//           HashMap<String, Object> hashMap = new HashMap<String, Object>();
//        HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
//        System.out.println(hashMap2.containsKey("1"));
//        hashMap.put("aa", 90.0);
//        String aa = hashMap.getOrDefault("aa", "-")+"";
//        System.out.println(aa);
//        hashMap2.put("aa", 80.0);
////        hashMap.put("cc", 70.0);
//        hashMap.putAll(hashMap2);
//        Iterator iter = hashMap.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            Object key = entry.getKey();
//            Object value = entry.getValue();
//            System.out.println(key + ":" + value);
//
//        }
//        long currentTimestamps = System.currentTimeMillis();
//        long oneDayTimestamps = (long) (60 * 60 * 24 * 1000);
//        long l = currentTimestamps - (currentTimestamps + 60 * 60 * 8 * 1000) % oneDayTimestamps;
//        System.out.println(l);

//        Date date = dateParse("yyyy-MM-dd HH:mm:ss", "1900-01-01 00:00:00");
//        System.out.println(date);
//        SCFInit.init("/Users/Bloomberg/IdeaProjects/DataCheck/src/main/java/test/configuration/scf.config");
//        final String url = "tcp://poi/PoiService" ;
//        IPoiService poiService = ProxyFactory.create(IPoiService.class, url);
//        ExtendPoiSearchRequest request = new ExtendPoiSearchRequest()
//                .withMapType(MapType.Baidu)   // 传入的坐标为百度地图坐标
//                .withCircle(new Circle(new Point(183.236165,39.084494), 1000.0))  // 查询范围为坐标(116.4, 40.0)周边1000米内
//                .withQuery("银行")             // 查询附近银行。支持或查询，关键词以空格分隔，例如 .withQuery("银行 地铁站")。
//                .withTag("银行")               // 查询附近的银行。若明确tag为银行则建议使用该接口而不是withQuery接口。
//                .withTagId(1402)            // 以tagID查询。数据已对所有tag进行编码，见下。该接口支持传入List<Integer>查询多个tagID。
//                .withName("银行")              // 查询名称为银行的数据。若明确是查名称中包含银行，建议使用该接口，而不是withQuery接口。
//                .withCity("北京市")              // 查询城市。数据来源百度地图。
//                .withProvince("北京市")       // 查询省。数据来源百度地图。
//                .withDistrict("朝阳区")        // 查询区域。数据来源百度地图。
//                .withLocal(1)                  // 以cmcs地域查询。该接口支持传入List<Integer>查询多个地域。
//                .startAt(35)                   // 取从第35条开始的记录，缺省为0
//                .withRows(20);                 // 取20条记录，缺省为10。
//        ExtendPoiResultEntity result = poiService.searchPoiExtend(request);
//        for (ExtendPoiEntity poi: result.getPoiList()) {
//            System.out.printf("id: %d, name: %s, location: (%f,%f), distance: %f m\n, address : %s",
//                    poi.getId(), poi.getName(),
//                    poi.getLocation().getLongitude(), poi.getLocation().getLatitude(),
//                    poi.getDistance(), poi.getAddress());
//        }
//        System.out.printf("result count: %d, total count: %d\n", result.getPoiList().size(), result.getTotalCount());
//        System.exit(0);
//
//        IZcmUserService iZcmUserService = ProxyFactory.create(IZcmUserService.class,
//                " tcp://zcmbase/ZcmUserService ");

//        String s="1601263146402_b125cf80-a815-4624-87fa-2da0ce490ad8";
//        String substring = s.substring(0,13);
//        System.out.println(substring);
//        classa classa = new classa();
//        String a = classa.getA();
//        System.out.println(a);


//        Map<Long, ZcmUser> getZcmUsers(List<Long> uids);
    }
//    public String evaluate(String key) {
//        if (key == null) {
//            return null;
//        } else {
//            try {
//                MessageDigest md = MessageDigest.getInstance("MD5");
//                md.update(key.getBytes());
//                byte[] b = md.digest();
//                StringBuffer buf = new StringBuffer("");
//
//                for(int offset = 0; offset < b.length; ++offset) {
//                    int i = b[offset];
//                    if (i < 0) {
//                        i += 256;
//                    }
//
//                    if (i < 16) {
//                        buf.append("0");
//                    }
//
//                    buf.append(Integer.toHexString(i));
//                }
//
//                return buf.toString();
//            } catch (NoSuchAlgorithmException var7) {
//                return null;
//            }
//        }
//    }
}
