package com.ning.logback.demo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ning on 2018/1/27.
 * User:ning
 * Date:2018/1/27
 * tIME:11:09
 */
public class LogbackDemoSuite {
    //默认使用root节点的appender
    @Test
    public void testRootAppender(){
        Logger logger = LoggerFactory.getLogger(this.getClass().getName());
        logger.info("this is a test for testConsole ");
    }

    public void test
}
