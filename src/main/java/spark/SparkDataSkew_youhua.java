package spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SparkDataSkew_youhua {

    public static void main(String[] args) {
        int parallelism = 48;
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("SolveDataSkewWithRandomPrefix");
        sparkConf.set("spark.default.parallelism", parallelism + "");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        JavaPairRDD<String, String> leftRDD = javaSparkContext.textFile("hdfs://hadoop1:8020/apps/hive/warehouse/default/test/")
                .mapToPair((String row) -> {
                    String[] str = row.split(",");
                    return new Tuple2<String, String>(str[0], str[1]);
                });

        JavaPairRDD<String, String> rightRDD = javaSparkContext.textFile("hdfs://hadoop1:8020/apps/hive/warehouse/default/test_new/")
                .mapToPair((String row) -> {
                    String[] str = row.split(",");
                    return new Tuple2<String, String>(str[0], str[1]);
                });

        String[] skewedKeyArray = new String[]{"9500048", "9500096"};
        List<String> addList = new ArrayList<String>();
        for (int i = 1; i <= 24; i++) {
            addList.add(i + "");
        }
        Set<String> skewedKeySet = new HashSet<String>(Arrays.asList(skewedKeyArray));

        Broadcast<Set<String>> skewedKeys = javaSparkContext.broadcast(skewedKeySet);
        Broadcast<List<String>> addListKeys = javaSparkContext.broadcast(addList);

        JavaPairRDD<String, String> leftSkewRDD = leftRDD
                .filter((Tuple2<String, String> tuple) -> skewedKeys.value().contains(tuple._1()))
                .mapToPair((Tuple2<String, String> tuple) -> new Tuple2<String, String>((new Random().nextInt(24) + 1) + "," + tuple._1(), tuple._2()));

        JavaPairRDD<String, String> rightSkewRDD = rightRDD.filter((Tuple2<String, String> tuple) -> skewedKeys.value().contains(tuple._1()))
                .flatMapToPair((Tuple2<String, String> tuple) -> addListKeys.value().stream()
                        .map((String i) -> new Tuple2<String, String>(i + "," + tuple._1(), tuple._2()))
                        .collect(Collectors.toList())
                        .iterator()
                );

        JavaPairRDD<String, String> skewedJoinRDD = leftSkewRDD
                .join(rightSkewRDD, parallelism)
                .mapToPair((Tuple2<String, Tuple2<String, String>> tuple) -> new Tuple2<String, String>(tuple._1().split(",")[1], tuple._2()._2()));

        JavaPairRDD<String, String> leftUnSkewRDD = leftRDD.filter((Tuple2<String, String> tuple) -> !skewedKeys.value().contains(tuple._1())); // 过滤倾斜值

        JavaPairRDD<String, String> unskewedJoinRDD = leftUnSkewRDD.join(rightRDD, parallelism)
                .mapToPair((Tuple2<String, Tuple2<String, String>> tuple) -> new Tuple2<String, String>(tuple._1(), tuple._2()._2()));

        skewedJoinRDD.union(unskewedJoinRDD).foreachPartition((Iterator<Tuple2<String, String>> iterator) -> {
            AtomicInteger atomicInteger = new AtomicInteger();
            iterator.forEachRemaining((Tuple2<String, String> tuple) -> atomicInteger.incrementAndGet());
        });

        javaSparkContext.stop();
        javaSparkContext.close();
    }

}
