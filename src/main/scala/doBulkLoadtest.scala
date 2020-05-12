

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.commons.codec.digest.DigestUtils
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.client.{HTable, Table, _}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{HFileOutputFormat2, LoadIncrementalHFiles}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, KeyValue, TableName}
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by hun on 2016/10/17.
 *
 * 终于成功了
 *
 */
object doBulkLoadtest {

  def main(args: Array[String]): Unit = {

    //创建sparkcontext,用默认的配置
    //val sc = new SparkContext(new SparkConf())
    val sc = new SparkContext("local", "app name")
    //hbase的列族
    val columnFamily1 = "f1"
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("hbase.zookeeper.quorum", "120.27.111.55")

    val res1 = sc.textFile("file:///E:/BaiduYunDownload/data1").map(x =>
      x.replaceAll("<|>", "")
    ).distinct();
    val res2 = res1.filter(x =>
      x.contains("REC")
    )

    val sourceRDD = res2.flatMap(x => {
      val arg0 = x.split(",")
      val arg1 = arg0.map(y =>
        y.replaceFirst("=", ",")
      ).filter(s =>
        s.split(",").length > 1
      )
      //arg0(10).replaceFirst("=",",").split(",")(0).contat(arg0(10).replaceFirst("=",",").split(",")(0))
      // val key1=Bytes.toBytes(arg0(11).replaceFirst("=",",").split(",")(0).concat(arg0(17).replaceFirst("=",",").split(",")(1)));
      val sdf = new SimpleDateFormat("yyyyMMdd")
      val date = (Long.MaxValue - sdf.parse(arg0(11).replaceFirst("=", ",").split(",")(1)).getTime).toString
      val key = DigestUtils.md5Hex(date).concat(arg0(17).replaceFirst("=", ",").split(",")(1));
      // println(arg0(11).replaceFirst("=",",").split(",")(1).concat(arg0(17).replaceFirst("=",",").split(",")(1)))

      val arg2 = arg1.map(z =>
        (key, (columnFamily1, z.split(",")(0), z.split(",")(1)))
      ).sorted

      arg2
      // arg0.
    }
    )
    val source = sourceRDD.sortBy(_._1)
    source.foreach(println)
    val date = new Date().getTime
    val rdd = source.map(x => {
      //将rdd转换成HFile需要的格式,我们上面定义了Hfile的key是ImmutableBytesWritable,那么我们定义的RDD也是要以ImmutableBytesWritable的实例为key
      //KeyValue的实例为value
      //rowkey
      val rowKey = x._1
      val family = x._2._1
      val colum = x._2._2
      val value = x._2._3
      (new ImmutableBytesWritable(Bytes.toBytes(rowKey)),
        new KeyValue(Bytes.toBytes(rowKey), Bytes.toBytes(family), Bytes.toBytes(colum), date, Bytes.toBytes(value)))
    })

    rdd.foreach(print)

    //生成的HFile的临时保存路径
    val stagingFolder = "file:///E:/BaiduYunDownload/data12"
    //将日志保存到指定目录

    rdd.saveAsNewAPIHadoopFile(stagingFolder,
      classOf[ImmutableBytesWritable],
      classOf[KeyValue],
      classOf[HFileOutputFormat2],
      conf)
    //此处运行完成之后,在stagingFolder会有我们生成的Hfile文件


    //开始即那个HFile导入到Hbase,此处都是hbase的api操作
    val load = new LoadIncrementalHFiles(conf)
    //hbase的表名
    val tableName = "output_table"
    //创建hbase的链接,利用默认的配置文件,实际上读取的hbase的master地址
    val conn = ConnectionFactory.createConnection(conf)
    //根据表名获取表
    val table: Table = conn.getTable(TableName.valueOf(tableName))
    //print(table.getTableDescriptor()+"eeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
    try {
      //获取hbase表的region分布
      // val regionLocator = conn.getRegionLocator(TableName.valueOf(tableName))
      //创建一个hadoop的mapreduce的job
      val job = Job.getInstance(conf)
      //设置job名称
      job.setJobName("DumpFile")
      //此处最重要,需要设置文件输出的key,因为我们要生成HFil,所以outkey要用ImmutableBytesWritable
      job.setMapOutputKeyClass(classOf[ImmutableBytesWritable])
      //输出文件的内容KeyValue
      job.setMapOutputValueClass(classOf[KeyValue])
      //配置HFileOutputFormat2的信息
      //HFileOutputFormat2.configureIncrementalLoad(job, table, regionLocator)
      HFileOutputFormat2.configureIncrementalLoadMap(job, table)

      //开始导入
      load.doBulkLoad(new Path(stagingFolder), table.asInstanceOf[HTable])
    } finally {
      table.close()
      conn.close()
    }
  }

}
