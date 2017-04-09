/**
 * 
 */
package com.xb.jmx.example1;

/**
 * @author exubixu
 */
public class Car
{
    private String color = "red";


    public String getColor()
    {
        return color;
    }


    public void setColor(String color)
    {
        this.color = color;
    }


    public void drive()
    {
        System.out.println("Baby you can drive my car.");
    }
}
