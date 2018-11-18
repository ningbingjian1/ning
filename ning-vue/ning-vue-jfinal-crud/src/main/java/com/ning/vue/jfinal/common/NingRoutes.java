package com.ning.vue.jfinal.common;

import com.jfinal.config.Routes;
import com.ning.vue.jfinal.controller.NingController;
import com.ning.vue.jfinal.controller.TaskController;

public class NingRoutes extends Routes {
    public void config() {
        setBaseViewPath("/view");
        add("/ning",NingController.class);
        add("/task",TaskController.class);
    }
}
