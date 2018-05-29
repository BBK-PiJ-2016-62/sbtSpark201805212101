package learnOnlyUntil20180528

//here use one queries but repeat the COUNT(salary) sub-query

import java.lang.System.currentTimeMillis

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object L201805292255SSqlUseWithFindMSalaries{

  def main(args: Array[String]) {

    val startTime = currentTimeMillis()

    Logger.getLogger("org").setLevel(Level.ERROR)
    val session = SparkSession.builder().appName("L201805281252ShufflingUseSSqlToFindMinSalaries").master("local[*]").getOrCreate()
    val salariesDS = session.read
      .option("header", "true")
      .option("inferSchema", value = true)
      .csv("D:/CN7000From201805211407/Lahman Baseball/SalariesRandom.csv")
    //above change Salaries.csv to SalariesRandom: shuffled to check the reliability of this programme
    //no problem after shuffling!

    salariesDS.createOrReplaceTempView("salaries")

    val simpleJoin = session.sql("WITH salaries1 AS (SELECT s.yearID, s.salary, COUNT(s.salary) Count_Salary " +
      "FROM salaries s GROUP BY s.yearID, s.salary), " +
      "salaries2 AS (SELECT s1.yearID , MAX(s1.Count_Salary) Max_No_Salary " +
      "FROM Salaries1 s1 GROUP BY s1.yearID) " +
      "SELECT s3.yearID Year, s3.salary Min_Salary " +
      "FROM salaries1 s3 " +
      "JOIN salaries2 s4 " +
      "ON s3.yearID = s4.yearID " +
      "AND s3.Count_Salary = s4.Max_No_Salary " +
      "ORDER BY Year")

    /*val simpleJoin = session.sql( "SELECT s.yearID Year, s1.salary Min_Salary FROM " +
      "(SELECT yearID , MAX(Count_Salary) Max_No_Salary FROM " +
        "(SELECT s.yearID, s.salary, COUNT(s.salary) Count_Salary " +
        "FROM salaries s " +
        "GROUP BY s.yearID, s.salary " +
        "ORDER BY s.yearID, s.salary) " +
      "GROUP BY yearID) s " +
      "JOIN " +
      "(SELECT s.yearID, s.salary, COUNT(s.salary) Count_Salary " +
        "FROM salaries s " +
        "GROUP BY s.yearID, s.salary " +
        "ORDER BY s.yearID, s.salary) s1 " +
      "ON s.yearID = s1.yearID " +
      "AND s.Max_No_Salary = s1.Count_Salary " +
      "ORDER BY Year")*/

    simpleJoin.show(50)

    println("Execution Time(ms) : "+(currentTimeMillis()-startTime))
  }
}

//23s