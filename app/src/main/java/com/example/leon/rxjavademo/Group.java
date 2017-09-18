package com.example.leon.rxjavademo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 2017/2/20.
 */

public class Group {

    private int id;//小组编号

    private List<Student>  mStudents;//小组内的学生


    public Group(int id) {
        this.id = id;
        mockStudents();
    }

    private void mockStudents() {
        mStudents = new ArrayList<Student>();
        for (int i = 0; i < 3; i++) {
            String name = "Group" + id + "_" + String.valueOf(i);
            Student student = new Student(name);
            mStudents.add(student);
        }
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Student> getStudents() {
        return mStudents;
    }

    public void setStudents(List<Student> students) {
        mStudents = students;
    }

    @Override
    public String toString() {
        return "Group" + getId();
    }
}
