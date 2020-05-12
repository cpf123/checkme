package hadoop.排序;

import org.apache.hadoop.conf.Configuration;
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
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

import java.util.Comparator;
import java.util.TreeMap;
// 非 分组求最值
//求最大的前K个数，用小顶堆
//求最小的前K个数，用大顶堆
public class TopK {

    static class mapJob extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
        //默认的TreeMap升序排列 ,求最大的前K个数，用小顶堆，堆的第一个值是最小的
        //private TreeMap<Integer, Integer> tree = new TreeMap<>();

        //自定义比较器的TreeMap降序排列 ,求最小的前K个数，用大顶堆,堆的第一个值是最大的
        private TreeMap<Integer, Integer> tree = new TreeMap<>(
                new Comparator<Integer>() {
                    public int compare(Integer a, Integer b) {
                        return b - a; // 降序
                    }
                });

        // map 进行tree 累计
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 配置传参
            Configuration conf = context.getConfiguration();
            String ktext = conf.get("k");
            int k = Integer.parseInt(ktext);

            String line = value.toString();
            if (line.length() > 0) {
                Integer i = new Integer(line);

                tree.put(i, i); // 值排序 非聚合数排序 treemap 不允许一键多值 去重作用

                if (tree.size() > k) {
//                    tree.remove(tree.firstKey());
                    tree.remove(tree.lastKey());// 去最小值
                }
            }
        }

        /**
         * 其中如Mapper类下的方法setup()和cleanup()。
         * <p>
         * setup()，此方法被MapReduce框架仅且执行一次，在执行Map任务前，进行相关变量或者资源的集中初始化工作。若是将资源初始化工作放在方法map()中，
         * 导致Mapper任务在解析每一行输入时都会进行资源初始化工作，导致重复，程序运行效率不高！
         * <p>
         * cleanup(),此方法被MapReduce框架仅且执行一次，在执行完毕Map任务后，进行相关变量或资源的释放工作。若是将释放资源工作放入方法map()中，
         * 也会导致Mapper任务在解析、处理每一行文本后释放资源，而且在下一行文本解析前还要重复初始化，导致反复重复，程序运行效率不高！
         *
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        // map
        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (Integer i : tree.values()) {
                context.write(new IntWritable(i), new IntWritable(i)); // map 任务粒度
            }
        }

    }

    // map 完成去重 排序 作用
    // 1 reduce
    static class reduceJob extends Reducer<IntWritable, IntWritable, IntWritable, NullWritable> {
        //默认的TreeMap升序排列
        //private TreeMap<Integer, Integer> tree = new TreeMap<>();

        //自定义比较器的TreeMap降序排列
        private TreeMap<Integer, Integer> tree = new TreeMap<>(new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return b - a;
            }
        });

        @Override
        protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
// 配置传参
            Configuration conf = context.getConfiguration();
            String ktext = conf.get("k");
            int k = Integer.valueOf(ktext);

            for (IntWritable i : values) {
                tree.put(new Integer(i.get()), i.get());
                if (tree.size() > k) {
                    //tree.remove(tree.firstKey());
                    tree.remove(tree.lastKey());// 去最小值
                }
            }
        }

        @Override
        protected void cleanup(Context context)
                throws IOException, InterruptedException {
            for (Integer i : tree.values()) {
                context.write(new IntWritable(i), NullWritable.get());
            }
        }
    }

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        try {
            String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

            if (otherArgs.length != 3) {
                System.err.println("length != 3");
            }
            conf.set("k", otherArgs[2]);


            Job job = new Job(conf);
            job.setJobName("top K");
            job.setJarByClass(TopK.class);

            job.setMapperClass(mapJob.class);
            job.setReducerClass(reduceJob.class);

            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setNumReduceTasks(1);

            FileInputFormat.addInputPath(job, new Path("/usr/local/hadooptempdata/input/topk/"));
            FileOutputFormat.setOutputPath(job, new Path("/usr/local/hadooptempdata/output/topk/"));

            System.exit(job.waitForCompletion(true) ? 0 : 1);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
