package hadoop.计数器;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @ProjectName CustomCounterDemo
 * @PackageName com.buaa
 * @ClassName MyCounter
 * @Description 假如一个文件，规范的格式是3个字段，“\t”作为分隔符，其中有2条异常数据，一条数据是只有2个字段，一条数据是有4个字段
 * @Author 刘吉超
 * @Date 2016-05-23 20:10:14
 */
public class MyCounter {
    // \t键
    private static String TAB_SEPARATOR = "\t";

    public static class MyCounterMap extends Mapper<LongWritable, Text, Text, Text> {
        // 定义枚举对象
        public static enum LOG_PROCESSOR_COUNTER {
            BAD_RECORDS_LONG, BAD_RECORDS_SHORT
        }

        ;

        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String arr_value[] = value.toString().split(TAB_SEPARATOR);
            if (arr_value.length > 3) {
                /* 自定义计数器 */
                context.getCounter("ErrorCounter", "toolong").increment(1);
                /* 枚举计数器 */
                context.getCounter(LOG_PROCESSOR_COUNTER.BAD_RECORDS_LONG).increment(1);
            } else if (arr_value.length < 3) {
                // 自定义计数器
                context.getCounter("ErrorCounter", "tooshort").increment(1);
                // 枚举计数器
                context.getCounter(LOG_PROCESSOR_COUNTER.BAD_RECORDS_SHORT).increment(1);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        String[] args0 = {
                "hdfs://hadoop2:9000/buaa/counter/counter.txt",
                "hdfs://hadoop2:9000/buaa/counter/out/"
        };
        // 读取配置文件
        Configuration conf = new Configuration();

        // 如果输出目录存在，则删除
        Path mypath = new Path(args0[1]);
        FileSystem hdfs = mypath.getFileSystem(conf);
        if (hdfs.isDirectory(mypath)) {
            hdfs.delete(mypath, true);
        }

        // 新建一个任务
        Job job = new Job(conf, "MyCounter");
        // 主类
        job.setJarByClass(MyCounter.class);
        // Mapper
        job.setMapperClass(MyCounterMap.class);

        // 输入目录
        FileInputFormat.addInputPath(job, new Path(args0[0]));
        // 输出目录
        FileOutputFormat.setOutputPath(job, new Path(args0[1]));

        // 提交任务，并退出
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
