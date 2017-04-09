package com.cooeration.marketengine.core.jmx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface ManagedJmxBean {
   String value() default "";
}
