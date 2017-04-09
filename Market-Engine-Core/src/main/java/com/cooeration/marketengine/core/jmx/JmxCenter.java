/**
 * 
 */
package com.cooeration.marketengine.core.jmx;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;

import com.cooeration.marketengine.core.jmx.annotation.JmxAnnotationUtil;
import com.cooeration.marketengine.core.jmx.beans.Car;

/**
 * @author exubixu
 */
public class JmxCenter
{
    private static MBeanServer mBeanServer = null;
    private static final String domain = "com.cooperation";


    public MBeanServer getmBeanServer()
    {
        return mBeanServer;
    }


    public JmxCenter()
    {
        mBeanServer = createMBeanServer(domain);
    }
    


    public static void registerMBean(Object obj)
    {
        String beanObjectName = ":type=MyMarketEngine";
        String beanName = "MyMarketEngine";
        try
        {
            ObjectName objectName = new ObjectName(domain + beanObjectName);
            ModelMBean modelMBean = createMBean(objectName, beanName, obj.getClass());
            // associate the Model MBean with our managed object
            modelMBean.setManagedResource(obj, "ObjectReference");
            mBeanServer.registerMBean(modelMBean, objectName);
        }
        catch (MalformedObjectNameException | RuntimeOperationsException | InstanceNotFoundException | MBeanException
                        | InvalidTargetObjectTypeException | InstanceAlreadyExistsException
                        | NotCompliantMBeanException e)
        {
            e.printStackTrace();
        }

    }


    public void unRegisterMBean(String beanObjectName)
    {
        try
        {
            ObjectName objectName = new ObjectName(domain + beanObjectName);
            mBeanServer.unregisterMBean(objectName);
        }
        catch (MalformedObjectNameException | RuntimeOperationsException | InstanceNotFoundException | MBeanException e)
        {
            e.printStackTrace();
        }
    }


    private static ModelMBean createMBean(ObjectName objectName, String mbeanName, Class<?> beanClass)
    {
        ModelMBeanInfo mBeanInfo = createModelMBeanInfo(objectName, mbeanName, beanClass);
        RequiredModelMBean modelMBean = null;
        try
        {
            modelMBean = new RequiredModelMBean(mBeanInfo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return modelMBean;
    }


    private static ModelMBeanInfo createModelMBeanInfo(ObjectName objectName, String mbeanName, Class<?> beanClass)
    {
        ModelMBeanNotificationInfo[] dNotifications = null;
        List<Field> attributes = JmxAnnotationUtil.getAttributes(beanClass);
        List<Method> methods = JmxAnnotationUtil.getMethods(beanClass);
        List<Constructor<?>> constructors = JmxAnnotationUtil.getConstructors(beanClass);

        ModelMBeanAttributeInfo[] dAttributes = new ModelMBeanAttributeInfo[attributes.size()];
        ModelMBeanConstructorInfo[] dConstructors = new ModelMBeanConstructorInfo[constructors.size()];
        ModelMBeanOperationInfo[] dOperations = new ModelMBeanOperationInfo[methods.size()];

        for (int i = 0; i < attributes.size(); i++)
        {
            Field a = attributes.get(i);

            dAttributes[i] = new ModelMBeanAttributeInfo(a.getName(), a.getType().getName(),
                            a.getName() + a.getType().getName(), true, true, false, null);
        }

        for (int i = 0; i < constructors.size(); i++)
        {
            Constructor<?> a = constructors.get(i);

            dConstructors[i] = new ModelMBeanConstructorInfo(a.getName(), a, null);
        }

        for (int i = 0; i < methods.size(); i++)
        {
            Method a = methods.get(i);
            Class<?> para[] = a.getParameterTypes();
            MBeanParameterInfo[] dParams = new MBeanParameterInfo[para.length];
            for (int k = 0; k < para.length; k++)
            {

                dParams[k] = new MBeanParameterInfo(para[k].getName(), para[k].getTypeName(), "");
            }

            dOperations[i] = new ModelMBeanOperationInfo(a.getName(), a.getName(), dParams, a.getReturnType().getName(),
                            MBeanOperationInfo.ACTION, null);

        }
        ModelMBeanInfo mBeanInfo = new ModelMBeanInfoSupport(beanClass.getName(), null, dAttributes, dConstructors,
                        dOperations, null);

        return mBeanInfo;
    }


    public static void main(String[] args)
    {
        JmxCenter center = new JmxCenter();
        Car test = new Car("pink");
        center.registerMBean(test);
        String beanObjectName = ":type=MyMarketEngine";
        String domainName = center.getmBeanServer().getDefaultDomain();
        System.out.println("default:" + domainName);
        Arrays.asList(center.getmBeanServer().getDomains()).forEach(System.out::println);

        ObjectName objectName;
        try
        {
            objectName = new ObjectName(domainName + beanObjectName);
            center.getmBeanServer().invoke(objectName, "drive", null, null);
            center.getmBeanServer().invoke(objectName, "setColor", new Object[] { "red" }, new String[]{ String.class.getName() });
            center.getmBeanServer().invoke(objectName, "getColor", null, null);
            String beanName = "MyMarketEngine";
            center.printMBeanInfo(objectName,beanName);
        }
        catch (MalformedObjectNameException | InstanceNotFoundException | ReflectionException | MBeanException e)
        {
            e.printStackTrace();
        }

    }


    public MBeanServer createMBeanServer(String domain)
    {
        MBeanServer mbs = MBeanServerFactory.createMBeanServer(domain);
        return mbs;
    }


    public MBeanServer getMBeanServer(String domain)
    {
        List<MBeanServer> mbs = MBeanServerFactory.findMBeanServer(null);
        Optional<MBeanServer> targetMb = mbs.stream().filter((mb) -> {
            Optional<String> result = Arrays.asList(mb.getDomains()).stream().filter((a) -> {
                return a.equals(domain);
            }).findFirst();
            return result.isPresent();
        }).findFirst();

        return targetMb.get();
    }


    private void printMBeanInfo(ObjectName mbeanObjectName, String mbeanName)
    {
        echo(">>> Getting the management information for the " + mbeanName + ":" + mbeanObjectName + " MBean");
        echo("    using the getMBeanInfo method of the MBeanServer");
        ModelMBeanInfo info = null;
        try
        {
            info = (ModelMBeanInfo) (mBeanServer.getMBeanInfo(mbeanObjectName));
            if (info == null)
            {
                echo("ModelMBeanInfo from JMX Agent is null!");
            }
        }
        catch (Exception e)
        {
            echo("\t!!! ModelAgent:printMBeanInfo: Could not get MBeanInfo object for " + mbeanName + " exception type "
                            + e.getClass().toString() + ":" + e.getMessage() + "!!!");
            e.printStackTrace();
            return;
        }
        echo("CLASSNAME: \t" + info.getClassName());
        echo("DESCRIPTION: \t" + info.getDescription());
        try
        {
            echo("MBEANDESCRIPTOR: \t" + (info.getMBeanDescriptor()).toString());
        }
        catch (Exception e)
        {
            echo("MBEANDESCRIPTOR: \tNone");
        }
        echo("ATTRIBUTES");
        MBeanAttributeInfo[] attrInfo = (info.getAttributes());
        if (attrInfo.length > 0)
        {
            for (int i = 0; i < attrInfo.length; i++)
            {
                echo(" ** NAME: \t" + attrInfo[i].getName());
                echo("    DESCR: \t" + attrInfo[i].getDescription());
                echo("    TYPE: \t" + attrInfo[i].getType() + "\tREAD: " + attrInfo[i].isReadable() + "\tWRITE: "
                                + attrInfo[i].isWritable());
                echo("    DESCRIPTOR: \t" + (((ModelMBeanAttributeInfo) attrInfo[i]).getDescriptor()).toString());
            }
        }
        else
            echo(" ** No attributes **");
        MBeanConstructorInfo[] constrInfo = info.getConstructors();
        echo("CONSTRUCTORS");
        if (constrInfo.length > 0)
        {
            for (int i = 0; i < constrInfo.length; i++)
            {
                echo(" ** NAME: \t" + constrInfo[i].getName());
                echo("    DESCR: \t" + constrInfo[i].getDescription());
                echo("    PARAM: \t" + constrInfo[i].getSignature().length + " parameter(s)");
                echo("    DESCRIPTOR: \t" + (((ModelMBeanConstructorInfo) constrInfo[i]).getDescriptor()).toString());
            }
        }
        else
            echo(" ** No Constructors **");
        echo("OPERATIONS");
        MBeanOperationInfo[] opInfo = info.getOperations();
        if (opInfo.length > 0)
        {
            for (int i = 0; i < opInfo.length; i++)
            {
                echo(" ** NAME: \t" + opInfo[i].getName());
                echo("    DESCR: \t" + opInfo[i].getDescription());
                echo("    PARAM: \t" + opInfo[i].getSignature().length + " parameter(s)");
                echo("    DESCRIPTOR: \t" + (((ModelMBeanOperationInfo) opInfo[i]).getDescriptor()).toString());
            }
        }
        else
            echo(" ** No operations ** ");
        echo("NOTIFICATIONS");
        MBeanNotificationInfo[] notifInfo = info.getNotifications();
        if (notifInfo.length > 0)
        {
            for (int i = 0; i < notifInfo.length; i++)
            {
                echo(" ** NAME: \t" + notifInfo[i].getName());
                echo("    DESCR: \t" + notifInfo[i].getDescription());
                echo("    DESCRIPTOR: \t" + (((ModelMBeanNotificationInfo) notifInfo[i]).getDescriptor()).toString());
            }
        }
        else
            echo(" ** No notifications **");
        echo("End of MBeanInfo print");
    }
    
    private static void echo(String msg)
    {
        System.out.println(msg);
    }
}
