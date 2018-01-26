/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ning.redis;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class ClusterJedisPoolFactory extends BaseJedisPoolFactory {

    public static JedisCluster createJedisPool(String configFilePath) {
        Properties       props        = loadConfig(configFilePath);
        int              timeout      = getProperty(props, "timeout", 2000);
        JedisPoolConfig  config       = createJedisPoolConfig(props);
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        String           hosts        = getProperty(props, "hosts", "localhost:6379");
        String[]         hostArray    = StringUtils.split(hosts, ";");
        for (String host : hostArray) {
            String[] ipport = StringUtils.split(host, ":");
            hostAndPorts.add(new HostAndPort(ipport[0], Integer.valueOf(ipport[1])));
        }

        return new JedisCluster(hostAndPorts, config);
    }
}
