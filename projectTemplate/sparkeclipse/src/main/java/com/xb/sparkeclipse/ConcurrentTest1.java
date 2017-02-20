/**
 * 
 */
package com.xb.sparkeclipse;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * @author exubixu
 *
 */
public class ConcurrentTest1 {

	public static void main(String[] args) throws Exception {

		SparkConf conf = new SparkConf().setAppName("Simple_App");
		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaRDD<String> file1 = sc.textFile("/user/hadoop/input/README.md");
		JavaRDD<String> file2 = sc.textFile("/user/hadoop/input/LICENSE");
		
		System.out.println("file1xubin: "+ file1.count());
	    System.out.println("file2xubin: "+file2.count());
	    
	    sc.stop();
	}
}
