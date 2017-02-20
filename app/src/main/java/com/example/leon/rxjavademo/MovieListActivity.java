package com.example.leon.rxjavademo;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Leon on 2017/2/20.
 */

public class MovieListActivity extends ListActivity {
    private static final String TAG = "MovieListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable<MovieBean> movieBeanObservable = MovieRetrofit.getInstance().getApi().listTop250(0, 10);
        movieBeanObservable.subscribeOn(Schedulers.io())
                .map(new Function<MovieBean, List<String>>() {
                    @Override
                    public List<String> apply(MovieBean movieBean) throws Exception {
                        List<String> array = new ArrayList<String>();
                        for (int i = 0; i < movieBean.getSubjects().size(); i++) {
                            String title = movieBean.getSubjects().get(i).getTitle();
                            Log.d(TAG, "apply: " + title);
                            array.add(title);
                        }
                        return array;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> value) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MovieListActivity.this, android.R.layout.simple_list_item_1, value);
                        setListAdapter(arrayAdapter);
                    }

                    @Override
                   public void onError(Throwable e) {
                        Toast.makeText(MovieListActivity.this, "onError", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MovieListActivity.this, "onComplete", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
