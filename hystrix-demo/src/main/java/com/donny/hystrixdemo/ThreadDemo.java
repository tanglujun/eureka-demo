/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.donny.hystrixdemo;

import rx.functions.Func0;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 功能说明
 * <p>
 *
 * @author 唐陆军
 * @version 1.0.0
 * @date 2020/12/3
 */
public class ThreadDemo {

    public static void main(String[] args) {

        ExecutorService executorService= Executors.newSingleThreadExecutor();

        Callable<String> call=new Func0<String>(){

            public String call() {
                return "hello";
            }
        };

        Future<String> callable = executorService.submit(call);

    }
}
