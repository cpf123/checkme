package hadoop.合并小文件;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 当遇到小文件处理时，每个文件会被当成一个split，那么资源消耗非常大，hadoop支持将小文件合并后当成一个切片处理。（默认）
 */
public class SmallFileCombiner {

    static class SmallFileCombinerMapper extends Mapper<LongWritable, Text, Text, NullWritable>{
        NullWritable v = NullWritable.get();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //通过这种方式相当于是直接把值打印到磁盘文件中。value其实就是每一样的的文件内容
            context.write(value, v);
        }

    }

    /**
     * 如果生产环境中，小文件的数量太多，那么累计起来的数量也是很庞大的,那么这时候就要设置切片的大小了。
     *
     * 即使用：CombineTextInputFormat.setMaxInputSplitSize(job, 1024*1024*150);
     */
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(SmallFileCombiner.class);

        job.setMapperClass(SmallFileCombinerMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        //下面的方式将小文件划分为一个切片。
        job.setInputFormatClass(CombineTextInputFormat.class);
        //如果小文件的总和为224M，将setMaxInputSplitSize中的第二个参数设置成300M的时候，在
        //E:\wordcount\output下只会生成一个part-m-00000这种文件
        //如果将setMaxInputSplitSize中的第二个参数设置成150M的时候，在
        //E:\wordcount\output下会生成part-m-00000 和 part-m-00001 两个文件
        CombineTextInputFormat.setMaxInputSplitSize(job, 1024*1024*150); // 最大分片
        CombineTextInputFormat.setInputPaths(job, new Path("e:/wordcount/input/"));
        FileOutputFormat.setOutputPath(job, new Path("e:/wordcount/output/"));

        job.setNumReduceTasks(0);

        job.waitForCompletion(true);
    }
}