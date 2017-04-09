/**
 * 
 */
package com.xb.jmx.example3;

/**
 * @author exubixu
 */
import java.lang.reflect.Constructor;
import javax.management.*;
import javax.management.modelmbean.*;

public class ModelAgent
{
    /*
     * 私有属性
     */
    private String dClassName = "TestBean";
    private String dDescription = "Simple implementation of a test app Bean.";
    private ModelMBeanAttributeInfo[] dAttributes = new ModelMBeanAttributeInfo[3];
    private ModelMBeanConstructorInfo[] dConstructors = new ModelMBeanConstructorInfo[1];
    private ModelMBeanOperationInfo[] dOperations = new ModelMBeanOperationInfo[6];
    private ModelMBeanNotificationInfo[] dNotifications = new ModelMBeanNotificationInfo[1];
    private Descriptor mmbDesc = null;

    // 这个Model MBean将会把信息放在ModelMBeanInfo对象中
    private ModelMBeanInfo dMBeanInfo = null;
    NotificationListener attrListener = null;


    public ModelAgent()
    {
        // 创建MBeanServer
        server = MBeanServerFactory.createMBeanServer();
        try
        {
            ObjectInstance html = server.createMBean("com.sun.jdmk.comm.HtmlAdaptorServer", null);
            server.invoke(html.getObjectName(), "start", new Object[0], new String[0]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void main(String[] args)
    {
        // 创建Agent...
        ModelAgent agent = new ModelAgent();
        // 执行演示方法
        agent.doSimpleDemo();
    }


    // 私有方法
    private void doSimpleDemo()
    {
        // build the simple MBean ObjectName
        //
        ObjectName mbeanObjectName = null;
        String domain = server.getDefaultDomain();
        String mbeanName = "ModelSample";
        attrListener = (NotificationListener) new TestBeanAttributeChangeListener();
        try
        {
            mbeanObjectName = new ObjectName(domain + ":type=" + mbeanName);
        }
        catch (MalformedObjectNameException e)
        {
            echo("\t!!! Could not create the MBean ObjectName !!!");
            e.printStackTrace();
            echo("EXITING...");
            System.exit(1);
        }
        // create and register the MBean
        createMBean(mbeanObjectName, mbeanName);
        // get and display the management information exposed by the MBean
        printMBeanInfo(mbeanObjectName, mbeanName);
        // create an event listener
        createEventListeners(mbeanObjectName, attrListener);
        // manage the MBean
        manageSimpleBean(mbeanObjectName, mbeanName);

        // trying to do illegal management actions...
        goTooFar(mbeanObjectName);

        System.out.println("\nNow, you can point your browser to http://localhost:8082/");
        System.out.println("or start your client application to connect to this agent.\n");
    }


    private void createMBean(ObjectName mbeanObjectName, String mbeanName)
    {
        echo("CREATE object name = " + mbeanObjectName);
        // set management interface in ModelMBean - attributes, operations, notifications
        buildDynamicMBeanInfo(mbeanObjectName, mbeanName);
        try
        {
            RequiredModelMBean modelmbean = new RequiredModelMBean(dMBeanInfo);
            // Set the managed resource for ModelMBean instance
            modelmbean.setManagedResource(new TestBean(), "objectReference");
            // register the ModelMBean in the MBean Server
            server.registerMBean(modelmbean, mbeanObjectName);
        }
        catch (Exception e)
        {
            echo("\t!!! ModelAgent: Could not create the " + mbeanName + " MBean !!!");
            e.printStackTrace();
            echo("EXITING...");
            System.exit(1);
        }
        echo("\tModelMBean has been successfully created.");
    }


    private void createEventListeners(ObjectName mbeanObjectName, NotificationListener aListener)
    {
        try
        {
            server.invoke(mbeanObjectName, "addAttributeChangeNotificationListener", (new Object[] {
                            aListener, "State", null
            }), (new String[] {
                            "javax.management.NotificationListener", "java.lang.String", "java.lang.Object"
            }));
            echo("\tEvent listener created successfully");
        }
        catch (Exception e)
        {
            echo("Error! Creating Event listener with invoke failed with message:");
            echo(e.getMessage() + "");
            echo("EXITING...");
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * 通过getAttribute(), getAttributes(), setAttribute(), and setAttributes() 方法来访问属性 通过invoke()方法来调用reset()操作
     */
    private void manageSimpleBean(ObjectName mbeanObjectName, String mbeanName)
    {
        echo("MANAGING the " + mbeanName + " MBean ");
        echo("using its attributes and operations exposed for management");
        try
        {
            // Get attribute values
            sleep(1000);
            echo("Printing attributes from ModelMBean ");
            printSimpleAttributes(mbeanObjectName);
            sleep(1000);
            echo("Printing attributes from instance cache ");
            printSimpleAttributes(mbeanObjectName);
            // Change State attribute
            sleep(1000);
            echo(" Setting State attribute to value \"new state\"...");
            Attribute stateAttribute = new Attribute("State", "new state");
            server.setAttribute(mbeanObjectName, stateAttribute);

            // Get attribute values
            sleep(1000);
            printSimpleAttributes(mbeanObjectName);
            echo("The NbChanges attribute is still \"0\" as its cached value is valid for 5 seconds (currencyTimeLimit=5s)");
            echo("Wait for 5 seconds and print new attributes values ...");
            sleep(5000);
            printSimpleAttributes(mbeanObjectName);
            // Invoking reset operation
            sleep(1000);
            echo(" Invoking reset operation...");
            server.invoke(mbeanObjectName, "reset", null, null);
            // Get attribute values 1
            echo(" Printing reset attribute values");
            sleep(1000);
            printSimpleAttributes(mbeanObjectName);
            echo("The State and NbChanges attributes are still \"1\" and \"new state\" as their cached value is valid for 5 seconds (currencyTimeLimit=5s)");
            echo("Wait for 5 seconds and print new attributes values ...");
            sleep(5000);
            printSimpleAttributes(mbeanObjectName);
            // Getting Notifications list
            echo("Printing Notifications Broadcasted");
            sleep(1000);
            MBeanNotificationInfo[] myNotifys = (MBeanNotificationInfo[]) server
                            .invoke(mbeanObjectName, "getNotificationInfo", null, null);
            echo("\tSupported notifications are:");
            for (int i = 0; i < myNotifys.length; i++)
            {
                echo("\t\t" + ((ModelMBeanNotificationInfo) myNotifys[i]).toString());
            }
            // Accesssing and printing Procol Map for NbChanges
            echo(" Exercising Protocol map for NbChanges");
            sleep(1000);
            ModelMBeanInfo myMMBI = (ModelMBeanInfo) server.invoke(mbeanObjectName, "getMBeanInfo", null, null);
            Descriptor myDesc = myMMBI.getDescriptor("NbChanges", "attribute");
            echo("\tRetrieving specific protocols:");
            // echo("Descriptor: " + myDesc.toString());
            Descriptor pm = (Descriptor) myDesc.getFieldValue("protocolMap");
            // echo("ProtocolMap *"+pm.toString()+"*");
            echo("\tProtocolMap lookup SNMP is " + pm.getFieldValue("SNMP"));
            echo("\tProtocolMap lookup CIM is " + pm.getFieldValue("CIM"));
            echo("\tDynamically updating Protocol Map:");
            pm.setField("CIM", "ManagedResource.LongVersion");
            pm.setField("CMIP", "SwitchData");
            echo("\tPrinting Protocol Map");
            String[] pmKeys = pm.getFieldNames();
            Object[] pmEntries = pm.getFieldValues(null);
            for (int i = 0; i < pmKeys.length; i++)
            {
                echo("\tProtocol Map Name " + i + ": Name: " + pmKeys[i] + ": Entry:"
                                + ((String) pmEntries[i]).toString());
            }
            echo(" Testing operation caching");
            echo(" Invoking getNbResets");
            Integer numResets = (Integer) server.invoke(mbeanObjectName, "getNbResets", null, null);
            echo("\tReceived " + numResets + " from getNbResets first time");
            echo(" Invoking second reset operation...");
            server.invoke(mbeanObjectName, "reset", null, null);
            Integer numResets2 = (Integer) server.invoke(mbeanObjectName, "getNbResets", null, null);
            echo("\tReceived " + numResets2 + " from getNbResets second time (from operation cache)");
            echo(" Invoking get of attribute ONLY provided through ModelMBeanAttributeInfo (should be 99)...");
            Integer respHardValue = (Integer) server.getAttribute(mbeanObjectName, "HardValue");
            echo("\tReceived " + respHardValue + " from getAttribute of hardValue");
        }
        catch (Exception e)
        {
            echo("ManageSimpleBean failed with " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }


    private void goTooFar(ObjectName mbeanObjectName)
    {
        echo(">>> Trying to set the NbChanges attribute (read-only)!");
        echo("... We should get an AttributeNotFoundException:");
        sleep(1000);
        // Try to set the NbChanges attribute
        Attribute nbChangesAttribute = new Attribute("NbChanges", new Integer(1));
        try
        {
            server.setAttribute(mbeanObjectName, nbChangesAttribute);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        echo(">>> Trying to access the NbResets property (not exposed for management)!");
        echo("... We should get an AttributeNotFoundException:");
        sleep(1000);
        // Try to access the NbResets property
        try
        {
            server.getAttribute(mbeanObjectName, "NbResets");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return;
    }


    private void printMBeanInfo(ObjectName mbeanObjectName, String mbeanName)
    {
        echo(">>> Getting the management information for the " + mbeanName + ":" + mbeanObjectName + " MBean");
        echo("    using the getMBeanInfo method of the MBeanServer");
        sleep(1000);
        ModelMBeanInfo info = null;
        try
        {
            info = (ModelMBeanInfo) (server.getMBeanInfo(mbeanObjectName));
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


    private void printSimpleAttributes(ObjectName mbeanObjectName)
    {
        try
        {
            echo("\tGetting attribute values:");
            String State = (String) server.getAttribute(mbeanObjectName, "State");
            Integer NbChanges = (Integer) server.getAttribute(mbeanObjectName, "NbChanges");
            echo("\t\tState     = \"" + State + "\"");
            echo("\t\tNbChanges = \"" + NbChanges.toString() + "\"");
        }
        catch (Exception e)
        {
            echo("\tModelAgent:printSimpleAttributes: !!! Could not read attributes !!!");
            e.printStackTrace();
            return;
        }
    }


    private static void echo(String msg)
    {
        System.out.println(msg);
    }


    private static void sleep(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            return;
        }
    }

    /*
     * ------------------------------------------ PRIVATE VARIABLES ------------------------------------------
     */
    private MBeanServer server = null;


    /*
     * ----------------------------------------------------- PRIVATE METHODS
     * -----------------------------------------------------
     */
    private void loadDynamicMBeanInfo(ObjectName inMbeanObjectName, String inMbeanName)
    {
        try
        {
            Class appBean = Class.forName(dClassName);
            // echo("Setting mbeanDescriptor " + mmbDesc);
            dMBeanInfo = new ModelMBeanInfoSupport(dClassName, dDescription, dAttributes, dConstructors, dOperations,
                            dNotifications, mmbDesc);
        }
        catch (Exception e)
        {
            echo("Exception in loadDynamicMBeanInfo : " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 创建私有的dMBeanInfo的各个字段 展示可以用来被管理的属性,构造器,操作以及通知等数据 A reference to the dMBeanInfo object is returned by the
     * getMBeanInfo() method of the DynamicMBean interface. Note that, once constructed, an MBeanInfo object is i
     */
    private void buildDynamicMBeanInfo(ObjectName inMbeanObjectName, String inMbeanName)
    {
        try
        {
            Class appBean = Class.forName(dClassName);
            // 为Model MBean设置描述符字段
            mmbDesc = new DescriptorSupport(new String[] {
                            ("name=" + inMbeanObjectName), "descriptorType=mbean", ("displayName=" + inMbeanName),
                            "log=T", "logfile=jmxmain.log", "currencyTimeLimit=5"
            });

            // 为State属性设置描述符对象
            Descriptor stateDesc = new DescriptorSupport();
            stateDesc.setField("name", "State");
            stateDesc.setField("descriptorType", "attribute");
            stateDesc.setField("displayName", "MyState");
            stateDesc.setField("getMethod", "getState");
            stateDesc.setField("setMethod", "setState");
            stateDesc.setField("currencyTimeLimit", "20");
            dAttributes[0] = new ModelMBeanAttributeInfo("State", "java.lang.String", "State: state string.", true,
                            true, false, stateDesc);

            // 为NbChanges属性设置描述符
            Descriptor nbChangesDesc = new DescriptorSupport();
            nbChangesDesc.setField("name", "NbChanges");
            nbChangesDesc.setField("descriptorType", "attribute");
            nbChangesDesc.setField("default", "0");
            nbChangesDesc.setField("displayName", "MyChangesCount");
            nbChangesDesc.setField("getMethod", "getNbChanges");
            nbChangesDesc.setField("setMethod", "setNbChanges");
            Descriptor nbChangesMap = new DescriptorSupport(new String[] {
                            "SNMP=1.3.6.9.12.15.18.21.0", "CIM=ManagedResource.Version"
            });

            nbChangesDesc.setField("protocolMap", (nbChangesMap));

            dAttributes[1] = new ModelMBeanAttributeInfo("NbChanges", "java.lang.Integer",
                            "NbChanges: number of times the State string", true, false, false, nbChangesDesc);

            // 为HardValue属性设置描述符
            Descriptor hardValueDesc = new DescriptorSupport();
            hardValueDesc.setField("name", "HardValue");
            hardValueDesc.setField("descriptorType", "attribute");
            hardValueDesc.setField("value", new Integer("99"));
            hardValueDesc.setField("displayName", "HardCodedValue");
            hardValueDesc.setField("currencyTimeLimit", "0");
            /*
             * currencyTimeLimit = 0 表示缓存在描述符对象中的值是永远有效的 当我们调用getAttribute方法访问这个属性的时候，会从描述符对象中读到99
             */
            dAttributes[2] = new ModelMBeanAttributeInfo("HardValue", "java.lang.Integer",
                            "HardValue: static value in ModelMBeanInfo and not in TestBean", true, false, false,
                            hardValueDesc);

            Constructor[] constructors = appBean.getConstructors();

            // 定义构造器描述符信息
            Descriptor testBeanDesc = new DescriptorSupport();
            testBeanDesc.setField("name", "TestBean");
            testBeanDesc.setField("descriptorType", "operation");
            testBeanDesc.setField("role", "constructor");

            dConstructors[0] = new ModelMBeanConstructorInfo("TestBean(): Constructs a TestBean App", constructors[0],
                            testBeanDesc);

            MBeanParameterInfo[] params = null;

            // 定义reset操作描述符信息
            Descriptor resetDesc = new DescriptorSupport();
            resetDesc.setField("name", "reset");
            resetDesc.setField("descriptorType", "operation");
            resetDesc.setField("class", "TestBean");
            resetDesc.setField("role", "operation");

            dOperations[0] = new ModelMBeanOperationInfo("reset", "reset(): reset State and NbChanges", params, "void",
                            MBeanOperationInfo.ACTION, resetDesc);

            // 定义getNbResets操作描述符信息
            Descriptor getNbResetsDesc = new DescriptorSupport(new String[] {
                            "name=getNbResets", "class=TestBeanFriend", "descriptorType=operation", "role=operation"
            });
            TestBeanFriend tbf = new TestBeanFriend();
            getNbResetsDesc.setField("targetObject", tbf);
            getNbResetsDesc.setField("targetType", "objectReference");

            dOperations[1] = new ModelMBeanOperationInfo("getNbResets", "getNbResets(): get number of resets done",
                            params, "java.lang.Integer", MBeanOperationInfo.INFO, getNbResetsDesc);
            // 定义getState操作描述符信息
            Descriptor getStateDesc = new DescriptorSupport(new String[] {
                            "name=getState", "descriptorType=operation", "class=TestBean", "role=operation"
            });

            dOperations[2] = new ModelMBeanOperationInfo("getState", "get state attribute", params, "java.lang.String",
                            MBeanOperationInfo.ACTION, getStateDesc);
            // 定义setState操作描述符信息
            Descriptor setStateDesc = new DescriptorSupport(new String[] {
                            "name=setState", "descriptorType=operation", "class=TestBean", "role=operation"
            });

            MBeanParameterInfo[] setStateParms = new MBeanParameterInfo[] {
                            (new MBeanParameterInfo("newState", "java.lang.String", "new State value"))
            };

            dOperations[3] = new ModelMBeanOperationInfo("setState", "set State attribute", setStateParms, "void",
                            MBeanOperationInfo.ACTION, setStateDesc);

            // 定义getNbChanges操作描述符信息
            Descriptor getNbChangesDesc = new DescriptorSupport(new String[] {
                            "name=getNbChanges", "descriptorType=operation", "class=TestBean", "role=operation"
            });

            dOperations[4] = new ModelMBeanOperationInfo("getNbChanges", "get NbChanges attribute", params,
                            "java.lang.Integer", MBeanOperationInfo.INFO, getNbChangesDesc);

            // 定义参数描述符信息
            Descriptor setNbChangesDesc = new DescriptorSupport(new String[] {
                            "name=setNbChanges", "descriptorType=operation", "class=TestBean", "role=operation"
            });

            MBeanParameterInfo[] setNbChangesParms = new MBeanParameterInfo[] {
                            (new MBeanParameterInfo("newNbChanges", "java.lang.Integer",
                                            "new value for Number of Changes"))
            };

            dOperations[5] = new ModelMBeanOperationInfo("setNbChanges", "set NbChanges attribute", setNbChangesParms,
                            "void", MBeanOperationInfo.ACTION, setNbChangesDesc);

            // dNotifications[0] = new MBeanNotificationInfo((new String[] {"jmx.attribute.change"}),
            // "AttributeChange","ModelMBean Attribute Change Event");
            // 定义通知描述符信息
            Descriptor genEventDesc = new DescriptorSupport(new String[] {
                            "descriptorType=notification", "name=jmx.ModelMBean.General", "severity=4",
                            "MessageId=MA001", "log=T", "logfile=jmx.log"
            });
            String[] genTypes = new String[1];
            genTypes[0] = "jmx.ModelMBean.General";
            dNotifications[0] = new ModelMBeanNotificationInfo(genTypes, "jmx.ModelMBean.General", // was Generic
                            "Generic Event", genEventDesc); // test event

            // 定义了ModelBean元数据信息
            dMBeanInfo = new ModelMBeanInfoSupport(dClassName, dDescription, dAttributes, dConstructors, dOperations,
                            dNotifications);

            dMBeanInfo.setMBeanDescriptor(mmbDesc);

        }
        catch (Exception e)
        {
            echo("Exception in buildDynamicMBeanInfo : " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void printLocalMBeanInfo(MBeanInfo info)
    {
        echo("CLASSNAME: \t" + info.getClassName());
        echo("DESCRIPTION: \t" + info.getDescription());
        echo("ATTRIBUTES");
        MBeanAttributeInfo[] attrInfo = info.getAttributes();
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
        echo("CONSTRUCTORS");
        MBeanConstructorInfo[] constrInfo = info.getConstructors();
        for (int i = 0; i < constrInfo.length; i++)
        {
            echo(" ** NAME: \t" + constrInfo[i].getName());
            echo("    DESCR: \t" + constrInfo[i].getDescription());
            echo("    PARAM: \t" + constrInfo[i].getSignature().length + " parameter(s)");
            echo("    DESCRIPTOR: \t" + (((ModelMBeanConstructorInfo) constrInfo[i]).getDescriptor()).toString());
        }
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
    }


    public void printLocalDescriptors(MBeanInfo mbi) throws javax.management.MBeanException
    {
        echo(mbi.getDescription() + "Descriptors:");
        echo("Attribute Descriptors:");
        Descriptor[] dArray;
        dArray = ((ModelMBeanInfo) mbi).getDescriptors("attribute");
        for (int i = 0; i < dArray.length; i++)
        {
            String[] afields = ((Descriptor) dArray[i]).getFields();
            for (int j = 0; j < afields.length; j++)
            {
                echo(afields[j] + "");
            }
        }
        echo("Operation Descriptors:");
        dArray = ((ModelMBeanInfo) mbi).getDescriptors("operation");
        for (int i = 0; i < dArray.length; i++)
        {
            echo("*Operation****************************");
            String[] ofields = ((Descriptor) dArray[i]).getFields();
            for (int j = 0; j < ofields.length; j++)
            {
                echo(ofields[j] + "");
            }
        }
        echo("Notification Descriptors:");
        dArray = ((ModelMBeanInfo) mbi).getDescriptors("notification");
        for (int i = 0; i < dArray.length; i++)
        {
            System.out.println("**Notification****************************");
            String[] nfields = ((Descriptor) dArray[i]).getFields();
            for (int j = 0; j < nfields.length; j++)
            {
                System.out.println(nfields[j] + "");
            }
        }
    }


    public void printModelMBeanDescriptors(ObjectName mbeanObjectName)
    {
        sleep(1000);
        Descriptor[] dArray = new DescriptorSupport[0];
        try
        {
            dArray = (Descriptor[]) (server.invoke(mbeanObjectName, "getDescriptors", new Object[] {},
                                                   new String[] {}));
            if (dArray == null)
            {
                echo("Descriptor list is null!");
            }
        }
        catch (Exception e)
        {
            echo("\t!!! Could not get descriptors for mbeanName ");
            e.printStackTrace();
            return;
        }
        echo("Descriptors: (");
        echo(dArray.length + ")");
        for (int i = 0; i < dArray.length; i++)
        {
            echo("**Descriptor***********************");
            String[] dlfields = ((Descriptor) dArray[i]).getFields();
            for (int j = 0; j < dlfields.length; j++)
            {
                echo(dlfields[j] + "");
            }
        }
    }
}