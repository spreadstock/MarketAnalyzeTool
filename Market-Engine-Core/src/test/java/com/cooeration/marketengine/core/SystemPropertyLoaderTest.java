/**
 * 
 */
package com.cooeration.marketengine.core;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

/**
 * @author exubixu
 */
public class SystemPropertyLoaderTest
{
    @Test
    public void readProperties()
    {
        SystemPropertyLoader loader = new SystemPropertyLoader();
        try
        {
            loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
      
        assertTrue(System.getProperty("log4j.configuration").contains("log4j.xml"));

    }
}
