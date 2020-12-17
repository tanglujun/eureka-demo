/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.donny.hystrixdemo;


import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 功能说明
 * <p>
 *
 * @author 唐陆军
 * @version 1.0.0
 * @date 2020/12/1
 */
public class RxJavaDemo {


    public static void main(String[] args) {

        //观察者
        Observer observer=new Observer()
        {
            //完成时触发
            public void onCompleted() {
                System.out.println(Thread.currentThread().getName() +"观察者事件完成");
            }

            //出错时触发
            public void onError(Throwable e) {
                System.out.println(Thread.currentThread().getName() +"观察者事件出错");
            }

            //每接收一次事件 触发一次
            public void onNext(Object o) {
                System.out.println(Thread.currentThread().getName() +"观察者接收到事件，数据为："+o);
            }
        };


        //订阅者
        Subscriber subscriber=new Subscriber()
        {
            @Override
            public void onStart() {
                super.onStart();
                System.out.println(Thread.currentThread().getName() +"事件发生之前发生");
            }


            //完成时触发
            public void onCompleted() {
                System.out.println(Thread.currentThread().getName() +"订阅者事件完成");
            }

            //出错时触发
            public void onError(Throwable e) {
                System.out.println(Thread.currentThread().getName() +"订阅者事件出错");
            }

            //每接收一次事件 触发一次
            public void onNext(Object o) {
                System.out.println(Thread.currentThread().getName() +"订阅者接收到事件，数据为："+o);
            }
        };


        //生产者
        Observable observable=Observable.create(new Observable.OnSubscribe<String>(){
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(Thread.currentThread().getName() + "生产者触发事件");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(subscriber);


        Observable.create(new Observable.OnSubscribe<String>(){
            public void call(Subscriber<? super String> subscriber) {
                System.out.println(Thread.currentThread().getName() + "生产者触发事件");
                subscriber.onNext("100");
                subscriber.onCompleted();
            }
        }).map(new Func1<String,Integer>(){
            public Integer call(String s) {
                return Integer.valueOf(s);
            }
        }).subscribe(new Subscriber<Integer>() {
            public void onCompleted() {
                System.out.println(Thread.currentThread().getName() + "订阅者完成");
            }

            public void onError(Throwable e) {
                System.out.println(Thread.currentThread().getName() + "订阅者出错");
            }

            public void onNext(Integer s) {
                System.out.println(Thread.currentThread().getName() + "订阅者事件触发："+(s+10));
            }
        });


    }
}
