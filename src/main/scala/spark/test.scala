package spark

import java.util.concurrent.TimeUnit

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import testjava.QpsControl


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

    val rdd = df.rdd.repartition(10)

    rdd.foreachPartition(
    part=>{
      val control = new QpsControl(280, 1, TimeUnit.SECONDS)
      part.foreach(
        x=>{
          val pass = control.isPass
          if(pass){
            println( "Value of a: " + x )
          }else{
            Thread.sleep(1000)
          }
        })
    })

    //    for( a <- 1 to rdd.getNumPartitions){
    //    rdd.mapPartitionsWithIndex((idx, iter) => if (idx == a) iter
    //    else Iterator())
    //    }
    val df2 = df.withColumnRenamed("decNum", "decNum2").join(df, "intNum").groupBy("intNum").agg(("decNum","sum"))



  }

}
