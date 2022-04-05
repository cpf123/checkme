package hadoop.关联;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MapSideJoin {

    static class MapSideJoinMapper extends Mapper<LongWritable, Text, Text, NullWritable>{

        //用一个hashmap来保存产品信息表
        Map<String, String> pdInfoMap = new HashMap<String, String>();
        Text k = new Text();

        //setup方法是在maptask处理数据之前调用一次，可以用来做一些初始化工作
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("D:/测试数据/product.txt")));
            String line;
            while(StringUtils.isNotEmpty(line = reader.readLine())){
                String[] split = line.split(",");
                pdInfoMap.put(split[0], split[1]);

            }
            reader.close();
        }

        //由于已经持有完整的产品信息表，所以再map中就可以实现join(连接)逻辑
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String orderLine = value.toString();
            String[] orders = orderLine.split(",");
            String pdName = pdInfoMap.get(orders[2]);
            k.set(orderLine + "," + pdName);
            context.write(k, NullWritable.get());

        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(MapSideJoin.class);

        job.setMapperClass(MapSideJoinMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, new Path("D:\\测试数据\\输入"));
        FileOutputFormat.setOutputPath(job, new Path("D:\\测试数据\\输出"));

        //指定需要缓存一个文件到所有的maptask运行节点工作目录
        //job.addArchiveToClassPath(archive); 缓存jar包到task运行节点的classpath中
        //job.addCacheArchive(uri); 缓存压缩包文件到task运行节点的工作目录
        //job.addCacheFile(uri); 缓存普通文件到task运行节点的工作目录
        //job.addFileToClassPath(file); 缓存普通文件到task运行节点的classpath中

        //将产品表文件缓存到task工作节点的工作目录中去
        job.addCacheFile(new URI("file:/D:/测试数据/product.txt"));

        //map端join的逻辑不需要reduce
        job.setNumReduceTasks(0);

        boolean res = job.waitForCompletion(true);
        System.exit(res?0:1);
    }
}
