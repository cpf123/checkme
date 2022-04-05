package flink.Streaming_Batch

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

object batchWordCountScala {
  def main(args: Array[String]): Unit = {
    val input = "D:\\data\\file"
    val output = "D:\\data\\result"

    val env = ExecutionEnvironment.getExecutionEnvironment
    val text: DataSet[String] = env.readTextFile(input)

    import org.apache.flink.api.scala._
    val counts = text.flatMap(_.toLowerCase.split("\\s"))
      .filter(_.nonEmpty)
      .map((_, 1))
      .groupBy(0)
      .sum(1)

    counts.writeAsCsv(output,"\n"," ").setParallelism(1)
    env.execute("bath word count")

  }
}
