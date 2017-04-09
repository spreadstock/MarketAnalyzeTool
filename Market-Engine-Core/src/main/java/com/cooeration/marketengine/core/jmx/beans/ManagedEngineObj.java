package com.cooeration.marketengine.core.jmx.beans;

import com.cooeration.marketengine.core.jmx.annotation.JmxConstructor;
import com.cooeration.marketengine.core.jmx.annotation.JmxMethod;
import com.cooeration.marketengine.core.jmx.annotation.ManagedJmxBean;
import com.cooeration.marketengine.framework.engine.Engine;

@ManagedJmxBean
public class ManagedEngineObj
{
    private Engine manager;


    @JmxConstructor
    public ManagedEngineObj(Engine manager)
    {
        this.manager = manager;
    }


    @JmxMethod
    public String getStatus()
    {
        return manager.getEngineStatus().getStatus().getStatusStr();
    }

}
