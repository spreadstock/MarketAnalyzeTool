package com.quantInAction.algorithm.basicFinFunctions;


import org.apache.spark.sql.SparkSession;

public interface AlgorithmTask extends Runnable {
	public void setTaskConfiguration (SparkSession sparkSession,String baseDirectory, String startTimestamp, String endTimestamp, boolean isLocalTest) ;
	

}
