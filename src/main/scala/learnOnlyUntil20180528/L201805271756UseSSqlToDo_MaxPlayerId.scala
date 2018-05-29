package learnOnlyUntil20180528

//here use two queries. Should try to use one

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object L201805271756UseSSqlToDo_MaxPlayerId {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)
    val session = SparkSession.builder().appName("L201805271032SessionSalaries").master("local[1]").getOrCreate()
    val salariesDS = session.read
      .option("header", "true")
      .option("inferSchema", value = true)
      .csv("D:/CN7000From201805211407/Lahman Baseball/Salaries.csv")

    salariesDS.createOrReplaceTempView("salaries")

    val select2010_teamIDMaxSalAndPlayerID = session.sql("SELECT teamID, " +
      "MAX(salary) Max_Salary, FIRST(playerID) PlayerID " +
      "From salaries " +
      "WHERE yearID = '2010' " +
      "GROUP BY teamID " +
      "ORDER BY Max_Salary desc")

    select2010_teamIDMaxSalAndPlayerID.createOrReplaceTempView("salaries1")

    select2010_teamIDMaxSalAndPlayerID.show(100)

    //val count = session.sql("SELECT COUNT(teamID) FROM salaries").show()
    val select2010_teamIDNoOfPlayerTotSalAvgSal = session.sql("SELECT teamID teamID1, " +
      "COUNT(teamID) No_of_Players, SUM(salary) Tot_Salary, " +
      "ROUND(AVG(salary)) Avg_Salary, MIN(salary) Min_Salary " +
      "FROM salaries " +
      "WHERE yearID = '2010' " +
      "GROUP BY teamID")

    select2010_teamIDNoOfPlayerTotSalAvgSal.createOrReplaceTempView("salaries2")

    select2010_teamIDNoOfPlayerTotSalAvgSal.show(100)

    /*val select2010_teamIDEtc = session.sql("SELECT s1.teamID, " +
      "COUNT(s2.teamID) No_of_Players, SUM(s2.salary) Tot_Salary, AVG(s2.salary) Avg_Salary, " +
      "MIN(s2.salary) Min_Salary, MAX(s1.salary) Max_Salary, FIRST(s1.playerID) PlayerID " +
      "From salaries s1 JOIN salaries s2 " +
      "ON s1.teamID = s2.teamID " +
      "AND s1.yearID = s2.yearID " +
      "WHERE s2.yearID = '2010' " +
      "AND s1.yearID = '2010' " +
      "GROUP BY s1.teamID " +
      "ORDER BY Max_Salary desc")

    select2010_teamIDEtc.show(100)*/
    //not correct. try to find the reason

    val select2010_teamIDEtc = session.sql("SELECT teamID, No_of_Players, " +
      "Tot_Salary, Avg_Salary, Min_Salary, Max_Salary, PlayerID " +
      "FROM salaries1 JOIN salaries2 " +
      "ON teamID = teamID1 " +
      "ORDER BY Max_Salary desc")

    select2010_teamIDEtc.show(100)
  }
}