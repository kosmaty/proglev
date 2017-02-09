package com.proglev.util;

import javafx.util.Callback;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SpringContextControllerFactory implements Callback<Class<?>, Object> {

    @Resource
    private ApplicationContext ctx;

    SpringContextControllerFactory(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Object call(Class<?> clazz) {
        return ctx.getBean(clazz);
    }
}