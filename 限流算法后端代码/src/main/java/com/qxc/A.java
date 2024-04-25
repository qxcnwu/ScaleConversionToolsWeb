package com.qxc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Author qxc
 * @Date 2024 2024/4/14 23:50
 * @Version 1.0
 * @PACKAGE com.qxc
 */
@Component
public class A implements BeanPostProcessor {

    public String a = "A BeanPostProcessor";

    {
        System.out.println(a);
        a = "qxc";
        System.out.println(a);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName);
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName);
        return bean;
    }
}
