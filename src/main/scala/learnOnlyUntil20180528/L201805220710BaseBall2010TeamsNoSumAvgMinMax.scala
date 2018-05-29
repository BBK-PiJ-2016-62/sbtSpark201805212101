package learnOnlyUntil20180528

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object L201805220710BaseBall2010TeamsNoSumAvgMinMax {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("BaseBall2010AllTeamsAvgAndHhest").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val basebSalaries = sc.textFile("D:/CN7000From201805211407/Lahman Baseball/Salaries.csv")

    val baseb2010Salaries = basebSalaries.
      filter(line => line.split(",", -1)(0) == "2010").
      map(teamSal => (teamSal.split(",")(1), teamSal.split(",")(4).toFloat))

    val baseb2010TeamsNoSumAvgMinMax = baseb2010Salaries.
      aggregateByKey((0.0, 0.0, 9999999999.0, 0.0))(
        (first, second) => ((first._1 + second/second, first._2 + second,
          (if(first._3 < second) first._3 else second), if(first._4 > second) first._4 else second)),
        (combine1, combine2) => (combine1._1 + combine2._1, combine1._2 + combine2._2,
          (if(combine1._3 < combine2._3) combine1._3 else combine2._3),
          (if(combine1._4 > combine2._4) combine1._4 else combine2._4))
      ).map(toInteger =>
      (toInteger._1, (toInteger._2._1.toInt, toInteger._2._2.toInt, toInteger._2._3.toInt, toInteger._2._4.toInt)))

    println("Salaries paid by baseball teams in 2010:\n\nTeam Code," +
      "(No. of players, Total Salary paid, Min Salary, Max Salary)")
    baseb2010TeamsNoSumAvgMinMax.sortByKey().collect().foreach(println)
  }
}