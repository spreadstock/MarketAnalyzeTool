package com.quantInAction.algorithm;

import org.apache.spark.sql.SparkSession;

public interface AlgorithmTask extends Runnable {
	public void setTaskConfiguration (SparkSession sparkSession,String baseDirectory, String startTimestamp, String endTimestamp) ;
	

}
