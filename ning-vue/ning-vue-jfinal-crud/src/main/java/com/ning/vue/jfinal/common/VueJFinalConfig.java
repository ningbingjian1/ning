package com.ning.vue.jfinal.common;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

import java.sql.Connection;

public class VueJFinalConfig extends JFinalConfig {
    private WallFilter wallFilter;
    public static void main(String[] args) {
        // IDEA 下的启动方式

        JFinal.start("src/main/webapp", 80, "/");
        //JFinal.start("ning-vue/ning-vue-jfinal-crud/src/main/webapp", 80, "/");
    }
    private static Prop p = PropKit.use("jdbc.properties");

    public void configConstant(Constants me) {
        System.out.println("---------------configConstant------------");
        me.setDevMode(true);
    }
    public void configRoute(Routes me) {
        me.add(new NingRoutes());

    }
    public void configEngine(Engine me) {


    }


    /**
     * 抽取成独立的方法，便于 _Generator 中重用该方法，减少代码冗余
     */
    public static DruidPlugin getDruidPlugin() {
        return new DruidPlugin(p.get("jdbc.url"), p.get("jdbc.username"), p.get("jdbc.password").trim());
    }
    public void configPlugin(Plugins me) {
        DruidPlugin druidPlugin = getDruidPlugin();
        wallFilter = new WallFilter();              // 加强数据库安全
        wallFilter.setDbType("mysql");
        druidPlugin.addFilter(wallFilter);
        druidPlugin.addFilter(new StatFilter());    // 添加 StatFilter 才会有统计数据
        me.add(druidPlugin);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
        me.add(arp);
        _MappingKit.mapping(arp);
    }
    public void configInterceptor(Interceptors me) {

    }
    public void configHandler(Handlers me) {

    }
}
