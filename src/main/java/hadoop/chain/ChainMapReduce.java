package hadoop.chain;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

import java.io.IOException;
import java.net.URI;

public class ChainMapReduce {

    // 定义输入输出路径
    private static final String INPUTPATH = "hdfs://liaozhongmin21:8020/chainFiles/*";
    private static final String OUTPUTPATH = "hdfs://liaozhongmin21:8020/out";

    public static void main(String[] args) {

        try {

            Configuration conf = new Configuration();
            // 创建文件系统
            FileSystem fileSystem = FileSystem.get(new URI(OUTPUTPATH), conf);

            // 判断输出路径是否存在，如果存在则删除
            if (fileSystem.exists(new Path(OUTPUTPATH))) {
                fileSystem.delete(new Path(OUTPUTPATH), true);
            }

            // 创建Job
            Job job = new Job(conf, ChainMapReduce.class.getSimpleName());

            // 设置输入目录
            FileInputFormat.addInputPath(job, new Path(INPUTPATH));
            // 设置输入文件格式
            job.setInputFormatClass(TextInputFormat.class);

            // 设置自定义的Mapper类
            ChainMapper.addMapper(job, FilterMapper1.class, LongWritable.class, Text.class, Text.class, DoubleWritable.class, conf);
            ChainMapper.addMapper(job, FilterMapper2.class, Text.class, DoubleWritable.class, Text.class, DoubleWritable.class, conf);
            ChainReducer.setReducer(job, SumReducer.class, Text.class, DoubleWritable.class, Text.class, DoubleWritable.class, conf);
            // 注：Reducer后面的Mapper也要用ChainReducer进行加载
            ChainReducer.addMapper(job, FilterMapper3.class, Text.class, DoubleWritable.class, Text.class, DoubleWritable.class, conf);

            // 设置自定义Mapper类的输出key和value
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(DoubleWritable.class);

            // 设置分区
            job.setPartitionerClass(HashPartitioner.class);
            // 设置reducer数量
            job.setNumReduceTasks(1);

            // 设置自定义的Reducer类
            // 设置输出的Key和value类型
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(DoubleWritable.class);

            // 设置输出路径
            FileOutputFormat.setOutputPath(job, new Path(OUTPUTPATH));
            // 设置输出格式
            job.setOutputFormatClass(TextOutputFormat.class);

            // 提交任务
            System.exit(job.waitForCompletion(true) ? 0 : 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 过滤掉金额大于10000的记录
     *
     *
     */
    public static class FilterMapper1 extends Mapper<LongWritable, Text, Text, DoubleWritable> {

        // 定义输出的key和value
        private Text outKey = new Text();
        private DoubleWritable outValue = new DoubleWritable();

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, DoubleWritable>.Context context) throws IOException,
                InterruptedException {

            // 获取行文本内容
            String line = value.toString();

            if (line.length() > 0) {
                // 对行文本内容进行切分
                String[] splits = line.split("\t");
                // 获取money
                double money = Double.parseDouble(splits[1].trim());
                // 过滤
                if (money <= 10000) {
                    // 设置合法结果
                    outKey.set(splits[0]);
                    outValue.set(money);
                    // 把合法结果写出去
                    context.write(outKey, outValue);
                }
            }
        }
    }

    /**
     * 过滤掉金额大于100的记录
     *
     *
     */
    public static class FilterMapper2 extends Mapper<Text, DoubleWritable, Text, DoubleWritable> {

        @Override
        protected void map(Text key, DoubleWritable value, Mapper<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException,
                InterruptedException {
            if (value.get() < 100) {

                // 把结果写出去
                context.write(key, value);
            }
        }
    }

    /**
     * 金额汇总
     *
     */
    public static class SumReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
        // 定义输出的value
        private DoubleWritable outValue = new DoubleWritable();

        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Reducer<Text, DoubleWritable, Text, DoubleWritable>.Context context)
                throws IOException, InterruptedException {

            // 定义汇总结果
            double sum = 0;
            // 遍历结果集进行统计
            for (DoubleWritable val : values) {

                sum += val.get();
            }
            // 设置输出value
            outValue.set(sum);
            // 把结果写出去
            context.write(key, outValue);
        }
    }

    /**
     * 过滤商品名称长度小于8的商品
     *
     */
    public static class FilterMapper3 extends Mapper<Text, DoubleWritable, Text, DoubleWritable> {

        @Override
        protected void map(Text key, DoubleWritable value, Mapper<Text, DoubleWritable, Text, DoubleWritable>.Context context) throws IOException,
                InterruptedException {
            // 过滤
            if (key.toString().length() < 8) {
                // 把结果写出去
                System.out.println("写出去的内容为：" + key.toString() + "++++" + value.toString());
                context.write(key, value);
            }
        }
    }
}