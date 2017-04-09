package com.quantInAction.algorithm.testFunctions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.SparkSession;

import com.cooeration.marketengine.framework.services.AlgorithmTask;

public class JavaSparkPi implements AlgorithmTask,Serializable {

	private SparkSession spark;

	public static void main(String[] args) throws Exception {
		JavaSparkPi aProcessor = new JavaSparkPi();
		SparkSession spark = SparkSession.builder().appName("JavaSparkPi").master("local[2]").getOrCreate();
		aProcessor.setTaskConfiguration(spark, "C:\\important\\ideas\\stock", "2003-04-28", "2003-05-13", true);
		aProcessor.run();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
		int slices = 2;
		int n = 100000 * slices;
		List<Integer> l = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			l.add(i);
		}

		JavaRDD<Integer> dataSet = jsc.parallelize(l, slices);

		int count = dataSet.map(new Function<Integer, Integer>() {
			@Override
			public Integer call(Integer integer) {
				double x = Math.random() * 2 - 1;
				double y = Math.random() * 2 - 1;
				return (x * x + y * y <= 1) ? 1 : 0;
			}
		}).reduce(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer integer, Integer integer2) {
				return integer + integer2;
			}
		});

		System.out.println("Pi is roughly " + 4.0 * count / n);
		jsc.close();
	}

	@Override
	public void setTaskConfiguration(SparkSession sparkSession, String baseDirectory, String startTimestamp,
			String endTimestamp, boolean isLocalTest) {
		this.spark = sparkSession;

	}
}
