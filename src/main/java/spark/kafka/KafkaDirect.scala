package spark.kafka


import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 *
 * Spark Streaming 基于 Direct 对接Kafka   KafkaUtils.createDirectStream
 */
object KafkaDirect {
  def main(args: Array[String]): Unit = {

    if(args.length != 2){
      System.err.println("Usage: KafkaDirect <brokers> <topics>")
      System.exit(1)
    }

    val Array(brokers,topics) = args

    val sparkConf = new SparkConf().setAppName("KafkaReceiver").setMaster("local[3]")
    val ssc = new StreamingContext(sparkConf,Seconds(5))

    /**
     * @param ssc StreamingContext object
     * @param kafkaParams Kafka <a href="http://kafka.apache.org/documentation.html#configuration">
     *   configuration parameters</a>. Requires "metadata.broker.list" or "bootstrap.servers"
     *   to be set with Kafka broker(s) (NOT zookeeper servers), specified in
     *   host1:port1,host2:port2 form.
     *   If not starting from a checkpoint, "auto.offset.reset" may be set to "largest" or "smallest"
     *   to determine where the stream starts (defaults to "largest")
     * @param topics Names of the topics to consume
     * @tparam K type of Kafka message key
     * @tparam V type of Kafka message value
     * @tparam KD type of Kafka message key decoder
     * @tparam VD type of Kafka message value decoder
     * @return DStream of (Kafka message key, Kafka message value)
     */
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String,String]("metadata.broker.list"-> brokers)
    val messages= KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](
      ssc,kafkaParams,topicsSet
    )

    messages.print()
    messages.map(_._2).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    ssc.start()
    ssc.awaitTermination()
  }
}
