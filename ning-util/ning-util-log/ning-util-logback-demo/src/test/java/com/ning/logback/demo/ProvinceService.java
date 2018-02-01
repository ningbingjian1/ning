package com.ning.logback.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/1/26
 * Time: 21:04
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 */
public class ProvinceService {
    Logger logger = LoggerFactory.getLogger(ProvinceService.class);
    public void save(String province){
        MDC.put("province",province);
        logger.info( province + " log in ProvinceService");
        MDC.remove("province");
    }
}
