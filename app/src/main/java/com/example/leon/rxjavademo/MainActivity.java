package com.example.leon.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        helloWorldSimple();
//        helloWorldComplex();
//        helloWorldPlus();
        filter();
    }

    private void filter() {
        //把Consumer可以看做精简版的Observer
        Consumer<String> consumer = new Consumer<String>() {
            //onNext
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);//这里只能吃上饺子
            }
        };

        Observable.just("包子", "馒头", "肠粉", "春卷", "饺子", "炒粉")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        Log.d(TAG, "test: " + s);
                        return s.equals("饺子");//只允许饺子通过测试
                    }
                })
                .subscribe(consumer);
    }

    private void helloWorldComplex() {
        //创建一个观察者
        Observer<String> observer = new Observer<String>() {

            //当Observable调用subscribe方法时会回调该方法
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            //onSubscribe方法后调用
            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
            }

            //这里没有出错，没有被调用
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            //onNext之后调用
            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };

        Observable.just("Hello World").subscribe(observer);


    }

    private void helloWorldPlus() {
        //创建一个观察者
        Observer<String> observer = new Observer<String>() {

            //当Observable调用subscribe方法时会回调该方法
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            //onSubscribe方法后调用
            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
            }

            //这里没有出错，没有被调用
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            //onNext之后调用
            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("Hello World");//会调用到观察者的onNext
                e.onComplete();//会调用到观察者的onComplete
            }
        });

        observable.subscribe(observer);
    }

    //简单版本
    private void helloWorldSimple() {
        //创建消费者，消费者接受一个String类型的事件
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        };
        //被观察者发出Hello World, 并且指定该事件的消费者为consumer
        Observable.just("Hello World").subscribe(consumer);
    }
}
