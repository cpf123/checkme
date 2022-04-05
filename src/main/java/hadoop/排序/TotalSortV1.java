package hadoop.排序;



import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 *  使用一个Reduce进行排序
 *  // 在数据达到reducer之前，MapReduce框架已经按照key值对这些数据按 键 排序了，就是shuffle()
 *     // 如果key为封装的int为IntWritable类型，那么MapReduce按照数字大小对key排序
 *     // 如果Key为封装String的Text类型，那么MapReduce将按照数据字典顺序对字符排序
 *     // 所以一般在map中把要排序的字段使用IntWritable类型，作为key，不排序的字段作为value

 */
public class TotalSortV1 extends Configured implements Tool {
    static class SimpleMapper extends
            Mapper<LongWritable, Text, IntWritable, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value,
                           Context context) throws IOException, InterruptedException {
            IntWritable intWritable = new IntWritable(Integer.parseInt(value.toString()));
            context.write(intWritable, intWritable);
        }
    }

    static class SimpleReducer extends
            Reducer<IntWritable, IntWritable, IntWritable, NullWritable> {
        @Override
        protected void reduce(IntWritable key, Iterable<IntWritable> values,
                              Context context) throws IOException, InterruptedException {
            for (IntWritable value : values)
                context.write(value, NullWritable.get());
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("<input> <output>");
            System.exit(127);
        }

        Job job = Job.getInstance(getConf());
        job.setJarByClass(TotalSortV1.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(SimpleMapper.class);
        job.setReducerClass(SimpleReducer.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(NullWritable.class);

        job.setNumReduceTasks(1);
        job.setJobName("TotalSort");
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new TotalSortV1(), args);
        System.exit(exitCode);
    }
}
