package hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


import java.io.IOException;

public class HbaseBulkLoad {

    static class MyMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
        private static final String COLUMNNAME1 = "name";
        private static final String COLUMNNAME2 = "age";
        private static final String FAMILYNAME = "f1";

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split(",");
            String rowKey = split[0];
            String name_value = split[1].split(":")[1];
            String age_value = split[2].split(":")[1];
            Put p = new Put(Bytes.toBytes(rowKey),Long.MAX_VALUE-System.currentTimeMillis());// 倒排数据
            p.addColumn(FAMILYNAME.getBytes(), COLUMNNAME1.getBytes(), name_value.getBytes());
            p.addColumn(FAMILYNAME.getBytes(), COLUMNNAME2.getBytes(), age_value.getBytes());

            context.write(new ImmutableBytesWritable(rowKey.getBytes()), p);
        }
    }


    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Connection conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(TableName.valueOf("people"));
        Admin admin = conn.getAdmin();


        String input = args[0];
        String output = args[1];
        Path inPath = new Path(input);
        Path outPath = new Path(output);

        Job job = Job.getInstance(conf, "Bulkload");
        job.setJarByClass(HbaseBulkLoad.class);
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);


        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(HFileOutputFormat2.class);
        FileSystem fs = FileSystem.get(conf);

        if (fs.exists(outPath)) {
            fs.delete(outPath);
        }

        FileInputFormat.setInputPaths(job, inPath);
        FileOutputFormat.setOutputPath(job, outPath);

        HFileOutputFormat2.configureIncrementalLoad(job, table, conn.getRegionLocator(TableName.valueOf("people")));
        boolean b = job.waitForCompletion(true);
        if (b) {
            LoadIncrementalHFiles loadIncrementalHFiles = new LoadIncrementalHFiles(conf);
            loadIncrementalHFiles.doBulkLoad(outPath, admin, table, conn.getRegionLocator(TableName.valueOf("people")));
        }
        System.exit(b ? 0 : 1);

    }
}


