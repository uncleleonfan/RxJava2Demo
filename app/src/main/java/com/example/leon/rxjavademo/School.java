package com.example.leon.rxjavademo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 2017/2/20.
 */

public class School {

    List<Class> mClasses;

    public School() {
        mockClasses();
    }

    private void mockClasses() {
        mClasses = new ArrayList<Class>();
        for (int i = 0; i < 3; i++) {
            Class c = new Class(i);
            mClasses.add(c);
        }
    }

    public List<Class> getClasses() {
        return mClasses;
    }
}
