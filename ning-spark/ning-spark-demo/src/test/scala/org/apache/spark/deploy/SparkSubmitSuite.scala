package org.apache.spark.deploy

import org.scalatest.concurrent.Timeouts
import org.scalatest.{BeforeAndAfterEach, FunSuite, Matchers}

/**
  *
  * Created by zhaoshufen
  * User:  zhaoshufen
  * Date: 2018/1/26
  * Time: 21:39
  * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
  */
class SparkSubmitSuite1 extends FunSuite{
  test("Yarn Client mode"){
    sys.props += ("spark.testing" -> "true")
    val clArgs = Seq(
     /* "--deploy-mode", "client",*/
      "--master", "yarn",
      "--executor-memory", "5g",
      "--executor-cores", "5",
      "--class", "org.SomeClass",
      "--jars", "one.jar,two.jar,three.jar",
      "--driver-memory", "4g",
      "--queue", "thequeue",
      "--files", "file1.txt,file2.txt",
      "--archives", "archive1.txt,archive2.txt",
      "--num-executors", "6",
      "--name", "trill",
      "--conf", "spark.ui.enabled=false",
      "thejar.jar",
      "arg1", "arg2")
    SparkSubmit.main(clArgs.toArray)
  }
}
class SparkSubmitSuite
  extends FunSuite
    with Matchers
    with BeforeAndAfterEach
    with Timeouts {
  test("handles arguments with --key=val") {
    val clArgs = Seq(
      "--jars=one.jar,two.jar,three.jar",
      "--name=myApp")
    val appArgs = new SparkSubmitArguments(clArgs)
    appArgs.jars should include regex (".*one.jar,.*two.jar,.*three.jar")
    appArgs.name should be ("myApp")
  }
  test("a b c d"){

  }

}
