package com.ning.logback.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhaoshufen
 * User:  zhaoshufen
 * Date: 2018/1/26
 * Time: 20:50
 * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
 */
public class UserService {
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    public void save(){
        logger.warn("save in userService");
    }
}
