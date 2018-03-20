package com.ning.jfinal.test.common;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.ning.jfinal.test.controller.HelloController;

/**
 * Created by ning on 2018/3/16.
 * User:ning
 * Date:2018/3/16
 * tIME:8:21
 */
public class TestJFinalConfig extends JFinalConfig {
    // 先加载开发环境配置，再追加生产环境的少量配置覆盖掉开发环境配置
    private static Prop p = PropKit.use("config.properties")
            .appendIfExists("jdbc.properties");
    private WallFilter wallFilter;
    public static void main(String[] args) {
        // IDEA 下的启动方式
        JFinal.start("src/main/webapp", 80, "/");
    }

    public void configConstant(Constants me) {
        PropKit.use("config.properties");
        PropKit.use("jdbc.properties");
        me.setDevMode(true);
    }
    public void configRoute(Routes me) {
        me.setBaseViewPath("/view");
        me.add("/hello", HelloController.class);
    }
    public void configEngine(Engine me) {

    }
    /**
     * 抽取成独立的方法，便于 _Generator 中重用该方法，减少代码冗余
     */
    public static DruidPlugin getDruidPlugin() {
        return new DruidPlugin(
                p.get("jdbc.url"),
                p.get("jdbc.username"),
                p.get("jdbc.password").trim());
    }
    public void configPlugin(Plugins me) {
        DruidPlugin druidPlugin = getDruidPlugin();
        wallFilter = new WallFilter();              // 加强数据库安全
        wallFilter.setDbType("mysql");
        druidPlugin.addFilter(wallFilter);
        druidPlugin.addFilter(new StatFilter());    // 添加 StatFilter 才会有统计数据
        me.add(druidPlugin);


        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        me.add(arp);


    }
    // 系统启动完成后回调
    public void afterJFinalStart() {
        System.out.println("------------afterJFinalStart-------------");
    }

    // 系统关闭之前回调
    public void beforeJFinalStop() {
        System.out.println("------------beforeJFinalStop-------------");
    }
    public void configInterceptor(Interceptors me) {}
    public void configHandler(Handlers me) {}
}
