package flink.Streaming_Batch

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
/**
 * 滑动窗口计算
 *
 * 每一秒统计最近2秒内的数据，打印到控制台
 *
 * */
object SockWindowCountScala {
  def main(args: Array[String]): Unit = {
    //获取socket端口号
    val port: Int = try{
      ParameterTool.fromArgs(args).getInt("port")
    }catch{
      case e:Exception=>{
        System.err.println("No port set use default port 9000")
      }
        9000
    }
    //获取运行环境
    val env:StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //获取运行环境
    val text = env.socketTextStream("localhost",port,'\n')

    //解析数据(把数据打平)，分组，窗口计算，并且聚合求sum

    //必须导入隐式转换，否则会报错
    import org.apache.flink.api.scala._
    val windowcounts = text.flatMap(line => line.split("\\s"))
      .map(w => wordwithcount(w, 1))
      .keyBy("word")
      .timeWindow(Time.seconds(2), Time.seconds(1))
      .sum("count")



    //打印到控制台
    windowcounts.print().setParallelism(1)

    //执行任务
    env.execute("scoket window count")

  }
  case class wordwithcount(word:String,count:Long)

}
