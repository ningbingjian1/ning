package com.ning.vue.jfinal.common;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.ning.vue.jfinal.entity.Task;

public class _MappingKit {
    public static void mapping(ActiveRecordPlugin arp) {
        arp.addMapping("t_task", "id", Task.class);
    }
}
