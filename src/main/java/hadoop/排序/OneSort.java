package hadoop.排序;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

// goods_visit1中包含（商品id ，点击次数）两个字段，内容以“\t”分割
// 对商品点击次数由低到高进行排序
public class OneSort {

    public static class Map extends Mapper<Object, Text, IntWritable, Text> {
        private static Text goods = new Text();
        private static IntWritable num = new IntWritable();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String arr[] = line.split("\t");
            num.set(Integer.parseInt(arr[1]));//把要排序的点击次数字段转化为IntWritable类型并设置为key
            goods.set(arr[0]);//商品id字段设置为value
            context.write(num, goods);//输出<key,value>
        }
    }

    // 在数据达到reducer之前，MapReduce框架已经按照key值对这些数据按键排序了，就是shuffle()
    // 如果key为封装的int为IntWritable类型，那么MapReduce按照数字大小对key排序
    // 如果Key为封装String的Text类型，那么MapReduce将按照数据字典顺序对字符排序
    // 所以一般在map中把要排序的字段使用IntWritable类型，作为key，不排序的字段作为value
    public static class Reduce extends Reducer<IntWritable, Text, IntWritable, Text> {
        @Override
        protected void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                context.write(key, value);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Job job = Job.getInstance();
        job.setJobName("OneSort");
        job.setJarByClass(OneSort.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        Path in = new Path("hdfs://localhost:9000/mr/in/goods_visit1");
        Path out = new Path("hdfs://localhost:9000/mr/out/onesort/goods_visit1");

        FileInputFormat.addInputPath(job, in);
        FileOutputFormat.setOutputPath(job, out);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
