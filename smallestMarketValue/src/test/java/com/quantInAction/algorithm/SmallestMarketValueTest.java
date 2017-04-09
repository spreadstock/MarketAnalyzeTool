package com.quantInAction.algorithm;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;

public class SmallestMarketValueTest {
	
	
	SmallestMarketValue aProcessor = new SmallestMarketValue();

	@Before
	public void setUp() throws Exception {
		String master = "local[*]";
		SparkConf conf = new SparkConf()
		        .setAppName(SmallestMarketValue.class.getName())
		        .setMaster(master);
		SparkSession spark = SparkSession
				  .builder()
				  .appName("Java Spark SQL basic example")
				  .config(conf)
				  .getOrCreate();
		
		aProcessor.setTaskConfiguration(spark, "C:\\important\\ideas\\stock", "", "", true);
	}

	@Test
	public void testRun() {
		aProcessor.run();
	}

}
