package com.cooeration.marketengine.framework.engine;

public class EngineStatus
{
    Status mystatus;


    public EngineStatus()
    {
        mystatus = Status.INACTIVE;
    }
    
    public Status getStatus()
    {
        return mystatus;
    }


    public void updateStatus(Status status)
    {
        mystatus = status;
    }

    public enum Status {
        ACTIVE("ACTIVE"), INACTIVE("INACTIVE"),STARTIING("STARTIING"),STOPPING("STOPPING");

        private String status;


        private Status(String input)
        {
            status = input;
        }

        public String getStatusStr()
        {
            return this.status;
        }
    }
}
