/**
 * 
 */
package com.cooeration.marketengine.core.jmx.beans;

import com.cooeration.marketengine.core.jmx.annotation.JmxAttribute;
import com.cooeration.marketengine.core.jmx.annotation.JmxConstructor;
import com.cooeration.marketengine.core.jmx.annotation.JmxMethod;
import com.cooeration.marketengine.core.jmx.annotation.ManagedJmxBean;

/**
 * @author exubixu
 */

@ManagedJmxBean
public class Car
{

    @JmxConstructor
    public Car(String color)
    {
        this.color = color;
    }

    @JmxAttribute
    private String color = "red";


    @JmxMethod
    public String getColor()
    {
        System.out.println("getColor." + color);
        return color;
    }


    @JmxMethod
    public void setColor(String color)
    {
        this.color = color;
    }


    @JmxMethod
    public void drive()
    {
        System.out.println("Baby you can drive my car.");
    }
}
