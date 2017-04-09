package com.cooeration.marketengine.core.services;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooeration.marketengine.framework.services.CoreService;

public class JmxServerServie implements CoreService
{
    private MBeanServer mbs = null;

    private int port = 9099;

    private int jmxport = 9994;

    private String host = "127.0.0.1";

    private String jxmURI = "/server";

    // "service:jmx:rmi:///jndi/rmi://localhost:9999/server"
    private String urlStr = "service:jmx:rmi://" + host + ":" + jmxport + "/jndi/rmi://" + host + ":" + port + jxmURI;
    private JMXConnectorServer cs = null;
    Logger logger = LoggerFactory.getLogger(JmxServerServie.class);

    public JmxServerServie(MBeanServer mbs)
    {
        this.mbs = mbs;
    }


    public void start()
    {
        try
        {
            logger.info("start JmxServerServie");
            Registry registry = LocateRegistry.createRegistry(9099);
            JMXServiceURL url = new JMXServiceURL(urlStr);
            cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
            cs.start();
            logger.info("started JmxServerServie");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    public void stop()
    {
        try
        {
            cs.stop();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
