package spark.socket_sparkstreaming

import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object SparkStreaming {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("PrintWebsites")
    val ssc = new StreamingContext(conf, Seconds(4))

    //    val output = "F:\\Code\\scala2.10.6_spark1.6_hadoop2.8\\out\\gettedWebsites"

    val lines = ssc.socketTextStream("localhost", 9000)

    val words: DStream[String] = lines.flatMap(_.split("\\s"))
    val wordAndOne: DStream[(String, Int)] = words.map((_, 1))
    val wordCount: DStream[(String, Int)] = wordAndOne.reduceByKey(_ + _)

    //得到结果，这个结果可以进行输出显示，也可以进行输出到数据库中。

    //websiteLines.repartition(1).saveAsTextFiles(output)
    wordCount.print()

    ssc.start()
    ssc.awaitTermination()
  }
}

