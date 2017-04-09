/**
 * 
 */
package com.cooeration.marketengine.core.jmx.beans;

import java.util.Collection;

import com.cooeration.marketengine.core.jmx.annotation.JmxAttribute;
import com.cooeration.marketengine.core.jmx.annotation.ManagedJmxBean;

/**
 * @author exubixu
 */
@ManagedJmxBean
public class Algorithm
{
    private int algId;
    @JmxAttribute
    private String name;
    @JmxAttribute
    private String classname;
    @JmxAttribute
    private TaskStatus status;
    @JmxAttribute
    private String output;
    @JmxAttribute
    private String input;
    @JmxAttribute
    private Collection<Algorithm> preconditions;


    public int getAlgId()
    {
        return algId;
    }


    public void setAlgId(int id)
    {
        this.algId = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public TaskStatus getStatus()
    {
        return status;
    }


    public void setStatus(TaskStatus status)
    {
        this.status = status;
    }

    public String getOutput()
    {
        return output;
    }


    public void setOutput(String output)
    {
        this.output = output;
    }


    public String getInput()
    {
        return input;
    }


    public void setInput(String input)
    {
        this.input = input;
    }


    public Collection<Algorithm> getPreconditions()
    {
        return preconditions;
    }


    public void setPreconditions(Collection<Algorithm> preconditions)
    {
        this.preconditions = preconditions;
    }


    public String getClassname()
    {
        return classname;
    }


    public void setClassname(String classname)
    {
        this.classname = classname;
    }

}
