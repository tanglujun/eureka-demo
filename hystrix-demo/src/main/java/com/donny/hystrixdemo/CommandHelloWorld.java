/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.donny.hystrixdemo;

import com.netflix.hystrix.*;

/**
 * 功能说明
 * <p>
 *
 * @author 唐陆军
 * @version 1.0.0
 * @date 2020/12/1
 */
public class CommandHelloWorld extends HystrixCommand<String> {


    private final String name;

    public CommandHelloWorld(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HelloWorldPool"))
                .andCommandPropertiesDefaults(getSetter())
        );
        this.name = name;
    }

    @Override
    protected String run() {
        // a real example would do work like a network call here
        System.out.println(Thread.currentThread().getName());
        //return "Hello " + name + "!";

        throw new RuntimeException("异常");
    }


    protected static Setter getSetter(final String commandKey) {
        Setter commandSetter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RibbonCommand")).andCommandKey(com.netflix.hystrix.HystrixCommandKey.Factory.asKey(commandKey));
        com.netflix.hystrix.HystrixCommandProperties.Setter setter = HystrixCommandProperties.defaultSetter();
        String threadPoolKey;

        return commandSetter.andCommandPropertiesDefaults(setter);
    }


    public static HystrixCommandProperties.Setter getSetter() {
        com.netflix.hystrix.HystrixCommandProperties.Setter setter = HystrixCommandProperties.defaultSetter();
        setter.withExecutionTimeoutEnabled(false);
        return setter;
    }

    @Override
    protected String getFallback() {
        return "降级方法";
    }
}
