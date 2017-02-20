/**
 * 
 */
package com.xb.sparkeclipse;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * @author exubixu
 *
 */
public class ConcurrentTest2 {

	public static void main(String[] args) throws Exception {

	    final JavaSparkContext sc = new JavaSparkContext("local[2]","Simple_App");
	    ExecutorService executorService = Executors.newFixedThreadPool(2);
	    // Start thread 1
	    Future<Long> future1 = executorService.submit(new Callable<Long>() {
	        @Override
	        public Long call() throws Exception {
	            JavaRDD<String> file1 = sc.textFile("/user/hadoop/input/README.md");
	            return file1.count();
	        }
	    });
	    // Start thread 2
	    Future<Long> future2 = executorService.submit(new Callable<Long>() {
	        @Override
	        public Long call() throws Exception {
	            JavaRDD<String> file2 = sc.textFile("/user/hadoop/input/LICENSE");
	            return file2.count();
	        }
	    });
	    // Wait thread 1
	    System.out.println("File1:"+future1.get());
	    // Wait thread 2
	    System.out.println("File2:"+future2.get());
	}
}
