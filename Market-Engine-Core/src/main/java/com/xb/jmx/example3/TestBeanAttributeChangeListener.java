/**
 * 
 */
package com.xb.jmx.example3;

import javax.management.NotificationListener;
import javax.management.AttributeChangeNotification;
import javax.management.Notification;

/**
 * 实现了NotificationListener接口. 监听和接受由ModelMBean发出的改变属性的通知
 */
public class TestBeanAttributeChangeListener implements NotificationListener
{
    public void handleNotification(Notification acn, Object handback)
    {
        echo("\n\tTestBeanAttributeChangeListener received Attribute ChangeNotification ");
        AttributeChangeNotification myacn = (AttributeChangeNotification) acn;
        echo("\t\tEvent: " + acn.getType());
        echo("\t\tAttribute: " + myacn.getAttributeName());
        echo("\t\tAttribute type: " + myacn.getAttributeType());
        echo("\t\tOld value: " + myacn.getOldValue());
        echo("\t\tNew value: " + myacn.getNewValue());
    }


    private static void echo(String outstr)
    {
        System.out.println(outstr);
    }
}
