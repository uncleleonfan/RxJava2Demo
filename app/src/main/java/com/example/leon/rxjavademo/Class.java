package com.example.leon.rxjavademo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 2017/2/20.
 */

public class Class {

    public Class (int id) {
        this.id = id;
//        mockStudents();
        mockGroups();
    }

    private void mockStudents() {
        mStudents = new ArrayList<Student>();
        for (int i = 0; i < 3; i++) {
            Student student = new Student();
            student.setAge(18);
            student.setName("Class" + id + "_" + String.valueOf(i));
            mStudents.add(student);
        }
    }



    private void mockGroups() {
        mGroups = new ArrayList<Group>();
        for (int i = 0; i < 3; i++) {
            Group group = new Group(i);
            mGroups.add(group);
        }
    }

    private int id;

    private List<Student>  mStudents;
    private List<Group> mGroups;


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

    public List<Group> getGroups() {
        return mGroups;
    }

    public void setGroups(List<Group> groups) {
        mGroups = groups;
    }

    @Override
    public String toString() {
        return "Class" + getId();
    }


}
