package com.quantInAction.algorithm;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooeration.marketengine.framework.services.AlgorithmTask;
import com.quantInAction.stock.StockFileLayout;

public class ReformatStockData implements AlgorithmTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReformatStockData.class);
	private SparkSession aSession;
	private String aBaseDirectory;

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTaskConfiguration(SparkSession sparkSession, String baseDirectory, String startTimestamp,
			String endTimestamp, boolean isLocalTest) {
		String[] fileNameList = {
				"SH600011.txt",
				"SH600012.txt" 
		};
		
		for (String aName:fileNameList) {
			Dataset<Row> ds= aSession.read().schema(StockFileLayout.stockSchema).option("header", "true").option("dateFormat", "yyyy/MM/dd")
					.csv(aBaseDirectory + "\\projects\\model1\\StockDatas\\test2\\"+aName);
		}
		
		

	}

}
