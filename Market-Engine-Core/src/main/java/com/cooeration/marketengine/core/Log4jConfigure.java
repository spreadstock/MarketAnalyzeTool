package com.cooeration.marketengine.core;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooeration.marketengine.framework.exception.EngineException;

public class Log4jConfigure
{
    private File logproperty;
    private long lastUpdateTime;
    private Timer timeLog = new Timer("log4jConfigure");
    private TimerTask task = new CheckForUpdate();


    public void init()
    {
        getConfLog4jFile();
        checkForUpdate();
        // start timer
         timeLog.schedule(task, 30000L);
    }


    public void checkForUpdate()
    {

        // init
        if (lastUpdateTime == 0 && logproperty.isFile() || logproperty.lastModified() > lastUpdateTime)
        {
            lastUpdateTime = logproperty.lastModified();
            try
            {
                new DOMConfigurator().doConfigure(logproperty.getAbsolutePath(), LogManager.getLoggerRepository());
                Logger logger = LoggerFactory.getLogger(Log4jConfigure.class);
                logger.info("update log4jconfig using path:" + logproperty.getAbsolutePath());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (!logproperty.isFile())
        {
            throw new EngineException("Failed to initiate log4j configuration", "NOT STARTED");
        }

    }


    protected void getConfLog4jFile()
    {
        String logconf = System.getProperty("log4j.configuration");
        logproperty = new File(logconf);
    }

    private class CheckForUpdate extends TimerTask
    {
        @Override
        public void run()
        {
            checkForUpdate();
        }

    }

}
