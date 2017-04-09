package com.cooeration.marketengine.core.jmx.annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JmxAnnotationUtil
{
    public static List<Field> getAttributes(Class<?> t)
    {
        List<Field> fields = new ArrayList<>();

        Field[] all = t.getDeclaredFields();
        fields = Arrays.asList(all);
        fields.stream().filter((Field a) -> {
            return a.isAnnotationPresent(JmxAttribute.class);
        });

        return fields;

    }


    public static List<Method> getMethods(Class<?> t)
    {
        List<Method> methods = new ArrayList<>();

        Method[] all = t.getMethods();
        methods = Arrays.asList(all);
        methods.stream().filter((Method a) -> {
            return a.isAnnotationPresent(JmxMethod.class);
        });

        return methods;
    }


    public static List<Constructor<?>> getConstructors(Class<?> t)
    {
        List<Constructor<?>> constructors = new ArrayList<>();

        Constructor<?>[] all = t.getConstructors();
        constructors = Arrays.asList(all);
        constructors.stream().filter((Constructor<?> a) -> {
            return a.isAnnotationPresent(JmxConstructor.class);
        });

        return constructors;
    }
}
