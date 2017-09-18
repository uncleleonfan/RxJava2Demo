package com.example.leon.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 黑马程序员
 */

public class BackPressureTestActivity extends AppCompatActivity {

    private static boolean interrupt = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpressure);
    }

    public void onTestBackPressure1(View view) {
        interrupt = false;
        testBackPressure1();
    }

    public void onTestBackPressure2(View view) {
        interrupt = false;
        testBackPressure2();
    }


    public void onTestBackPressure3(View view) {
        testBackPressure3();
    }


    public void onTestBackPressure4(View view) {
        interrupt = false;
        testBackPressure4();
    }


    public void onTestBackPressure5(View view) {
        testBackPressure5();
    }



    private static void testBackPressure1() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                //死循环，不停的发送HelloWorld，直到中断
                while (true) {
                    if (interrupt) return;
                    System.out.println("subscribe: Hello World");
                    e.onNext("Hello World");
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept Hello World");
            }
        });
    }

    private static void testBackPressure2() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                while (true) {
                    if (interrupt) return;
                    System.out.println("subscribe: Hello World");
                    e.onNext("Hello World");
                }
            }
        })
                .subscribeOn(Schedulers.io())//被观察者在io线程执行
                .observeOn(Schedulers.newThread())//观察者在新线程执行
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        sleep(10000);//休眠10s
                        System.out.println("accept Hello World");
                    }
                });
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onInterrupt(View view) {
        interrupt = true;
    }

    private void testBackPressure3() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            //发送3个Hello World
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                System.out.println("Send: Hello World1");
                e.onNext("Hello World1");
                System.out.println("Send: Hello World2");
                e.onNext("Hello World2");
                System.out.println("Send: Hello World3");
                e.onNext("Hello World3");

            }
        }, BackpressureStrategy.ERROR)//背压策略，抛出错误
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(3);
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("Receive: " + s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError " + t.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void testBackPressure4(){
        Flowable.create(new FlowableOnSubscribe<String>() {
            //发出3个Hello World
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                System.out.println("Send: Hello World1");
                e.onNext("Hello World1");
                System.out.println("Send: Hello World2");
                e.onNext("Hello World2");
                System.out.println("Send: Hello World3");
                e.onNext("Hello World3");
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())//在io线程订阅
                .observeOn(Schedulers.newThread())//在新线程观察
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //不请求处理数据
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("Receive: " + s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError " + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    private void testBackPressure5() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                //发送出128(缓存大小)个Hello World
                for (int i = 0; i < Flowable.bufferSize() + 1; i++) {
                    System.out.println("Send: Hello World " + i);
                    e.onNext("Hello World " + i);
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())//在io线程订阅
                .observeOn(Schedulers.newThread())//在新线程观察
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //不请求处理数据
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("Receive: " + s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError " + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }
}
