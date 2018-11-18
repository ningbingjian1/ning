package com.ning.vue.jfinal.entity;

import com.jfinal.plugin.activerecord.Model;

public class Task extends Model<Task>{
    public static final Task dao = new Task().dao();
    private int id ;
    private String name ;
    private String task_time;
    private String desc;
    private boolean deleted = true;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTask_time() {
        return task_time;
    }

    public void setTask_time(String task_time) {
        this.task_time = task_time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", task_time='" + task_time + '\'' +
                ", desc='" + desc + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
