package com.cooeration.marketengine.core.hotdeploy;

public interface Deployer
{

    void deployAlgorithm(String fileName);

    void redeployAlgorithm(String fileName);

    void undeployAlgorithm(String fileName);

}
