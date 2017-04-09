package com.cooeration.marketengine.core.jmx.beans;

public enum TaskStatus {
    ACTIVE("scheduling"), INACTIVE("executing"), STARTIING("finished"), STOPPING("error");

    private String status;


    private TaskStatus(String input)
    {
        status = input;
    }


    public String getTaskStatus()
    {
        return this.status;
    }
}
