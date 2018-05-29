package learnOnlyUntil20180528

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

object L201805271307SparkSQLExample {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      //.config("spark.some.config.option", "some-value")
      .master("local[1]")  //added by Chiu
      .getOrCreate()

    import spark.implicits._

    runBasicDataFrameExample(spark)

    spark.stop()
  }

  private def runBasicDataFrameExample(spark: SparkSession): Unit = {

    val df = spark.read
      .option("header", "true")
      .option("inferSchema", value = true)
      .csv("D:/CN7000From201805211407/Lahman Baseball/Salaries.csv")

    df.show()

    import spark.implicits._

    df.printSchema()

    df.select("yearID").show()

    df.select($"yearID" + 1).show()

    df.filter($"yearID" > 2010).show()

    df.groupBy("yearID").count().show()

    df.createOrReplaceTempView("salaries")

    val sqlDF = spark.sql("SELECT * FROM salaries")
    sqlDF.show()

    df.createGlobalTempView("salaries")

    spark.sql("SELECT * FROM global_temp.salaries").show()

    spark.newSession().sql("SELECT * FROM global_temp.salaries").show()
  }
}