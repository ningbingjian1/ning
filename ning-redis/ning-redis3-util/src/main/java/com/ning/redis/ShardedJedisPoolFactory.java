/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ning.redis;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ShardedJedisPoolFactory extends BaseJedisPoolFactory {

    public static ShardedJedisPool createJedisPool(String configFilePath) {
        Properties props = loadConfig(configFilePath);
        int timeout = getProperty(props, "timeout", 2000);
        JedisPoolConfig config = createJedisPoolConfig(props);
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        String hosts = getProperty(props, "hosts", "localhost:6379");
        String[] hostArray = StringUtils.split(hosts, ";");
        for (String host : hostArray) {
            String[] ipport = StringUtils.split(host, ":");
            shards.add(new JedisShardInfo(ipport[0], Integer.valueOf(ipport[1]), timeout));
        }

        return new ShardedJedisPool(config, shards);
    }
}
