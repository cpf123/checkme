import java.util.Date

object test {
  def qsort(a:Array[Int],start:Int,end:Int): Unit ={

    if(start >= end){return}
    var i = start
    var j = end
    val key = a(start)

    while(i!=j) {

      var out = 0
      while (out == 0) {
        if (a(j) < key) {
          a(i) = a(j)
          out = 1
        } else if (j == i) {
          out = 1
        }
        else {
          j = j - 1
        }
      }

      out = 0
      while (out == 0) {
        if (a(i) > key) {
          a(j) = a(i)
          out = 1
        } else if (i == j) {
          out = 1
        } else {
          i = i + 1
        }
      }


    }

    a(i)=key
    qsort(a,start,i-1)
    qsort(a,i+1,end)

  }

  def main(args: Array[String]): Unit = {
//    val arr = Array(10,10,1,1)
////    val arr = Array(10, 7, 2, 4, 7, 62, 3, 4, 2, 1, 8, 9, 19)
//    qsort(arr, 0, arr.length - 1)
//    arr.foreach(println(_))
val myMap: Map[String, String] = Map("key1" -> "value")
    val value1: Option[String] = myMap.get("key1")  // @1
    val value2: Option[String] = myMap.get("key2") // @2

    println(value1.get) // Some("value1")
    println(value2.get) // None

  }
}
