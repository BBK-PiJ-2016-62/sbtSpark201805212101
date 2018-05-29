package learnOnlyUntil20180528

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object L201805220710BaseBall2010TeamsNoSumAvgMinMaxMaxPlayerId {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("BaseBall2010AllTeamsAvgAndHhest").setMaster("local[*]")
    val sc = new SparkContext(conf)

    //yearID,teamID,lgID,playerID,salary

    val basebSalaries = sc.textFile("D:/CN7000From201805211407/Lahman Baseball/Salaries.csv")

    val baseb2010Salaries = basebSalaries.
      filter(line => line.split(",", -1)(0) == "2010").
      map(teamSal => (teamSal.split(",")(1), (teamSal.split(",")(3), teamSal.split(",")(4).toFloat)))

    val baseb2010TeamsrankFromHhestSal = baseb2010Salaries.
      aggregateByKey((0, 0.0, 9999999999.0, 0.0, "Hello Spark"))(
        (first, second) => ((first._1 + (second._2/second._2).toInt, first._2 + second._2,
          if(first._3 < second._2) first._3 else second._2, if(first._4 > second._2) first._4 else second._2,
          if(first._4 > second._2) first._5 else second._1)),
        (combine1, combine2) => (combine1._1 + combine2._1, combine1._2 + combine2._2,
          (if(combine1._3 < combine2._3) combine1._3 else combine2._3),
          (if(combine1._4 > combine2._4) combine1._4 else combine2._4),
          (if(combine1._4 > combine2._4) combine1._5 else combine2._5))
      ).sortBy (rankHhestSal => -rankHhestSal._2._4
      ).map(rankHhestSal => (rankHhestSal._1, rankHhestSal._2._1, rankHhestSal._2._2.toInt, rankHhestSal._2._2/rankHhestSal._2._1,
                             rankHhestSal._2._3.toInt, rankHhestSal._2._4.toInt, rankHhestSal._2._5)).collect

    val hhest2010SalHistory = basebSalaries.filter(line => line.split(",", -1)(3) == baseb2010TeamsrankFromHhestSal(0)._7).
      map(paidHistory => (paidHistory.split(",")(3), paidHistory.split(",")(0), paidHistory.split(",")(1), paidHistory.split(",")(4))).
      collect

      /*map(rankFromHhestSal =>
       (rankFromHhestSal._2._4, (rankFromHhestSal._1, rankFromHhestSal._2._1, rankFromHhestSal._2._2,
        rankFromHhestSal._2._3, rankFromHhestSal._2._5))
      ).sortByKey(ascending = false)*/

    //val baseBall2010TeamsNoSumAvgMinMaxMaxPlayerId = baseb2010TeamsrankFromHhestSal.
      //map(rankFromH => rankFromH._1, rankFromH._2._1)

    println("Salaries paid by USA baseball teams in 2010:\nTeam Code," +
      "No. of players, Total Salary paid, Avg. Salary, Min Salary, Max Salary, Highest paid Player id")
    baseb2010TeamsrankFromHhestSal.foreach(println)

    println("Salary history of the highest paid player of 2010:")
    hhest2010SalHistory.foreach(println)
  }
}