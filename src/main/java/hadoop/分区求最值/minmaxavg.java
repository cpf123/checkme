package hadoop.分区求最值;

//import com.sun.tools.internal.xjc.Driver;
import org.apache.hadoop.io.*;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class minmaxavg {


    public class ScoreMap extends Mapper<LongWritable, Text, Text, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //获取行
            String line = value.toString();
            //按逗号切分
            String[] split = line.split(",");
            //将学生名作为key，分数作为value输出
            context.write(new Text(split[1]), new IntWritable(Integer.parseInt(split[2])));
        }
    }


    public class ScoreReduce extends Reducer<Text, IntWritable, Text, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            //创建List对象存储学生成绩
            List<Integer> scoreList = new ArrayList<>();
            //将学生成绩写入List
            for (IntWritable score : values) {
                scoreList.add(score.get());
            }
            //输出
            double scoreMax = scoreList.stream().mapToDouble(Integer::byteValue).max().getAsDouble();
            double scoreMin = scoreList.stream().mapToDouble(Integer::byteValue).min().getAsDouble();
            double scoreAvg = scoreList.stream().mapToDouble(Integer::byteValue).average().getAsDouble();
            context.write(new Text(key + ":"
                    + "\t" + "最高分" + Math.round(scoreMax) + ","
                    + "\t" + "平均分" + Math.round(scoreAvg) + ","
                    + "\t" + "最低分" + Math.round(scoreMin) + ";"), NullWritable.get());
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        //调用工具类Driver执行驱动程序
//        Driver.run(minmaxavg.class, ScoreMap.class, Text.class, IntWritable.class,
//                ScoreReduce.class, Text.class, NullWritable.class,
//                "D:\\Bigdata\\StudentScore\\studentscore.txt",
//                "D:\\Bigdata\\StudentScore\\Result");
    }


}
