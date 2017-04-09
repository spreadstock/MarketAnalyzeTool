package com.cooeration.marketengine.core.hotdeploy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.xbean.spring.context.ResourceXmlApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;

import com.cooeration.marketengine.core.jmx.JmxCenter;
import com.cooeration.marketengine.core.jmx.beans.Algorithm;
import com.cooeration.marketengine.framework.exception.EngineException;

public class DeployerImpl implements Deployer
{
    static final Logger logger = LoggerFactory.getLogger(DeployerImpl.class);
    public static final String contextName = "META-INF/algorithm-xbean.xml";
    public static final String beanName = "localTest";

    public static void main(String[] args)
    {
        DeployerImpl aaa = new DeployerImpl();
        aaa.deployAlgorithm("C:\\Users\\exubixu\\Desktop\\hotdeploy\\localTestFuncs-1.0.jar");
        
        
    }
    @Override
    public void deployAlgorithm(String fileName)
    {
        ClassLoader origClassLoader = Thread.currentThread().getContextClassLoader();
        try
        {
            File jarFile = new File(fileName);
            ApplicationContext xbeancontext = loadApplicationContext(jarFile);
            Algorithm algbean = (Algorithm)xbeancontext.getBean(beanName);
            
            System.out.println(algbean.getClassname());
            
            
            //注册到Jmx
//            JmxCenter.registerMBean(algbean);
            
            //把object保存到一个地方
            ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
            
            Class<?> algClass = currentLoader.loadClass(algbean.getClassname());
            System.out.println( algClass.newInstance().getClass());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            Thread.currentThread().setContextClassLoader(origClassLoader);
            e.printStackTrace();
        }
        
      
    }


    @Override
    public void redeployAlgorithm(String fileName)
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void undeployAlgorithm(String fileName)
    {
        // TODO Auto-generated method stub

    }


    protected ApplicationContext loadApplicationContext(File jarFile)
    {
        
        try
        {
            logger.debug("Reading application context [{}] from JAR file [{}]", contextName, jarFile.getAbsolutePath());

            URLClassLoader classLoader = new URLClassLoader(new URL[] {
                            jarFile.toURI().toURL()
            }, Thread.currentThread().getContextClassLoader());
            URL url = classLoader.findResource(contextName);
            if (url == null)
            {
                logger.error(jarFile.getName() + " not present in " + jarFile.getAbsolutePath());
            }
            
            Thread.currentThread().setContextClassLoader(classLoader);

            ResourceXmlApplicationContext appContext = (ResourceXmlApplicationContext) loadApplicationContextFile(jarFile);
            appContext.setClassLoader(classLoader);

            // this.contextRegistry.register(this.getApplicationName(jarFile), appContext);
            return appContext;
        }
        catch (Exception e)
        {
            throw new EngineException("Could not read application context '" + contextName + "' from JAR file '"
                            + jarFile.getAbsolutePath() + "'", "deploy", e);
        }
    }


    public ResourceXmlApplicationContext loadApplicationContextFile(File jdvFile)
    {
        ResourceXmlApplicationContext appContext = null;
        JarFile jarFile = null;
        InputStream inStream = null;
        try
        {
            jarFile = new JarFile(jdvFile);
            ZipEntry beanFile = jarFile.getEntry(contextName);
            if (null == beanFile)
            {
                logger.debug(contextName + " not present in JAR file " + jarFile.toString());
                return null;
            }
            inStream = jarFile.getInputStream(beanFile);

            appContext = new JarResourceXmlApplicationContext(new InputStreamResource(inStream));
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
            logger.error("", e1);
        }
        finally
        {
            try
            {
                if (inStream != null)
                {
                    inStream.close();
                }
                if (jarFile != null)
                {
                    jarFile.close();
                }
            }
            catch (IOException e)
            {
                logger.error("", e);
            }
        }
        return appContext;

    }

}
