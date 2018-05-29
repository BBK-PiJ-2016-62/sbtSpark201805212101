package learnOnlyUntil20180528

import java.lang.System.currentTimeMillis

import com.sparkTutorial.commons.Utils
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object L201805281329ShufflingRDD {

  def main(args: Array[String]) {

    val startTime = currentTimeMillis()

    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("BOS2010Highest").setMaster("local[*]")  //[1] only one partition. other
    val sc = new SparkContext(conf)                                                // number and * => 2

    val basebSalaries1 = sc.textFile("D:/CN7000From201805211407/Lahman Baseball/Salaries.csv")
    val r = scala.util.Random
    val basebSalaries = basebSalaries1.map(row => (r.nextInt(), row)).sortBy(rdd=>rdd._1).map(rdd=>rdd._2)
    basebSalaries.take(50).foreach(println)
    basebSalaries.saveAsTextFile("D:/CN7000From201805211407/Lahman Baseball/SalariesRandom.csv")

    /*println(basebSalaries.getNumPartitions)
    val basebBOS2010Salaries = basebSalaries.filter(line => {
      val splits = line.split(Utils.COMMA_DELIMITER)
      (splits(0) == "2010" && splits(1) == "BOS")
    })

    val basebBOS2010SalariesHighest = basebBOS2010Salaries.sortBy(row =>
      -row.split(Utils.COMMA_DELIMITER)(4).toFloat)

    //basebBOS2010SalariesHighest.collect().foreach(println)
    println(basebBOS2010SalariesHighest.first)  //the highest salary paid by AL League BOS in 2010 and the player's id
    //basebBOS2010Salaries.saveAsTextFile("out/basebBOS2010Salaries.text")
    //airportsNameAndCityNames.saveAsTextFile("out/airports_by_latitude.text")
    println(currentTimeMillis()-startTime)  //time used in ms*/
  }
}

/* output:
2
2010,BOS,AL,lackejo01,18700000
7230
 */