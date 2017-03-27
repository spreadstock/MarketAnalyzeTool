package com.quantInAction.algorithm;


import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantInAction.stock.StockFileLayout;
import com.quantInAction.stock.StockTotalFileLayout;

import scala.collection.immutable.Seq;

public class SmallestMarketValue implements AlgorithmTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SmallestMarketValue.class);
	private SparkSession aSession;
	private String aBaseDirectory;
	
//	private Timestamp toTimestamp(String aTimestamp) throws ParseException {
//		SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
//		
//		if (aTimestamp!=null) {
//			return new Timestamp( format.parse(aTimestamp).getTime());
//		} else
//		return null;
//			
//	}
	

	@Override
	public void run() {
//		JavaSparkContext jsc = new JavaSparkContext(aSession.sparkContext());
//		JavaRDD<String> abc = jsc.textFile("abc");
//		JavaPairRDD<String, String> pairs = abc.mapToPair(x -> new Tuple2<String,String>(x.split(" ")[0], x) );
//		JavaPairRDD<String, String> allFiles = jsc.wholeTextFiles(aBaseDirectory + "\\projects\\model1\\StockDatas\\2016-08-09-Former_Rehabilitation_leaned");
		
		Dataset<Row> ds= aSession.read().schema(StockFileLayout.stockSchema).option("header", "true").option("dateFormat", "yyyy/MM/dd")
				.csv(aBaseDirectory + "\\projects\\model1\\StockDatas\\test\\*.txt");
		ds.takeAsList(30).forEach(x->LOGGER.info("Stock data : " + x));
		ds.printSchema();
		ds.select("Stock").distinct().show();
		Dataset<Row> fds = ds.filter("Date == '2004-03-18'");
		fds.show();
		
		fds.createOrReplaceTempView("stock");

		//aSession.sql("SELECT * FROM stock").write().format("csv").option("header", true).save(aBaseDirectory + "\\projects\\model1\\StockDatas\\test\\SH600004.out.txt");
		
		Dataset<Row> ds2= aSession.read().schema(StockTotalFileLayout.stockTotalSchema).option("header", "true").option("dateFormat", "yyyy-MM-dd")
				.csv(aBaseDirectory + "\\projects\\model1\\StockDatas\\test\\totalStock\\*.txt");
		ds2.createOrReplaceTempView("totalStock");
		Dataset<Row> ds3 = aSession.sql("SELECT Stock as Stock1,SUM(Total) as totalTotal,SUM(Market) as totalMarket FROM totalStock WHERE Date <= '2004-03-18' GROUP BY stock  ");
		ds3.show();
		Dataset<Row> ds4 = ds3.join(fds, ds3.col("Stock1").equalTo(fds.col("Stock")));
		ds4 .select(ds4.col("Stock"),ds4.col("totalTotal").multiply(ds4.col("Close")), ds4.col("totalMarket").multiply(ds4.col("Close"))).show();
		 



		
		
		

	

		
	}

	@Override
	public void setTaskConfiguration(SparkSession sparkSession, String baseDirectory, String startTimestamp, String endTimestamp) {
		aSession = sparkSession;
		aBaseDirectory = baseDirectory;
		
	}
	
	

}
