name := "sbtSpark201805212101"

version := "0.1"

scalaVersion := "2.11.8"

//extra dependencies
val sparkVersion = "2.2.1"

// refer at https://mvnrepository.com
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)
