package learnOnlyUntil20180528

//here use two queries. Should try to use one

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object L201805280155UseSSqlToFindMaxSalaries3Tables {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)
    val session = SparkSession.builder().appName("L201805271032SessionSalaries").master("local[1]").getOrCreate()
    val salariesDS = session.read
      .option("header", "true")
      .option("inferSchema", value = true)
      .csv("D:/CN7000From201805211407/Lahman Baseball/Salaries.csv")

    salariesDS.createOrReplaceTempView("salaries")

    /*val simpleJoin = session.sql("SELECT s1.yearID, s1.teamID, MAX(s2.Count_Salary) FROM salaries s1 JOIN (SELECT s2.yearID, s2.teamID, COUNT(s2.salary) Count_Salary FROM salaries s2 GROUP BY s2.yearID, s2.teamID) WHERE s1.yearID = s2.yearID AND s1.teamID = s2.teamID GROUP BY s1.yearID, s1.teamID")*/

    val simpleJoin = session.sql("SELECT s2.yearID, s2.salary, COUNT(s2.salary) Count_Salary FROM salaries s2 GROUP BY s2.yearID, s2.salary ORDER BY s2.yearID, s2.salary")

    simpleJoin.show()

    simpleJoin.createOrReplaceTempView("simpleJoin")

    val yearMaxNoSalary = session.sql("SELECT yearID, MAX(Count_Salary)  Max_No_Salary FROM simpleJoin GROUP BY yearID")

    yearMaxNoSalary.show()

    yearMaxNoSalary.createOrReplaceTempView("yearMaxNoSalary")

    val yearMinSalary = session.sql("SELECT s.yearID year, s.salary, s.Count_Salary FROM simpleJoin s JOIN yearMaxNoSalary y ON s.yearID = y.yearID AND s.Count_Salary = y.Max_No_Salary ORDER BY s.yearID")

    yearMinSalary.show(30)
    /*val select2010_teamIDMaxSalAndPlayerID = session.sql("SELECT teamID, " +
      "MAX(salary) Max_Salary, FIRST(playerID) PlayerID " +
      "From salaries " +
      "WHERE yearID = '2010' " +
      "GROUP BY teamID " +
      "ORDER BY Max_Salary desc")

    select2010_teamIDMaxSalAndPlayerID.createOrReplaceTempView("salaries1")

    select2010_teamIDMaxSalAndPlayerID.show(100)*/

    //val count = session.sql("SELECT COUNT(teamID) FROM salaries").show()
    /*val select2010_teamIDNoOfPlayerTotSalAvgSal = session.sql("SELECT teamID teamID1, " +
      "COUNT(teamID) No_of_Players, SUM(salary) Tot_Salary, " +
      "ROUND(AVG(salary)) Avg_Salary, MIN(salary) Min_Salary " +
      "FROM salaries " +
      "WHERE yearID = '2010' " +
      "GROUP BY teamID")

    select2010_teamIDNoOfPlayerTotSalAvgSal.createOrReplaceTempView("salaries2")

    select2010_teamIDNoOfPlayerTotSalAvgSal.show(100)*/

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

    /*val select2010_teamIDEtc = session.sql("SELECT teamID, No_of_Players, " +
      "Tot_Salary, Avg_Salary, Min_Salary, Max_Salary, PlayerID " +
      "FROM salaries1 JOIN salaries2 " +
      "ON teamID = teamID1 " +
      "ORDER BY Max_Salary desc")

    select2010_teamIDEtc.show(100)*/
  }
}