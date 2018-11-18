/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ning.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import redis.clients.jedis.JedisPoolConfig;

public class BaseJedisPoolFactory {
    protected final static String PREFIX = "redis.";

    protected static JedisPoolConfig createJedisPoolConfig(Properties props) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setBlockWhenExhausted(getProperty(props, "blockWhenExhausted", true));
        config.setMaxIdle(getProperty(props, "maxIdle", 10));
        config.setMinIdle(getProperty(props, "minIdle", 5));
        config.setMaxTotal(getProperty(props, "maxTotal", 50));
        config.setMaxWaitMillis(getProperty(props, "maxWaitMillis", 100));
        config.setTestWhileIdle(getProperty(props, "testWhileIdle", false));
        config.setTestOnBorrow(getProperty(props, "testOnBorrow", true));
        config.setTestOnReturn(getProperty(props, "testOnReturn", false));
        config.setNumTestsPerEvictionRun(getProperty(props, "numTestsPerEvictionRun", 10));
        config.setMinEvictableIdleTimeMillis(getProperty(props, "minEvictableIdleTimeMillis", 1000));
        config.setSoftMinEvictableIdleTimeMillis(getProperty(props, "softMinEvictableIdleTimeMillis", 10));
        config.setTimeBetweenEvictionRunsMillis(getProperty(props, "timeBetweenEvictionRunsMillis", 10));
        config.setLifo(getProperty(props, "lifo", false));
        return config;
    }

    protected static Properties loadConfig(String configFilePath) {
        InputStream configStream = BaseJedisPoolFactory.class.getClassLoader().getParent()
                .getResourceAsStream(configFilePath);
        if (configStream == null) {
            configStream = BaseJedisPoolFactory.class.getResourceAsStream(configFilePath);
        }
        if (configStream == null) {
            throw new RuntimeException("Cannot find " + configFilePath + " !!!");
        }
        Properties props = new Properties();
        try {
            props.load(configStream);
            configStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load " + configFilePath + " !!!");
        }

        Properties propsNoPrefix = new Properties();
        Enumeration<Object> keys = props.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.startsWith(PREFIX))
                propsNoPrefix.setProperty(key.substring(PREFIX.length()), props.getProperty(key));
        }
        return propsNoPrefix;
    }

    protected static String getProperty(Properties props, String key, String defaultValue) {
        return props.getProperty(key, defaultValue).trim();
    }

    protected static int getProperty(Properties props, String key, int defaultValue) {
        try {
            return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)).trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    protected static boolean getProperty(Properties props, String key, boolean defaultValue) {
        return "true".equalsIgnoreCase(props.getProperty(key, String.valueOf(defaultValue)).trim());
    }
}
