package com.xb.springxmlbean;

import java.io.Serializable;

public class Teacher implements Serializable
{

    private static final long serialVersionUID = -8409611806997881614L;

    private int age;
    private String name;
    private int teacherId;


    public void setTeacherId(int id)
    {
        this.teacherId = id;
    }


    public int getTeacherId()
    {
        return this.teacherId;
    }


    public int getAge()
    {
        return age;
    }


    public void setAge(int age)
    {
        this.age = age;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }
}
