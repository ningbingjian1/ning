package com.ning.jfinal.test.controller;

import com.jfinal.core.Controller;

/**
 * Created by ning on 2018/3/16.
 * User:ning
 * Date:2018/3/16
 * tIME:9:30
 */

public class HelloController extends Controller {

    public String index(){
        System.out.println("h");
        return "hello.html";
    }
}
