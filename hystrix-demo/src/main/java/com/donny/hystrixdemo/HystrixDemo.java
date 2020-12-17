/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.donny.hystrixdemo;

import java.util.concurrent.ExecutionException;

/**
 * 功能说明
 * <p>
 *
 * @author 唐陆军
 * @version 1.0.0
 * @date 2020/12/1
 */
public class HystrixDemo {


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CommandHelloWorld commandHelloWorld=new CommandHelloWorld("World");

//        Future<String> fs = commandHelloWorld.queue();
//
//        String s = fs.get();
//
//        System.out.println(s);
//
//
//        commandHelloWorld.execute();

        //同步执行
        String s = commandHelloWorld.execute();

        System.out.println(Thread.currentThread().getName()+" "+s);
    }
}
