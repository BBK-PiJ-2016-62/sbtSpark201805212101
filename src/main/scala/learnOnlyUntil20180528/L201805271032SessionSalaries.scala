package learnOnlyUntil20180528

//see second to last command to find the aim of this programme

import org.apache.log4j.{Level, Logger}
import org.apache.spark
import org.apache.spark.sql
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

object L201805271032SessionSalaries {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val session = SparkSession.builder().appName("L201805271032SessionSalaries").master("local[1]").getOrCreate()
    val salariesDS = session.read
      .option("header", "true")
      .option("inferSchema", value = true)
      .csv("D:/CN7000From201805211407/Lahman Baseball/Salaries.csv")

    // Print the schema in a tree format
    salariesDS.printSchema()

    //import to use $
    import session.implicits._
    salariesDS.select($"yearID"+100).show

    salariesDS.createOrReplaceTempView("salaries")

    val selectYearHhestSalary = session.sql("SELECT yearID Year, teamID, " +
      "FIRST(playerID), MAX(salary) Max_Salary " +
      "FROM salaries " +
      "GROUP BY yearID, teamID " +
      "ORDER BY yearID, MAX(salary) desc")
    selectYearHhestSalary.show(100)

    session.stop()
  }
}
