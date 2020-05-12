package hadoop.关联;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ReduceJoinDemo {
    public static final String DELIMITER = "\t"; // 字段分隔符

    static class MyMappper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
                throws IOException, InterruptedException {

            FileSplit split = (FileSplit) context.getInputSplit();
            String filePath = split.getPath().toString();
            // 获取记录字符串
            String line = value.toString();
            // 抛弃空记录
            if (line == null || line.trim().equals("")) return;

            String[] values = line.split(DELIMITER);
            // 处理user.txt数据
            if (filePath.contains("users.txt")) {
                if (values.length < 2) return;
                context.write(new Text(values[0]), new Text("u#" + values[1]));
            }
            // 处理login_logs.txt数据
            else if (filePath.contains("login_logs.txt")) {
                if (values.length < 3) return;
                context.write(new Text(values[0]), new Text("l#" + values[1] + DELIMITER + values[2]));
            }
        }
    }

    static class MyReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
                throws IOException, InterruptedException {
            // Text key, Iterable<Text> values 相同的key vlaue累加
            LinkedList<String> linkU = new LinkedList<String>();  //users值
            LinkedList<String> linkL = new LinkedList<String>();  //login_logs值

            for (Text tval : values) { // 相同的key 不同的value map 打不同的标签
                String val = tval.toString();
                if (val.startsWith("u#")) {
                    linkU.add(val.substring(2));
                } else if (val.startsWith("l#")) {
                    linkL.add(val.substring(2));// 插入效率高
                }
            }

            for (String u : linkU) {
                for (String l : linkL) {
                    context.write(key, new Text(u + DELIMITER + l));  //相同的key vlaue累加
                }
            }
        }
    }

    private final static String FILE_IN_PATH = "hdfs://cluster1/join/in";
    private final static String FILE_OUT_PATH = "hdfs://cluster1/join/out";

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        System.setProperty("hadoop.home.dir", "D:\\desktop\\hadoop-2.6.0");
        Configuration conf = getHAContiguration();

        // 删除已存在的输出目录
        FileSystem fileSystem = FileSystem.get(new URI(FILE_OUT_PATH), conf);
        if (fileSystem.exists(new Path(FILE_OUT_PATH))) {
            fileSystem.delete(new Path(FILE_OUT_PATH), true);
        }

        Job job = Job.getInstance(conf, "Reduce Join Demo");
        job.setMapperClass(MyMappper.class);
        job.setJarByClass(ReduceJoinDemo.class);
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(FILE_IN_PATH));
        FileOutputFormat.setOutputPath(job, new Path(FILE_OUT_PATH));
        job.waitForCompletion(true);
    }

    private static Configuration getHAContiguration() {
        Configuration conf = new Configuration();
        conf.setStrings("dfs.nameservices", "cluster1");
        conf.setStrings("dfs.ha.namenodes.cluster1", "hadoop1,hadoop2");
        conf.setStrings("dfs.namenode.rpc-address.cluster1.hadoop1", "172.19.7.31:9000");
        conf.setStrings("dfs.namenode.rpc-address.cluster1.hadoop2", "172.19.7.32:9000");
        // 必须配置，可以通过该类获取当前处于active状态的namenode
        conf.setStrings("dfs.client.failover.proxy.provider.cluster1", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        return conf;
    }

}
