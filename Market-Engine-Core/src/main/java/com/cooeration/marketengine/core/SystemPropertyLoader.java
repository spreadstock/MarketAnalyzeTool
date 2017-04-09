/**
 * 
 */
package com.cooeration.marketengine.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author exubixu
 *
 */
public class SystemPropertyLoader
{

    public void load() throws IOException
    {
        ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
        Properties pro = new Properties();
        pro.load(new FileInputStream(new File(currentLoader.getResource("application.properties").getFile())));
        pro.forEach((key, value) -> {
            System.setProperty((String)key, (String)value);
        });
    }

}
