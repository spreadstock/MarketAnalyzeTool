package com.quantInAction.algorithm.basicFinFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cooeration.marketengine.framework.services.AlgorithmTask;

public class MovingAverage implements AlgorithmTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(MovingAverage.class);
	private SparkSession aSession;
	private String aBaseDirectory;
	private String aStartTime;
	private String aEndTime;
	private boolean aLocalTest;

	@Override
	public void run() {
		Dataset<Row> ds;
		if (aLocalTest) 
			ds= aSession.read().schema(StockFileLayout.stockSchema).option("header", "true").option("dateFormat", "yyyy/MM/dd")
				.csv(aBaseDirectory + "\\projects\\model1\\StockDatas\\test\\*.txt");
		else
			ds= aSession.read().schema(StockFileLayout.stockSchema).option("header", "true").option("dateFormat", "yyyy/MM/dd")
		.csv(aBaseDirectory + "/com/quantInAction/stock/*.txt");
		
		String aNewStartTime = aStartTime;
		try {
			SimpleDateFormat aFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = aFormat.parse(aStartTime);
			Calendar aCal =  Calendar.getInstance();
			aCal.setTime(d1);
			aCal.add(Calendar.DATE, -30);
			aNewStartTime = aFormat.format(aCal.getTime());
			LOGGER.info("New Start Time :" + aNewStartTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Dataset<Row> fds = ds.filter((ds.col("Date").$greater$eq(aNewStartTime)).$amp$amp(ds.col("Date").$less$eq(aEndTime)) ).orderBy("Stock", "Date");
		
		WindowSpec ws = Window.partitionBy("Stock").orderBy("Date").rowsBetween(-4, 0);
		Dataset<Row> fds2 = fds.withColumn("MovingAvg", functions.avg(fds.col("Close")) .over(ws)).select("Stock","Date","MovingAvg").filter((ds.col("Date").$greater$eq(aStartTime)).$amp$amp(ds.col("Date").$less$eq(aEndTime)));
		fds2.show();
		
		if (aLocalTest)
			fds2.repartition(1).write().mode("append").option("header", "true").csv(aBaseDirectory + "\\projects\\model1\\StockDatas\\test\\output\\movingAvg.txt");
		else
			fds2.repartition(1).write().mode("append").option("header", "true").csv(aBaseDirectory + "/com/quantInAction/algorithm/MovingAverage/public");

		

	}

	@Override
	public void setTaskConfiguration(SparkSession sparkSession, String baseDirectory, String startTimestamp,
			String endTimestamp, boolean isLocalTest) {
		aSession = sparkSession;
		aBaseDirectory = baseDirectory;
		aStartTime = startTimestamp;
		aEndTime = endTimestamp;
		aLocalTest = isLocalTest;

	}
	
	public static void main(String[] args) {
		SparkConf conf = new SparkConf()
		        .setAppName("QuantInAction");

		SparkSession spark = SparkSession
				  .builder()
				  .config(conf)
				  .getOrCreate();
		
		MovingAverage aProcessor = new MovingAverage();
		aProcessor.setTaskConfiguration(spark, "/home/hadoop/data", "2003-04-28", "2003-05-13", false);
		aProcessor.run();
	}

}
