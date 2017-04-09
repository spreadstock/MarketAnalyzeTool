/**
 * 
 */
package com.cooeration.marketengine.framework.engine;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.cooeration.marketengine.framework.engine.EngineStatus.Status;
import com.cooeration.marketengine.framework.exception.EngineException;

/**
 * @author exubixu Engine is used to restirc the activity of Engine implemention and add some hooks
 */
public abstract class Engine
{
    private EngineStatus status = new EngineStatus();

    private ExecutorService monitorExcutor = Executors.newFixedThreadPool(1);
    private String fileDir = null;
    private ServiceManagerCenter serviceregister = new ServiceManagerCenter();
    private final String pidDir = "pidfile";

    public EngineStatus getEngineStatus()
    {
        return status;
    }


    public void init()
    {
        // init context
        status.updateStatus(Status.INACTIVE);
    }


   public abstract void registerService(ServiceManagerCenter serviceregister);


    public void start()
    {
        init();
        beforeStart();
        // start service
        registerService(serviceregister);
        serviceregister.startServices();

        afterStart();
        signelStarted();
    }


    public void shutdown()
    {
        beforeShutdown();
        // stop service

        afterShutdown();
        signelShutdonw();
    }


    public void signelShutdonw()
    {
        // stop monitor thread
        monitorExcutor.shutdownNow();
        // remove a signal from file
        removeSignalFile();
    }


    public void signelStarted()
    {

        // start monitor thread
        monitorExcutor.execute(new MonitorTask());
        // write a signal to file
        createSignalFile();
        status.updateStatus(Status.ACTIVE);
    }


    protected void createSignalFile()
    {
        Optional<String> dir = Optional.of(System.getProperty("application.dir"));
        fileDir = dir.orElse("xubin");
        String name = getPid();
        File directory = new File(fileDir + pidDir);
        directory.deleteOnExit();
        
        
        
        File signal = new File(fileDir + pidDir + File.separator + name);
        try
        {
            System.out.println(signal.getAbsolutePath());
            directory.mkdir();
            System.out.println(directory.getAbsolutePath());
            signal.createNewFile();
        }
        catch (IOException e)
        {
            throw new EngineException("createNewFile failed" + "MarketCoreApplicaton will STOP", "STARTING", e);
        }
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }


    protected void removeSignalFile()
    {
        Optional<String> dir = Optional.of(System.getProperty("application.dir"));
        fileDir = dir.orElse("xubin");
        String name = getPid();
        File directory = new File(fileDir + pidDir);
        directory.deleteOnExit();
        
        File signal = new File(fileDir + pidDir + File.separator + name);
        signal.delete();

    }


    private String getPid()
    {
        return ManagementFactory.getRuntimeMXBean().getName();
    }


    public void beforeStart()
    {
        status.updateStatus(Status.STARTIING);
    }


    public void afterStart()
    {

    }


    public void beforeShutdown()
    {
        status.updateStatus(Status.STOPPING);
    }


    public void afterShutdown()
    {

    }

    private class MonitorTask implements Runnable
    {

        @Override
        public void run()
        {
            while (status.mystatus != Status.INACTIVE)
            {
                try
                {
                    System.out.println("Engine is running debug");
                    TimeUnit.SECONDS.sleep(10);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }

    private class ShutdownHook extends Thread
    {
        @Override
        public void run() {
            System.out.println("in add on hook");
            removeSignalFile();
        }
    }
}
