package learnOnlyUntil20180528

import com.sparkTutorial.commons.Utils

import java.lang.System.currentTimeMillis
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}

object L201805212215BaseBallBOS2010Highest {

  def main(args: Array[String]) {

    val startTime = currentTimeMillis()

    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("BOS2010Highest").setMaster("local[*]")  //[1] only one partition. other
    val sc = new SparkContext(conf)                                                // number and * => 2

    val basebSalaries = sc.textFile("D:/CN7000From201805211407/Lahman Baseball/Salaries.csv")
    val r = scala.util.Random

    println(basebSalaries.getNumPartitions)
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
    println(currentTimeMillis()-startTime)  //time used in ms
  }
}

/* output:
2
2010,BOS,AL,lackejo01,18700000
7230
 */