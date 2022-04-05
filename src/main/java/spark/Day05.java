package spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day05 {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("Day05");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        List<String> keys = getKeyBySample(jsc);
        System.out.println("导致数据倾斜的key是:" + keys);
        jsc.stop();

        JavaRDD<Integer> rdd = jsc.parallelize(Arrays.asList(6,2,8,4));
        JavaRDD<Integer> res = rdd.sample(false, 0.5);
        res.foreach(x -> System.out.print(x +" "));

//结果：2 8 4

//        JavaRDD<Integer> rdd = jsc.parallelize(Arrays.asList(6,2,8,4));
//        JavaRDD<Integer> res = rdd.sample(true, 2);
//        res.foreach(x -> System.out.print(x +" "));

//结果：6 2 8 8 8 8 8 4
    }

    /**
     * 通过Sample算子进行抽样并把导致数据倾斜的key找出来
     * 然后可以做对计算做针对性的优化
     *
     * @param jsc
     */
    public static List<String> getKeyBySample(JavaSparkContext jsc) {
        List<String> data = Arrays.asList("A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
                "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
                "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
                "B", "B", "B", "B", "B", "B", "B", "B", "C", "D", "E", "F", "G");

        JavaRDD<String> rdd = jsc.parallelize(data, 2);
//        rdd.treeReduce()
        List<Tuple2> item =
                rdd.mapToPair(x -> new Tuple2<String, Integer>(x, 1))
                        .sample(true, 0.4)
                        .reduceByKey((x, y) -> x + y)
                        .map(x -> new Tuple2(x._2, x._1))
                        .sortBy(x -> x._1, false, 2)
                        .take(3);

        List<String> keys = new ArrayList<>();
        System.out.println("keys=" + item);
        for (int i = 0; i < item.size(); i++) {
            if (i == item.size() - 1)
                break;
            Tuple2 current = item.get(i);
            Tuple2 next = item.get(i + 1);
            Integer v1 = Integer.parseInt(current._1.toString());
            Integer v2 = Integer.parseInt(next._1.toString());
            System.out.println(v1 + "   " + v2);

            /**
             * 这儿的逻辑有问题,找出导致数据倾斜的key的方式和具体的业务也有关系
             * 这里只是给了一个简单的判断方法，很有局限性
             */
            if (v1 / v2 >= 3) {
                System.out.println("===");
                keys.add(current._2.toString());
            }
        }
        return keys;
    }
}
