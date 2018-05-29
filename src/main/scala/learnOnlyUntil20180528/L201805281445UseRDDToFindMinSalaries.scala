package learnOnlyUntil20180528


import java.lang.System.currentTimeMillis

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object L201805281445UseRDDToFindMinSalaries {

  def main(args: Array[String]) {

    val startTime = currentTimeMillis()

    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("L201805281445UseRDDToFindMinSalaries").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val basebSalaries = sc.textFile("D:/CN7000From201805211407/Lahman Baseball/SalariesRandom.csv")

    val basebMinSalaries = basebSalaries.filter(row => row.split(",")(0) != "yearID")
      .map(row => (((row.split(",")(0).toInt),(row.split(",")(4).toInt)), 1))
      .reduceByKey((first, second)=> (first + second))
      .map(row => (row._1._1, (row._1._2, row._2)))
      .reduceByKey((first, second)=> (if (first._2 > second._2) first else second))
      .map(row => (row._1, row._2._1))
      .sortBy(row => row._1)

    println("Year / Min_Salary\n=================")
    basebMinSalaries.take(50).foreach(println)
    println("Execution Time(ms) : "+(currentTimeMillis()-startTime))  //time used in ms
  }
}