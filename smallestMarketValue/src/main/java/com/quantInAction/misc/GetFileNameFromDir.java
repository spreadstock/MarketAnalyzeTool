package com.quantInAction.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class GetFileNameFromDir {
	static String aBaseDirectory = "C:\\important\\ideas\\stock";
	
	List<String> getFileNameList (String aPath) {
		List<String> aList = new ArrayList<String>();
		
		File folder = new File(aPath);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	String fileName = listOfFiles[i].getName();
		    	
		        aList.add(fileName.substring(0,fileName.indexOf('.')));
		      } 
		    }
		 return aList;   
		
	}
	
	public void addColumn(String path,String fileName) throws IOException{
	    BufferedReader br=null;
	    BufferedWriter bw=null;
	    final String lineSep=System.getProperty("line.separator");

	    try {
	        File file = new File(path, fileName + ".txt");
	        File file2 = new File(path + "\\output\\", fileName+".txt");//so the
	                    //names don't conflict or just use different folders

	        br = new BufferedReader(new InputStreamReader(new FileInputStream(file))) ;
	        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2)));
	        String line = null;
	                    int i=0;
	        for ( line = br.readLine(); line != null; line = br.readLine(),i++)
	        {               

	            String newLine = "";
	        	if (i == 0) {
	            	newLine = "Stock,Date,Open,High,Low,Close,Volume,Adjusted" + lineSep;
	            } else {
	            	String addedColumn = fileName + ",";
	            	newLine = addedColumn + line + lineSep;
	            }
	        	
	            bw.write(newLine);
	    }

	    }catch(Exception e){
	        System.out.println(e);
	    }finally  {
	        if(br!=null)
	            br.close();
	        if(bw!=null)
	            bw.close();
	    }

	}

	public static void main(String[] args) throws IOException {
		GetFileNameFromDir aDir = new GetFileNameFromDir();
		List<String> aFileList = aDir.getFileNameList(aBaseDirectory + "\\projects\\model1\\StockDatas\\2016-08-09-Former_Rehabilitation_leaned\\");
		
		for (String aName:aFileList) {
			System.out.println(aName);
			aDir.addColumn(aBaseDirectory + "\\projects\\model1\\StockDatas\\2016-08-09-Former_Rehabilitation_leaned\\", aName);
		}

	}

}
