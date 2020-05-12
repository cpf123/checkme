package hadoop.分区;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


/**
 * Mapper：由客户端分片情况决定，客户端获取到输入路径的所有文件，依次对每个文件执行分片，分片大小通过最大分片大小、最小分片大小、hdfs的blocksize综合确定，
 * 分片结果写入job.split提交给yarn，对每个分片分配一个Mapper，即确定了数目。
 *
 * Partition：由PartitionerClass中的逻辑确定，默认情况下使用的HashPartitioner中使用了hash值与reducerNum的余数，即由reducerNum决定，
 * 等于Reducer数目。如果自定义的PartitionerClass中有其他逻辑比如固定了，也可以与Reducer数目无关，但注意这种情况下，如果reducerNum小于分区数则会报错，
 * 如果大于则会产生无任务的reduecer但不会影响结果。但是如果reducerNum只有1个，则不会报错而是所有分区都交给唯一的reducer。
 *
 * Reducer：通过job.setNumReduceTasks手动设置决定

 */
public class ParitionerTest {

    /**
     * 读取输入文件
     */                                      //map 输入类型 map_in key为偏移量
    public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //接收到的每一行数据
            String line = value.toString();
            //按照指定分隔符进行分割字符串
            String[] words = line.split(" ");
            if (words.length > 1) {
                context.write(new Text(words[0]), new LongWritable(Long.parseLong(words[1])));
            }
        }
    }

    /**
     * 归并操作
     */                                     // reduce 输入类型
    public static class MyReduce extends Reducer<Text, LongWritable, Text, LongWritable> {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            //定义单词出现的总数
            long sum = 0;
            for (LongWritable value : values) {
                //求key出现的次数
                sum += value.get();
            }
            //将统计的结果进行输出
            context.write(key, new LongWritable(sum));
        }
    }

    //定义分类处理                                        map 输出类型
    public static class Mypartitioner extends Partitioner<Text, LongWritable> {

        @Override
        public int getPartition(Text key, LongWritable value, int numPartitions) {
            //int numPartitions=  job.setNumReduceTasks(5);
            if (key.toString().trim().equals("xiaomi")) {
                return 0;
            }
            if (key.toString().trim().equals("huawei")) {
                return 1;
            }
            if (key.toString().trim().equals("iphonex")) {
                return 2;
            }
            if (key.toString().trim().equals("nokia")) {
                return 3;
            }
            return 4;
        }
    }
    //main
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.exit(1);
        }

        //创建配置文件
        Configuration configuration = new Configuration();

        //判断是否存在输出文件--有的话进行删除
        FileSystem fileSystem = FileSystem.get(configuration);

        Path outFilePath = new Path(args[1]);

        boolean is_exists = fileSystem.exists(outFilePath);

        //判断是否存在此文件--存在的话进行删除
        if (is_exists) {
            fileSystem.delete(outFilePath, true);
        }

        //创建job对象
        Job job = Job.getInstance(configuration, "wordcount");
        //设置job的处理类
        job.setJarByClass(ParitionerTest.class); // 任务包
        //设置作业处理的输入路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));

        //设置map相关参数
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //设置reduce相关参数
        job.setReducerClass(MyReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //设置job的partitioner
        job.setPartitionerClass(Mypartitioner.class);
        job.setNumReduceTasks(5);//设置reducer【这里不设置是不会生效的】

        //设置作业处理的输出路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}