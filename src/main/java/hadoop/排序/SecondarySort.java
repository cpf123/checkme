package hadoop.排序;

import java.io.*;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

/**
 * Mapper：由客户端分片情况决定，客户端获取到输入路径的所有文件，依次对每个文件执行分片，分片大小通过最大分片大小、最小分片大小、hdfs的blocksize综合确定，
 * 分片结果写入job.split提交给yarn，对每个分片分配一个Mapper，即确定了数目。
 * <p>
 * Partition：由PartitionerClass中的逻辑确定，默认情况下使用的HashPartitioner中使用了hash值与reducerNum的余数，即由reducerNum决定，等于Reducer数目。
 * 如果自定义的PartitionerClass中有其他逻辑比如固定了，也可以与Reducer数目无关，但注意这种情况下，如果reducerNum小于分区数则会报错，
 * 如果大于则会产生无任务的reduecer但不会影响结果。但是如果reducerNum只有1个，则不会报错而是所有分区都交给唯一的reducer。
 * <p>
 * Reducer：通过job.setNumReduceTasks 手动设置决定  Task的数量，默认是1
 */
// 二次排序 对两个字段进行排序
// goods_visit2表，包含（goods_id,click_num）两个字段
// 根据商品的点击次数(click_num)进行降序排序，再根据goods_id升序排序，并输出所有商品
public class SecondarySort {
    public static class IntPair implements WritableComparable<IntPair>{
            // 自定义组合key，让类中个每个成员变量都参与计算和比较
        int first;//第一个成员变量
        int second;//第二个成员变量

        public void set(int left, int right) {
            first = left;
            second = right;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }

        @Override
        public void readFields(DataInput in) throws IOException {//反序列化，从流中的二进制转换成IntPair
            first = in.readInt();
            second = in.readInt();
        }

        @Override
        public void write(DataOutput out) throws IOException {//序列化，将IntPair转化成使用流传送的二进制
            out.writeInt(first);
            out.writeInt(second);
        }

        @Override
        public int compareTo(IntPair o) {// 自定义key比较
            if (first != o.first)
                return first < o.first ? 1 : -1;
            else if (second != o.second)
                return second < o.second ? -1 : 1;
            else
                return 0;
        }

        // 由于后面进行了自定义组合key对象的相等比较操作，最好重写hashCode()和equal()方法
        @Override
        public int hashCode() {
            return first * 157 + second;
        }

        @Override
        public boolean equals(Object right) {
            if (right == null)
                return false;
            if (this == right)
                return true;
            if (right instanceof IntPair) {
                IntPair r = (IntPair) right;
                return r.first == first && r.second == second;
            } else
                return false;
        }
    }

    // 分区函数类代码
    public static class FirstPartitioner extends Partitioner<IntPair, IntWritable> {
        @Override
        public int getPartition(IntPair key, IntWritable value, int numPartitions) {
            //int numPartitions=  job.setNumReduceTasks(5);
            /**
             *  数据输入来源：map输出
             *  @param key map输出键值，自定义组合key
             *  @param value map输出value值
             *  @param numPartitions 分区总数，即reduce task个数  Reducer：通过job.setNumReduceTasks 手动设置决定  Task的数量，默认是1
             **/
            // 数字的分区写法：
            // 根据自定义key中first(click_num)乘以127取绝对值，再对numPartions取余来进行分区，主要是为实现了第一次排序
            return Math.abs(key.getFirst() * 127) % numPartitions;
        }
    }

    // 分组函数类代码，即自定义比较器，自定义二次排序策略 只比较a 不比较b
    public static class GroupingComparator extends WritableComparator // 这是一个比较器，需要继承WritableComparator
    {
        protected GroupingComparator() {
            super(IntPair.class, true);
        }

        @Override
        public int compare(WritableComparable w1, WritableComparable w2) {
            // 在reduce阶段，构造一个key对应的value迭代器的时候，只要first相同就属于同一个组，放在一个value迭代器
            IntPair ip1 = (IntPair) w1;
            IntPair ip2 = (IntPair) w2;
            int l = ip1.getFirst();//click_num
            int r = ip2.getFirst();
            return Integer.compare(l, r);//比较click_num大小，相等返回0，小于返回-1，大于返回1
        }
    }

    /**
     * 在Map阶段：
     * 1. 使用job.setInputFormatClass定义的InputFormat将输入的数据集分割成小数据块splites，同时InputFormat提供一个RecordReder的实现。
     * 本实验中使用的是TextInputFormat，他提供的RecordReder会将文本的字节偏移量作为key，这一行的文本作为value。
     * 这就是自定义Map的输入是<LongWritable, Text>的原因。
     * <p>
     * 2. 然后调用自定义Map的map方法，将一个个<LongWritable, Text>键值对输入给Map的map方法。
     * 注意输出应该符合自定义Map中定义的输出<IntPair, IntWritable>。最终是生成一个List<IntPair, IntWritable>。
     * <p>
     * 3. 在map阶段的最后，会先调用job.setPartitionerClass对这个List进行分区，每个分区映射到一个reducer。
     * 每个分区内又调用job.setSortComparatorClass设置的key比较函数类排序。可以看到，这本身就是一个二次排序。
     * 如果没有通过job.setSortComparatorClass设置key比较函数类，则可以使用key实现的compareTo方法进行排序。
     * <p>
     * 将map端输出的<key,value>中的key和value组合成一个新的key（称为newKey），value值不变，变成<(key,value),value>
     * 在针对newKey排序的时候，如果key相同，就再对value进行排序。
     */
    //RecordReder会将文本的字节偏移量作为key value => key+value /value
    public static class Map extends Mapper<LongWritable, Text, IntPair, IntWritable> {
        private final IntPair intkey = new IntPair();
        private final IntWritable intvalue = new IntWritable();//相当于int

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            int left = 0;
            int right = 0;
            if (tokenizer.hasMoreTokens())//如果还存在下一个记录
            {
                left = Integer.parseInt(tokenizer.nextToken());//goods_id
                if (tokenizer.hasMoreTokens())
                    right = Integer.parseInt(tokenizer.nextToken());//click_num
                intkey.set(right, left);
                intvalue.set(left);
                context.write(intkey, intvalue);//组合为新的键<(key,value),value>，即<(click_num,goods_id),goods_id>
            }
        }
    }

    // 在Reduce阶段：
    // 1. reducer接收到所有映射到这个reducer的map输出后，也是会调用job.setSortComparatorClass设置的key比较函数类对所有数据对排序
    // 2. 然后开始构造一个key对应的value迭代器。这时就要用到分组，使用job.setGroupingComparatorClass设置的分组函数类
    //    只要这个比较器比较的两个key相同，他们就属于同一个组，它们的value放在一个value迭代器，而这个迭代器的key使用属于同一个组的所有key的第一个key
    // 3. 最后就是进入Reducer的reduce方法，reduce方法的输入是所有的（key和它的value迭代器），同样注意输入与输出的类型必须与自定义的Reducer中声明的一致
    public static class Reduce extends Reducer<IntPair, IntWritable, Text, IntWritable> {
        private final Text left = new Text();
        private static final Text SEPARATOR = new Text("------------------------------------------------");

        public void reduce(IntPair key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            context.write(SEPARATOR, null);
            left.set(Integer.toString(key.getFirst()));//click_num
            for (IntWritable val : values)//goods_id
                context.write(left, val);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "SecondarySort");
        job.setJarByClass(SecondarySort.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        //设置分区函数类，实现第一次排序
        job.setPartitionerClass(FirstPartitioner.class);

        // 指定分组排序使用的比较器，默认使用key对象(IntPair)自身的compareTo()方法，实现第二次排序
        job.setGroupingComparatorClass(GroupingComparator.class);

        //设置map输出类型
        job.setMapOutputKeyClass(IntPair.class);
        job.setMapOutputValueClass(IntWritable.class);

        //设置reduce输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        TextInputFormat.setMaxInputSplitSize(job, 100); // 分片设置
        TextInputFormat.setMinInputSplitSize(job,1);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

//        job.setNumReduceTasks(1);//设置reduce  Task的数量，默认是1

        String[] otherArgs = new String[]{
                "hdfs://localhost:9000/mr/in/goods_visit2",
                "hdfs://localhost:9000/mr/out/secondarysort/goods_visit2"
        };
        FileInputFormat.setInputPaths(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
