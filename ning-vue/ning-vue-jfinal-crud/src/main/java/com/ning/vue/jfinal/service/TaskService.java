package com.ning.vue.jfinal.service;

import com.ning.vue.jfinal.entity.Task;

import java.util.List;

public class TaskService {
    public static TaskService taskService = new TaskService();
    public List<Task> queryAll(){
        return Task.dao.find("select * from t_task");
    }
}
