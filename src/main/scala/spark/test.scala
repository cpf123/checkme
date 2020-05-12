package spark

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}


object test {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("Day05")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    sc.setLogLevel("DEBUG")
    val constant = 1
    val nonSerializableObj = new Object
//    val df = sc.emptyRDD[Int].map { _ => constant }
    import sqlContext.implicits._
    val df = Seq(
      (BigDecimal("10000000000000000000"), 1),// 过20溢出
      (BigDecimal("10000000000000000000"), 1),
      (BigDecimal("10000000000000000000"), 2),
      (BigDecimal("10000000000000000000"), 2),
      (BigDecimal("10000000000000000000"), 2),
      (BigDecimal("10000000000000000000"), 2),
      (BigDecimal("10000000000000000000"), 2),
      (BigDecimal("10000000000000000000"), 2),
      (BigDecimal("10000000000000000000"), 2),
      (BigDecimal("10000000000000000000"), 2),
      (BigDecimal("10000000000000000000"), 2),
      (BigDecimal("10000000000000000000"), 2)).toDF("decNum", "intNum")
//    df.select("decNum").show(1)
    val df2 = df.withColumnRenamed("decNum", "decNum2").join(df, "intNum").groupBy("intNum").agg(("decNum","sum"))



  }

}
