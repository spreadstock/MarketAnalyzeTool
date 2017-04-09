package com.xb.filewatcher;

import java.io.IOException;

public class TestFIle
{
    private static String DIR_PATH = "C:\\Users\\exubixu\\Desktop\\ERICvEMA_CXP10000-0.0.1-SNAPSHOT";

    public static void main(String[] args)
    {
        DirectoryWatcher watcher;
        try
        {
            watcher = new DirectoryWatcher(DIR_PATH );
            watcher.execute();


            // 延迟等待后台任务的执行
//            Thread.sleep(10000);
//            watcher.shutdown();
//            System.out.println("finished");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
