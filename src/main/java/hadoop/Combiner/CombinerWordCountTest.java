package hadoop.Combiner;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class CombinerWordCountTest {
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
        job.setJarByClass(CombinerWordCountTest.class);
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

        //通过job设置combiner处理类，其实逻辑上和我们的reducer是一模一样的【map先处理，减少中间的传输量】
        //注意适用的场景【求和、次数比较适用】【平均数不适合--中间不能进行先合并】
        job.setCombinerClass(MyReduce.class);
        /**
         *      setCombinerClass
         *         combineClass运行在reduceClass之前，相当于一个小型的reduceClass，key值相
         *         同的value合并成集合，然后再交给reduce处理，这样可以减少map和reduce时间传
         *         输的数量，减少reduce的压力，对运行时间可能有一定的优势
         */


        //设置作业处理的输出路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }

    /**
     * 读取输入文件
     */
    public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //接收到的每一行数据
            String line = value.toString();
            //按照指定分隔符进行分割字符串
            String[] words = line.split(" ");
            //指定字词出现的次数
            LongWritable one = new LongWritable(1);
            //循环单词数组，将单词进行记录和存放
            for (String word : words) {
                //通过上下文将map的处理的结果进行输出
                context.write(new Text(word), one);
            }
        }
    }

    /**
     * 归并操作
     */
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
}
