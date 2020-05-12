package hadoop.合并小文件;

/**
 *
 */


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.IOUtils;


/**
 * @author Zimo
 * 合并小文件到HDFS
 *
 */
public class MergeSmallFilesToHDFS {

    private static FileSystem hdfs = null; //定义HDFS上的文件系统对象
    private static FileSystem local = null; //定义本地文件系统对象

    /**
     *
     * @function 过滤 regex 格式的文件
     *
     */
    public static class RegexExcludePathFilter implements PathFilter {

        private final String regex;

        public RegexExcludePathFilter(String regex) {
            // TODO Auto-generated constructor stub
            this.regex = regex;
        }

        @Override
        public boolean accept(Path path) {
            // TODO Auto-generated method stub
            boolean flag = path.toString().matches(regex);
            return !flag;
        }

    }

    /**
     *
     * @function 接受 regex 格式的文件
     *
     */
    public static class RegexAcceptPathFilter implements PathFilter {

        private final String regex;

        public RegexAcceptPathFilter(String regex) {
            // TODO Auto-generated constructor stub
            this.regex = regex;
        }

        @Override
        public boolean accept(Path path) {
            // TODO Auto-generated method stub
            boolean flag = path.toString().matches(regex);
            return flag;
        }

    }

    /**
     * @param args
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        // TODO Auto-generated method stub
        list();

    }

    private static void list() throws URISyntaxException, IOException {
        // TODO Auto-generated method stub
        Configuration conf = new Configuration();//读取Hadoop配置文件

        //设置文件系统访问接口，并创建FileSystem在本地的运行模式
        URI uri = new URI("hdfs://Centpy:9000");
        hdfs = FileSystem.get(uri, conf);

        local = FileSystem.getLocal(conf);//获取本地文件系统

        //过滤目录下的svn文件
        FileStatus[] dirstatus = local.globStatus(new Path("D://Code/EclipseCode/mergeSmallFilesTestData/*"),
                new RegexExcludePathFilter("^.*svn$"));

        //获取D:\Code\EclipseCode\mergeSmallFilesTestData目录下的所有文件路径
        Path[] dirs = FileUtil.stat2Paths(dirstatus);
        FSDataOutputStream out = null;
        FSDataInputStream in = null;
        for (Path dir : dirs) {
            //比如拿2018-03-23为例
            //将文件夹名称2018-03-23的-去掉，直接，得到20180323文件夹名称
            String fileName = dir.getName().replace("-", "");//文件名称

            //只接受2018-03-23日期目录下的.txt文件
            FileStatus[] localStatus = local.globStatus(new Path(dir + "/*"),
                    new RegexAcceptPathFilter("^.*txt$"));

            // 获得2018-03-23日期目录下的所有文件
            Path[] listPath = FileUtil.stat2Paths(localStatus);

            // 输出路径
            Path outBlock = new Path("hdfs://Centpy:9000/mergeSmallFiles/result/" + fileName + ".txt");
            System.out.println("合并后的文件名称：" + fileName + ".txt");

            // 打开输出流
            out = hdfs.create(outBlock);

            //循环操作2018-03-23日期目录下的所有文件
            for (Path p : listPath) {
                in = local.open(p);// 打开输入流
                IOUtils.copyBytes(in, out, 4096, false);// 复制数据
                in.close();// 关闭输入流
            }

            if (out != null) {
                out.close();// 关闭输出流
            }
        }

    }

}
