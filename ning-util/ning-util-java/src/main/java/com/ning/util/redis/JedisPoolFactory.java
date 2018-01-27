/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ning.util.redis;


import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

public class JedisPoolFactory extends BaseJedisPoolFactory {

    static JedisPool createJedisPool(String configFilePath) {
        Properties props = loadConfig(configFilePath);
        String host = getProperty(props, "host", "127.0.0.1");
        String password = props.getProperty("password", null);
        int port = getProperty(props, "port", 6379);
        int timeout = getProperty(props, "timeout", 2000);
        int database = getProperty(props, "database", 0);
        JedisPoolConfig config = createJedisPoolConfig(props);
        return new JedisPool(config, host, port, timeout, password, database);
    }
}
