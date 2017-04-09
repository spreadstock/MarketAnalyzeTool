package com.cooeration.marketengine.framework.engine;

import java.util.ArrayList;

import com.cooeration.marketengine.framework.services.CoreService;

public class ServiceManagerCenter
{

    ArrayList<CoreService> allServices = new ArrayList<>();


    public void startServices()
    {
        allServices.forEach((x) -> x.start());
    }


    public void registerService(CoreService service)
    {
        allServices.add(service);
    }

}
