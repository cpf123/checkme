package hadoop;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

public class sequeneceFile {

    public static void main(String[] args) throws IOException {

        //String seqFsUrl = "hdfs://localhost:9000/user/mjiang/target-seq/sdfgz.seq";
        String seqFsUrl = "user/mjiang/target-seq/sdfgz.seq";

        Configuration conf = new Configuration();
        //conf.set("fs.default.name", "hdfs://venus:9000");
        //conf.set("hadoop.job.user", "mjiang");
        //conf.set("mapred.job.tracker", "venus:9001");

        FileSystem fs = FileSystem.get(URI.create(seqFsUrl), conf);

        Path seqPath = new Path(seqFsUrl);

        //Text key = new Text();
        Text value = new Text();
        String filesPath = "/home/mjiang/java/eclipse/hadoop/sequenceFile/data/sdfgz/";
        File gzFilesDir = new File(filesPath);
        String[] gzFiles = gzFilesDir.list();
        int filesLen = gzFiles.length;
        SequenceFile.Writer writer = null;

        try {//返回一个SequenceFile.Writer实例 需要数据流和path对象 将数据写入了path对象


            writer = SequenceFile.createWriter(fs, conf, seqPath, NullWritable.class, value.getClass());

            //for (int i=0;i<2;i++){

            while (filesLen > 0) {

                File gzFile = new File(filesPath + gzFiles[filesLen - 1]);
                InputStream in = new BufferedInputStream(new FileInputStream(gzFile));
                long len = gzFile.length();
                byte[] buff = new byte[(int) len];
                if ((len = in.read(buff)) != -1) {
                    value.set(buff);
                    writer.append(NullWritable.get(), value);
                    //将每条记录追加到SequenceFile.Writer实例的末尾、
                }

                //process

                System.out.println(gzFiles[filesLen - 1]);

                //key.clear();

                value.clear();

                IOUtils.closeStream(in);

                filesLen--;//!!

            }
            //filesLen = 2; }
        } finally {

            IOUtils.closeStream(writer);

        }
    }
}
