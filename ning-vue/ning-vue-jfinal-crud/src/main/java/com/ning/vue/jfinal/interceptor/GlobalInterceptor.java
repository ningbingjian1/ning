package com.ning.vue.jfinal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class GlobalInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {

        inv.getController().getResponse().setHeader("Access-Control-Allow-Origin","*");
        inv.invoke();
    }
}
