package com.quantInAction.stock;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class StockFileLayout {
	static public final StructType stockSchema = new StructType(new StructField[] {
		    new StructField("Stock", DataTypes.StringType, true, Metadata.empty()),
		    new StructField("Date", DataTypes.DateType, true, Metadata.empty()),
		    new StructField("Open", DataTypes.FloatType, true, Metadata.empty()),
		    new StructField("High", DataTypes.FloatType, true, Metadata.empty()),
		    new StructField("Low", DataTypes.FloatType, true, Metadata.empty()),
		    new StructField("Close", DataTypes.FloatType, true, Metadata.empty()),
		    new StructField("Volume", DataTypes.FloatType, true, Metadata.empty()),
		    new StructField("Adjust", DataTypes.FloatType, true, Metadata.empty()),
		});
	
	

}
