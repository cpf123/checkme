import java.security.MessageDigest

object Md5Util {
  def encrypt32(input: String): String = {
    var md5: MessageDigest = null
    try {
      md5 = MessageDigest.getInstance("MD5")
    }
    catch {
      case e: Exception => {
        e.printStackTrace
        //println(e.getMessage)
      }
    }
    val byteArray: Array[Byte] = input.getBytes
    val md5Bytes: Array[Byte] = md5.digest(byteArray)
    var hexValue: String = ""
    var i: Integer = 0
    for ( i <- 0 to md5Bytes.length-1) {
      val str: Int = (md5Bytes(i).toInt) & 0xff
      //println("str"+str)
      if (str < 16) {
        hexValue=hexValue+"0"
      }
      hexValue=hexValue+(Integer.toHexString(str))
    }
    return hexValue.toString
  }
  def encrypt16(input: String): String ={
    val tem = encrypt32(input)
    return  tem.substring(8,24)
  }
  def main(args: Array[String]) {
    println(encrypt16("niShao1"))
  }
}
