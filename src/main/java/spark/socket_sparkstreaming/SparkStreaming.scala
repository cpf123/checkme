package spark.socket_sparkstreaming

import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object SparkStreaming {
  /**
   * http://localhost:4040/jobs/
   * @param args
   */
//  def main(args: Array[String]): Unit = {
//    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("PrintWebsites")
//    sparkConf.set("spark.dynamicAllocation.enabled", "false")
//    sparkConf.set("spark.executor.cores", "2")
//    sparkConf.set("spark.executor.memory", "1g")
//    val ssc = new StreamingContext(sparkConf, Seconds(4))
//
//    //    val output = "F:\\Code\\scala2.10.6_spark1.6_hadoop2.8\\out\\gettedWebsites"
//
//    val lines = ssc.socketTextStream("localhost", 9000)
//
//    val words: DStream[String] = lines.flatMap(_.split("\\s"))
//    val wordAndOne: DStream[(String, Int)] = words.map((_, 1))
//    val wordCount: DStream[(String, Int)] = wordAndOne.reduceByKey(_ + _)
//
//    //得到结果，这个结果可以进行输出显示，也可以进行输出到数据库中。
//
//    //websiteLines.repartition(1).saveAsTextFiles(output)
//    wordCount.print()
//
//    ssc.start()
//    ssc.awaitTermination()
//  }
  def main(args: Array[String]): Unit = {
    // Spark Streaming程序以StreamingContext为起点，其内部维持了一个SparkContext的实例。
    // 这里我们创建一个带有两个本地线程的StreamingContext，并设置批处理间隔为8秒。
    val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(1))
    // 在一个Spark应用中默认只允许有一个SparkContext，默认地spark-shell已经为我们创建好了
    // SparkContext，名为sc。因此在spark-shell中应该以下述方式创建StreamingContext，以
    // 避免创建再次创建SparkContext而引起错误：
    // val ssc = new StreamingContext(sc, Seconds(8))
    // 创建一个从TCP连接获取流数据的DStream，其每条记录是一行文本
    val lines = ssc.socketTextStream("192.168.88.80", 8048) // tcp
    // 对DStream进行转换，最终得到计算结果
    //val res = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    val res = lines.flatMap(word=>word.split(" ")).map((_, 1)).reduceByKey(_ + _)
    // 打印该DStream中每个RDD中的前十个元素
    res.print()
    // 执行完上面代码，Spark Streaming并没有真正开始处理数据，而只是记录需在数据上执行的操作。
    // 当我们设置好所有需要在数据上执行的操作以后，我们就可以开始真正地处理数据了。如下：
    ssc.start() // 开始计算
    ssc.awaitTermination() // 等待计算终止
    //启动后，通过：nc -lk 8048，可以向端口发送字符串，spark可以接受到字符串，并处理
  }

}

