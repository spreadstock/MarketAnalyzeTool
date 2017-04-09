package com.xb;

/**
 * 
 */

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public final class JavaWordCount2 {
  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) throws Exception {

      String file = "";
    if (args.length < 1) {
        file = "file:///C:/Users/exubixu/Desktop/test.txt";
    }
    else
    {
        file = args[0];
    }
    String libpath = System.getProperty("java.library.path");
    System.out.println("libpath=" + libpath);  

    SparkSession spark = SparkSession
      .builder()
      .appName("JavaWordCount")
      .getOrCreate();

    JavaRDD<String> lines = spark.read().textFile(file).javaRDD();

    JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      public Iterator<String> call(String s) {
        return Arrays.asList(SPACE.split(s)).iterator();
      }
    });

    JavaPairRDD<String, Integer> ones = words.mapToPair(
      new PairFunction<String, String, Integer>() {
        public Tuple2<String, Integer> call(String s) {
          return new Tuple2<>(s, 1);
        }
      });

    JavaPairRDD<String, Integer> counts = ones.reduceByKey(
      new Function2<Integer, Integer, Integer>() {
        public Integer call(Integer i1, Integer i2) {
          return i1 + i2;
        }
      });
    

    List<Tuple2<String, Integer>> output = counts.collect();
    for (Tuple2<?,?> tuple : output) {
      System.out.println(tuple._1() + ": " + tuple._2());
    }

    
    spark.stop();
  }
}
