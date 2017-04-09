package com.cooeration.marketengine.core.hotdeploy;

import org.apache.xbean.spring.context.ResourceXmlApplicationContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

public class JarResourceXmlApplicationContext extends ResourceXmlApplicationContext
{
    public JarResourceXmlApplicationContext(Resource resource)   {
        super(resource);
    }

    @Override
    protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
        reader.setValidating(false);
    }
}
