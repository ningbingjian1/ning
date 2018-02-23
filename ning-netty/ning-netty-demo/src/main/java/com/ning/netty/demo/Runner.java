package com.ning.netty.demo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Runner {
    public static void main(String[] args)throws Exception {
        String clazz = args[0];
        if(args.length > 1 ){
            List<String> argList = new ArrayList<>(args.length - 1);
            for (int i = 1; i < args.length; i++) {
                argList.add(args[i]);
            }
            callMain(clazz,argList.toArray(new String[]{}));
        }else if(args.length == 1){
            callMain(clazz,null);
        }else{
            System.out.println("必须传入包含main方法的包名全路径");
            System.exit(0);
        }

    }
    public static void callMain(String clazz,String[]args)throws Exception{
        Method method = Class.forName(clazz).getMethod("main",String[].class);
        method.invoke(null,(Object)args);
    }
}
