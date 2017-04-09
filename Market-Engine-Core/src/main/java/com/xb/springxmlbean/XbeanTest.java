package com.xb.springxmlbean;

import java.io.File;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;

import com.cooeration.marketengine.core.jmx.beans.Algorithm;

public class XbeanTest
{
    public static void main(String[] args)
    {
        System.out.println("xubin");
        File a = new File("");
        System.out.println(a.getAbsolutePath());
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                        "file:C:/Users/exubixu/Desktop/Imp/Project/localproject/Market-Engine-Core/xbean.xml");
        Algorithm teacher = (Algorithm) context.getBean("localTest");

        // assertNotNull(teacher);
        // assertEquals(25, teacher.getAge());
        // assertEquals("xjacker", teacher.getName());
        System.out.println(teacher.getName());
        System.out.println(teacher.getInput());
        teacher.getPreconditions().forEach((x) -> {
            System.out.println(x.getName());
        });

    }
}
