package com.ning.demo.wordcount

import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

/**
  *
  * Created by zhaoshufen
  * User:  zhaoshufen
  * Date: 2018/2/1
  * Time: 21:32
  * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
  */
object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[1]").setAppName(this.getClass.getName)
    val sc = new SparkContext(conf)
    val rnd = new Random()
    val words = for( i  <- 0 to 1000) yield "word" + rnd.nextInt(100)
    sc.parallelize(words).map((_,1)).reduceByKey(_ + _)
      .collect().foreach(item =>{
      println("在这里睡眠")
      Thread.sleep(Integer.MAX_VALUE)
    })
  }
}
