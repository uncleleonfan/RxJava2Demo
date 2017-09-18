package com.example.leon.rxjavademo;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class Main {

    public static void main(String[] args) {
        testObserveOn();
    }



    private static void test() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                System.out.println("subscribe: " + Thread.currentThread().getName());
                e.onNext("Hello World");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept " + Thread.currentThread().getName());
            }
        });
    }

    private static void testSubscribeOn() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                System.out.println("subscribe: " + Thread.currentThread().getName());
                e.onNext("Hello World");
            }
        }).subscribeOn(Schedulers.newThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept " + Thread.currentThread().getName());
            }
        });
        sleep(1000);
    }

    private static void testObserveOn(){
        Observable.just("Hello World")
                .observeOn(Schedulers.newThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        System.out.println("test1: " + Thread.currentThread().getName());
                        return true;
                    }
                })
                .observeOn(Schedulers.io())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        System.out.println("test2: "+ Thread.currentThread().getName());
                        return true;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("accept "+ Thread.currentThread().getName());
                    }
                });
        sleep(1000);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void scheduleThreads() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                System.out.println("subscribe: " + Thread.currentThread().getName());
                e.onNext("Hello Leon Fan");
            }
        })
                .observeOn(Schedulers.newThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        System.out.println("test1 " + Thread.currentThread().getName());
                        return true;
                    }
                })
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        System.out.println("test2 " + Thread.currentThread().getName());
                        return true;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("accept " + Thread.currentThread().getName());
                    }
                });
    }

    private static void flatMapSchoolToClassToGroupToStudent() {
        Observable.fromIterable(new School().getClasses())
                //输入是Class类型，输出是ObservableSource<Group>类型
                .flatMap(new Function<Class, ObservableSource<Group>>() {
                    @Override
                    public ObservableSource<Group> apply(Class aClass) throws Exception {
                        System.out.println("apply: " + aClass.toString());
                        return Observable.fromIterable(aClass.getGroups());
                    }
                })
                //输入类型是Group，输出类型是ObservableSource<Student>类型
                .flatMap(new Function<Group, ObservableSource<Student>>() {
                    @Override
                    public ObservableSource<Student> apply(Group group) throws Exception {
                        System.out.println("apply: " + group.toString());
                        return Observable.fromIterable(group.getStudents());
                    }
                })
                .subscribe(
                        new Observer<Student>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                System.out.println("onSubscribe: ");
                            }

                            @Override
                            public void onNext(Student value) {
                                System.out.println("onNext: " + value.toString());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
    }

    private static void flatMapClassToGroupToStudent() {
        Observable.just(new Class(0))//发送一个班级Class
                .flatMap(new Function<Class, ObservableSource<Group>>() {
                    @Override
                    public ObservableSource<Group> apply(Class aClass) throws Exception {
                        System.out.println("apply: " + aClass);
                        //返回一个新的被观察者，新的被观察者会发出所有的Group
                        return Observable.fromIterable(aClass.getGroups());
                    }
                })
                .flatMap(new Function<Group, ObservableSource<Student>>() {
                    //处理一个Group
                    @Override
                    public ObservableSource<Student> apply(Group group) throws Exception {
                        System.out.println("apply: " + group);
                        //返回一个新的观察者，新的观察者会发送出Group里面所有的Student
                        return Observable.fromIterable(group.getStudents());
                    }
                })
                .subscribe(
                        new Observer<Student>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(Student value) {
                                System.out.println("onNext: " + value.toString());
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
    }

    private static void flatMapGroupToStudent() {
        Observable.just(new Group(0))
                .flatMap(new Function<Group, ObservableSource<Student>>() {
                    @Override
                    public ObservableSource<Student> apply(Group group) throws Exception {
                        return Observable.fromIterable(group.getStudents());
                    }
                }).subscribe(new Observer<Student>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(Student student) {
                System.out.println(student);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }


    private static void map() {
        Student student = new Student("Leon");
        //map操作符，从Student类型转换成Developer
        Observable.just(student).map(new Function<Student, Developer>() {
            @Override
            public Developer apply(Student student) throws Exception {
                System.out.println("apply: " + student.toString());
                Developer developer = new Developer();
                developer.setName(student.getName());
                developer.setSkill("Android");
                return developer;
            }
        }).subscribe(new Observer<Developer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe: ");
            }

            @Override
            public void onNext(Developer value) {
                System.out.println("onNext: " + value.toString());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError: ");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete: ");
            }
        });
    }

    private static void filter() {
        Observable.just("包子", "馒头", "肠粉", "春卷", "饺子", "炒粉")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        System.out.println("test: " + s);
                        return s.equals("饺子");//只允许饺子通过检查
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("accept: " + s);
                    }
                });
    }

    private static void helloWorldComplex() {
        //创建一个观察者
        Observer<String> observer = new Observer<String>() {

            Disposable mDisposable;

            //当Observable调用subscribe方法时会回调该方法
            @Override
            public void onSubscribe(Disposable d) {

                mDisposable = d;
                System.out.println("onSubscribe: ");
            }

            //onSubscribe方法后调用
            @Override
            public void onNext(String value) {
                System.out.println("onNext: " + value);
                if (value.equals("!")) {
                    mDisposable.dispose();
                }
            }

            //这里没有出错，没有被调用
            @Override
            public void onError(Throwable e) {
                System.out.println("onError: ");
            }

            //onNext之后调用
            @Override
            public void onComplete() {
                System.out.println("onComplete: ");
            }
        };

//        Observable.just("Hello World").subscribe(observer);
        Observable.just("Hello World", "!", "!").subscribe(observer);
    }

    /*    private static void helloWorldPlus() {
            //创建一个观察者
            Observer<String> observer = new Observer<String>() {

                //当Observable调用subscribe方法时会回调该方法
                @Override
                public void onSubscribe(Disposable d) {
                    System.out.println("onSubscribe: ");
                }

                //onSubscribe方法后调用
                @Override
                public void onNext(String value) {
                    System.out.println("onNext: " + value);
                }

                //这里没有出错，没有被调用
                @Override
                public void onError(Throwable e) {
                    System.out.println("onError: ");
                }

                //onNext之后调用
                @Override
                public void onComplete() {
                    System.out.println("onComplete: ");
                }
            };

            Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {

                @Override
                public void subscribe(ObservableEmitter<String> e) throws Exception {
                    System.out.println("Send Hello World");
                    e.onNext("Hello World");//会调用到观察者的onNext
                    System.out.println("Send !");
                    e.onNext("!");
                    System.out.println("Send !");
                    e.onNext("!");
                    System.out.println("Send Complete");
                    e.onComplete();//会调用到观察者的onComplete
                }
            });

            observable.subscribe(observer);
        }*/
    private static void helloWorldPlus() {
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                System.out.println("Send Hello World");
                e.onNext("Hello World");//会调用到观察者的onNext
                System.out.println("Send !");
                e.onNext("!");
                System.out.println("Send !");
                e.onNext("!");
                System.out.println("Send Complete");
                e.onComplete();//会调用到观察者的onComplete
            }
        }).subscribe(new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe: ");
            }

            @Override
            public void onNext(String value) {
                System.out.println("onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError: ");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete: ");
            }
        });
    }


    //简单版本
    private static void helloWorldSimple() {
        //创建消费者，消费者接受一个String类型的事件
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept: " + s);
            }
        };
        //被观察者发出Hello World, 并且指定该事件的消费者为consumer
//        Observable.just("Hello World").subscribe(consumer);
        Observable.just("Hello World", "Hello World", "Hello World").subscribe(consumer);
    }

}
