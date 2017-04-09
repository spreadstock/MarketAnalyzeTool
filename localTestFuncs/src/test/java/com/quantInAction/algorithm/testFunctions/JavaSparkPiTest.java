package com.quantInAction.algorithm.testFunctions;

import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;

public class JavaSparkPiTest {
	
	JavaSparkPi aProcessor = new JavaSparkPi();

	@Before
	public void setUp() throws Exception {
		SparkSession spark = SparkSession.builder().appName("JavaSparkPi").master("local[2]").getOrCreate();
		
		aProcessor.setTaskConfiguration(spark, "C:\\important\\ideas\\stock", "2003-04-28", "2003-05-13", true);
	}

	@Test
	public void testRun() {
		aProcessor.run();
	}


}
