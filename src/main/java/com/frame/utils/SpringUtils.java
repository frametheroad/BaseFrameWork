package com.frame.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

@Component
public class SpringUtils implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public  Object getBean(String beanName) {
        return this.applicationContext.getBean(beanName);
    }

    public  <T> T getBean(Class<T> clazz) {
        return this.applicationContext.getBean(clazz);
    }

    public  <T> T getBean(String beanName, Class<T> clazz) {
        return this.applicationContext.getBean(beanName, clazz);
    }
    public Map<String, Object> getBeanWithAccotation(Class<? extends Annotation> clazz){
        return this.applicationContext.getBeansWithAnnotation(clazz);
    }

}

