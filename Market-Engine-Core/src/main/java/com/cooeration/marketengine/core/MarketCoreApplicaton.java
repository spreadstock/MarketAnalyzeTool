/**
 * 
 */
package com.cooeration.marketengine.core;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.spark.sql.SparkSession;

import com.cooeration.marketengine.core.jmx.JmxCenter;
import com.cooeration.marketengine.core.services.HotDeployService;
import com.cooeration.marketengine.core.services.JmxServerServie;
import com.cooeration.marketengine.framework.engine.Engine;
import com.cooeration.marketengine.framework.engine.ServiceManagerCenter;

/**
 * @author exubixu
 */
public class MarketCoreApplicaton extends Engine
{
    SparkSession spark = null;
    Log4jConfigure log4jConfig = new Log4jConfigure();
    private String status;
    private final Logger logger = Logger.getLogger(MarketCoreApplicaton.class);
    private JmxCenter center =null;
    private JmxServerServie jmxs = null;
    private HotDeployService deploys = null;
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // check parameters
//        if (args.length < 1)
//        {
//            throw new EngineException("arguments not correct, please run like" + "MarketCoreApplicaton START/STOP",
//                            "NOT STARTED");
//        }
//        try
//        {
//            if (args[0].endsWith("START"))
//            {
//                new MarketCoreApplicaton().start();
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

        
        new MarketCoreApplicaton().start();
    }


    @Override
    public void init()
    {
        super.init();
        SystemPropertyLoader loader = new SystemPropertyLoader();
        try
        {
            loader.load();
            center = new JmxCenter();
            jmxs = new JmxServerServie(center.getmBeanServer());
            deploys = new HotDeployService();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log4jConfig.init();

    }


    @Override
    public void registerService(ServiceManagerCenter serviceregister)
    {
        // TODO Auto-generated method stub
        serviceregister.registerService(jmxs);
        serviceregister.registerService(deploys);
    }




}
