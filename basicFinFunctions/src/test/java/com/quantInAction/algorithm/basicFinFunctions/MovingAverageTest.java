package com.quantInAction.algorithm.basicFinFunctions;

import static org.junit.Assert.fail;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;

public class MovingAverageTest {
	
	MovingAverage aProcessor = new MovingAverage();

	@Before
	public void setUp() throws Exception {
		String master = "local[*]";
		SparkConf conf = new SparkConf()
		        .setAppName(MovingAverageTest.class.getName())
		        .setMaster(master);
		SparkSession spark = SparkSession
				  .builder()
				  .appName("Java Spark SQL basic example")
				  .config(conf)
				  .getOrCreate();
		
		aProcessor.setTaskConfiguration(spark, "C:\\important\\ideas\\stock", "2003-04-28", "2003-05-13", true);
	}

	@Test
	public void testRun() {
		aProcessor.run();
	}


}
