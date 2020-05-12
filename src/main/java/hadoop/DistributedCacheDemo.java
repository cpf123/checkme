package hadoop;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.examples.SecondarySort.Reduce;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.yarn.webapp.example.MyApp.MyController;

public class DistributedCacheDemo {
    public static final String DELIMITER = "\t"; // 字段分隔符

    static class MyMappper extends Mapper<LongWritable, Text, Text, Text> {
        private Map<String, String> userMaps = new HashedMap();

        @Override
        protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            //可以通过localCacheFiles获取本地缓存文件的路径
            //Configuration conf = context.getConfiguration();
            //Path[] localCacheFiles = DistributedCache.getLocalCacheFiles(conf);

            //此处使用快捷方式users.txt访问
            FileReader fr = new FileReader("users.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                //map端加载缓存数据
                String[] splits = line.split(DELIMITER);
                if (splits.length < 2) continue;
                userMaps.put(splits[0], splits[1]);
            }
        }

        ;

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            // 获取记录字符串
            String line = value.toString();
            // 抛弃空记录
            if (line == null || line.trim().equals("")) return;

            String[] values = line.split(DELIMITER);
            if (values.length < 3) return;

            String name = userMaps.get(values[0]);
            Text t_key = new Text(values[0]);
            Text t_value = new Text(name + DELIMITER + values[1] + DELIMITER + values[2]);
            context.write(t_key, t_value);
        }
    }

    private final static String FILE_IN_PATH = "hdfs://cluster1/join/in/login_logs.txt";
    private final static String FILE_OUT_PATH = "hdfs://cluster1/join/out";

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        System.setProperty("hadoop.home.dir", "D:\\desktop\\hadoop-2.6.0");
        Configuration conf = getHAConfiguration();

        // 删除已存在的输出目录
        FileSystem fileSystem = FileSystem.get(new URI(FILE_OUT_PATH), conf);
        if (fileSystem.exists(new Path(FILE_OUT_PATH))) {
            fileSystem.delete(new Path(FILE_OUT_PATH), true);
        }

        //添加分布式缓存文件 可以在map或reduce中直接通过users.txt链接访问对应缓存文件
        DistributedCache.addCacheFile(new URI("hdfs://cluster1/join/in/users.txt#users.txt"), conf);

        Job job = Job.getInstance(conf, "Map Distributed Cache Demo");
        job.setMapperClass(MyMappper.class);
        job.setJarByClass(DistributedCacheDemo.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(FILE_IN_PATH));
        FileOutputFormat.setOutputPath(job, new Path(FILE_OUT_PATH));
        job.waitForCompletion(true);
    }

    private static Configuration getHAConfiguration() {
        Configuration conf = new Configuration();
        conf.setStrings("dfs.nameservices", "cluster1");
        conf.setStrings("dfs.ha.namenodes.cluster1", "hadoop1,hadoop2");
        conf.setStrings("dfs.namenode.rpc-address.cluster1.hadoop1", "172.19.7.31:9000");
        conf.setStrings("dfs.namenode.rpc-address.cluster1.hadoop2", "172.19.7.32:9000");
        //必须配置，可以通过该类获取当前处于active状态的namenode
        conf.setStrings("dfs.client.failover.proxy.provider.cluster1", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        return conf;
    }

}
