import org.apache.spark.sql.SQLContext
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

case class Info(name: String ,age: Int,gender: String,addr: String)

/**
 * java.lang.IllegalArgumentException: Class is not registered: scala.collection.mutable.WrappedArray$ofRef
 * Note: To register this class use: kryo.register(scala.collection.mutable.WrappedArray$ofRef.class);
 * at com.esotericsoftware.kryo.Kryo.getRegistration(Kryo.java:488)
 */
object KyroTest {
  def main(args: Array[String]) {

    val conf = new SparkConf().setMaster("local[2]").setAppName("KyroTest")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.set("spark.kryo.registrationRequired", "true")
    //设置 spark.kryo.registrationRequired 参数为true，
    // 使用kyro时如果在应用中有类没有进行注册则会报错：
    conf.registerKryoClasses(Array(classOf[Info], classOf[scala.collection.mutable.WrappedArray.ofRef[_]]))
    val sc = new SparkContext(conf)

    val arr = new ArrayBuffer[Info]()

    val nameArr = Array[String]("lsw", "yyy", "lss")
    val genderArr = Array[String]("male", "female")
    val addressArr = Array[String]("beijing", "shanghai", "shengzhen", "wenzhou", "hangzhou")

    for (i <- 1 to 1000000) {
      val name = nameArr(Random.nextInt(3))
      val age = Random.nextInt(100)
      val gender = genderArr(Random.nextInt(2))
      val address = addressArr(Random.nextInt(5))
      arr.+=(Info(name, age, gender, address))
    }
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._
    val rdd = sc.parallelize(arr)
    rdd.toDF.groupBy("U").pivot("I",Seq("I`","I2","I3")).sum("S")

    //序列化的方式将rdd存到内存
    rdd.persist(StorageLevel.MEMORY_ONLY_SER)
    rdd.count()
  }
}
