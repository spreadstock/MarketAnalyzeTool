package com.cooeration.marketengine.core.services;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooeration.marketengine.core.hotdeploy.Deployer;
import com.cooeration.marketengine.core.hotdeploy.DeployerImpl;
import com.cooeration.marketengine.core.hotdeploy.DirectoryWatcher;
import com.cooeration.marketengine.framework.services.CoreService;

public class HotDeployService implements CoreService
{
    private Logger logger = LoggerFactory.getLogger(HotDeployService.class);
    private String hotdir = System.getProperty("hotdaplop.dir");
    private DirectoryWatcher watcher = null;
    private Deployer deployer = new DeployerImpl();
    @Override
    public void start()
    {
        // TODO Auto-generated method stub
        try
        {
            watcher = new DirectoryWatcher(hotdir);
            watcher.setDeployer(deployer);
            watcher.execute();
        }
        catch (IOException e)
        {
            logger.error("", e);
        }
        
    }

    @Override
    public void stop()
    {
        try
        {
            watcher.shutdown();
        }
        catch (IOException e)
        {
            logger.error("", e);
        }
    }

}
