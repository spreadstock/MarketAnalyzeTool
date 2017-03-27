package com.quantInAction.stock;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class StockTotalFileLayout {
	static public final StructType stockTotalSchema = new StructType(new StructField[] {
		    new StructField("Stock", DataTypes.StringType, true, Metadata.empty()),
		    new StructField("Date", DataTypes.DateType, true, Metadata.empty()),
		    new StructField("Total", DataTypes.FloatType, true, Metadata.empty()),
		    new StructField("Market", DataTypes.FloatType, true, Metadata.empty()),
		    new StructField("Limit", DataTypes.FloatType, true, Metadata.empty())
		});
}
