package com.ning.vue.jfinal.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.ning.vue.jfinal.entity.Task;
import com.ning.vue.jfinal.service.TaskService;

import java.util.List;

public class TaskController  extends Controller{
    private TaskService taskService = TaskService.taskService;

    public String save(){

        return null;
    }
    public void list(){
        List<Task> tasks = taskService.queryAll();

        Ret ret = Ret.ok().set("data",tasks);

        System.out.println(ret.toJson());

        renderJson(ret);

        getResponse().setHeader("Access-Control-Allow-Origin","*");

    }

}
