/**
 * 
 */
package com.xb.jarload;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author exubixu
 *
 */
public class TestMain
{
    public static void main(String[] args)
    {
        try
        {
            // х┌Х■оо
            URL url1 = new URL("file:C:/Users/exubixu/Desktop/engine.jar");
            URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] {
                            url1
            }, Thread.currentThread().getContextClassLoader());
            
            Class<?> myClass1 = myClassLoader1.loadClass("com.xb.springxmlbean.Teacher");
            
            System.out.println( myClass1.newInstance().getClass());
            
            myClassLoader1.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
