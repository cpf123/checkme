package spark.kafka


import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 *
 * Spark Streaming 基于 Receiver 对接Kafka  KafkaUtils.createStream
 */
object KafkaReceiver {
  def main(args: Array[String]): Unit = {

    if(args.length != 4){
      System.err.println("Usage: KafkaReceiver <zkQuorum> <groupId> <topics> <numPartitions>")
      System.exit(1)
    }

    val Array(zkQuorum, groupId, topics, numPartitions) = args

    val sparkConf = new SparkConf().setAppName("KafkaReceiver").setMaster("local[3]")
    val ssc = new StreamingContext(sparkConf,Seconds(5))

    /**
     * Create an input stream that pulls messages from Kafka Brokers.
     * @param ssc       StreamingContext object
     * @param zkQuorum  Zookeeper quorum (hostname:port,hostname:port,..)
     * @param groupId   The group id for this consumer
     * @param topics    Map of (topic_name to numPartitions) to consume. Each partition is consumed
     *                  in its own thread
     * @param storageLevel  Storage level to use for storing the received objects
     *                      (default: StorageLevel.MEMORY_AND_DISK_SER_2)
     * @return DStream of (Kafka message key, Kafka message value)
     */
    val topicMap = topics.split(",").map((_,numPartitions.toInt)).toMap
    val messages = KafkaUtils.createStream(ssc,zkQuorum,groupId,topicMap)

    messages.print()
    messages.map(_._2).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    ssc.start()
    ssc.awaitTermination()
  }
}
