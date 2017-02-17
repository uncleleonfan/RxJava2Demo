# RxJava & RxAndroid （2.0版）#

## 定义 ##
>RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.

>一个在 Java VM 上使用可观测的序列来组成异步的、基于事件的程序的库

初学者如果看到这个准确但晦涩的定义肯定一脸懵逼，不过我们我们只要把握重点即可：

* 异步
* 基于事件
* 观察者模式


>RxAndroid - Android specific bindings for RxJava 2.This module adds the minimum classes to RxJava that make writing reactive components in Android applications easy and hassle-free.
>
> RxAndroid在RxJava的基础上添加了最少的类使得开发Android应用中的响应式组件更加的容易和自由

## 特点 ##
简洁，并不是单单指代码量上的那种简洁，而是逻辑上的简洁，随着程序逻辑变得越来越复杂，它依然能够保持简洁。

## Github ##
* [RxJava](https://github.com/ReactiveX/RxJava)
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)

# Hello world #
## 添加依赖 ##
	compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
	compile 'io.reactivex.rxjava2:rxjava:2.0.1'

## 简单版本 ##
    
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

## 复杂版本 ##

    private void helloWorldComplex() {
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
        //被观察者发出Hello World, 并且指定该事件的观察者为observer
        Observable.just("Hello World").subscribe(observer);
    }

## 变态版本 ##
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

# 过滤 #
你早上去吃早餐，是否是被观察者，说咱这有包子，馒头，肠粉，春卷，饺子，炒粉，你仔细想了想，发现你是最喜欢饺子的，所以把其他的都排除掉，
于是你就吃到了饺子。

    private void filter() {
        //把Consumer可以看做精简版的Observer
        Consumer<String> consumer = new Consumer<String>() {
            //accept其实就是onNext
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

# 变换 #



# 参考 #
* [给Android开发者的Rx详解](http://gank.io/post/560e15be2dca930e00da1083)
* [RxJava2-Android-Samples](https://github.com/amitshekhariitbhu/RxJava2-Android-Samples)
* [关于 RxJava 最友好的文章—— RxJava 2.0 全新来袭](http://www.jianshu.com/p/220955eefc1f)