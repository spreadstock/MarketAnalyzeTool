/**
 * 
 */
package com.cooeration.marketengine.core;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author exubixu
 */
public class log4jConfigureTest
{
    @Test
    public void testLoadConfigFile() throws IOException
    {
        SystemPropertyLoader loader = new SystemPropertyLoader();
        loader.load();

        try
        {
            Log4jConfigure log4jConfig = new Log4jConfigure();
            log4jConfig.init();
//            
            Logger logger = LoggerFactory.getLogger(log4jConfigureTest.class);
            logger.info("aaaxubi");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
