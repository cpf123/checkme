package spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class SparkDataSkew {
    public static void main(String[] args) {
        int parallelism = 48;
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("DemoSparkDataFrameWithSkewedBigTableDirect");
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

        leftRDD.join(rightRDD, parallelism)
                .mapToPair((Tuple2<String, Tuple2<String, String>> tuple) -> new Tuple2<String, String>(tuple._1(), tuple._2()._2()))
                .foreachPartition((Iterator<Tuple2<String, String>> iterator) -> {
                    AtomicInteger atomicInteger = new AtomicInteger();
                    iterator.forEachRemaining((Tuple2<String, String> tuple) -> atomicInteger.incrementAndGet());
                });

        javaSparkContext.stop();
        javaSparkContext.close();
    }
}
