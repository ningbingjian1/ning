/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ning.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在redis-sharded.properties中可以配置多个redis服务器，通过一致性hash来get set。
 *
 * 从tcl接手的ShardedJedisTemplate代码的备份  因为升级了redis为集群模式，而又不了解
 * 原先的代码，只能在ShardedJedisTemplate中将代码都修改为JedisCluster进行操作
 *
 */
public class ShardedJedisTemplate {
    private static final String                            DEFAULT_CONFIG_FILE = "/redis/redis-sharded.properties";
    private static Map<String, ShardedJedisTemplate> jedisTemplates      = new HashMap<String, ShardedJedisTemplate>();

    public static ShardedJedisTemplate getInstance() {
        return getInstance(DEFAULT_CONFIG_FILE);
    }

    public static ShardedJedisTemplate getInstance(String configFilePath) {
        if (jedisTemplates.get(configFilePath) == null) {
            synchronized (ShardedJedisTemplate.class) {
                if (jedisTemplates.get(configFilePath) == null) {
                    ShardedJedisPool           pool          = ShardedJedisPoolFactory.createJedisPool(configFilePath);
                    ShardedJedisTemplate jedisTemplate = new ShardedJedisTemplate(pool);
                    jedisTemplates.put(configFilePath, jedisTemplate);
                }
            }
        }
        return jedisTemplates.get(configFilePath);
    }

    private static Logger logger = LoggerFactory.getLogger(ShardedJedisTemplate.class);

    private ShardedJedisPool jedisPool;

    protected ShardedJedisTemplate(ShardedJedisPool pool) {
        this.jedisPool = pool;
    }

    /**
     * 执行有返回结果的action。
     */
    public <T> T execute(JedisAction<T> jedisAction) {
        ShardedJedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            return jedisAction.action(jedis);
        } catch (JedisConnectionException e) {
            logger.error("Redis connection lost.", e);
            broken = true;
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * 执行无返回结果的action。
     */
    public void execute(JedisActionNoResult jedisAction) {
        ShardedJedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            jedisAction.action(jedis);
        } catch (JedisConnectionException e) {
            logger.error("Redis connection lost.", e);
            broken = true;
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * 根据连接是否已中断的标志，分别调用returnBrokenResource或returnResource。
     */
    protected void closeResource(ShardedJedis jedis, boolean connectionBroken) {
        if (jedis != null) {
            try {
                if (connectionBroken) {
                    jedisPool.returnBrokenResource(jedis);
                } else {
                    jedisPool.returnResource(jedis);
                }
            } catch (Exception e) {
                logger.error("Error happen when return jedis to pool, try to close it directly.", e);
            }
        }
    }

    /**
     * 获取内部的pool做进一步的动作。
     */
    public ShardedJedisPool getJedisPool() {
        return jedisPool;
    }

    /**
     * 有返回结果的回调接口定义。
     */
    public interface JedisAction<T> {
        T action(ShardedJedis jedis);
    }

    /**
     * 无返回结果的回调接口定义。
     */
    public interface JedisActionNoResult {
        void action(ShardedJedis jedis);
    }

    // ////////////// 常用方法的封装 ///////////////////////// //

    // ////////////// 公共 ///////////////////////////
    /**
     * 删除key, 如果key存在返回true, 否则返回false。
     */
    public Boolean del(final String key) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(ShardedJedis jedis) {
                return jedis.del(key) == 1 ? true : false;
            }
        });
    }

    // ////////////// 关于String ///////////////////////////
    /**
     * 如果key不存在, 返回null.
     */
    public String get(final String key) {
        return execute(new JedisAction<String>() {

            @Override
            public String action(ShardedJedis jedis) {
                return jedis.get(key);
            }
        });
    }

    // ////////////// 关于String ///////////////////////////
    /**
     * 如果key不存在, 返回null.
     */
    public byte[] get(final byte[] key) {
        return execute(new JedisAction<byte[]>() {

            @Override
            public byte[] action(ShardedJedis jedis) {
                return jedis.get(key);
            }
        });
    }

    /**
     * 如果key不存在, 返回null.
     */
    public Long getAsLong(final String key) {
        String result = get(key);
        return result != null ? Long.valueOf(result) : null;
    }

    /**
     * 如果key不存在, 返回null.
     */
    public Integer getAsInt(final String key) {
        String result = get(key);
        return result != null ? Integer.valueOf(result) : null;
    }

    public void set(final String key, final String value) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(ShardedJedis jedis) {
                jedis.set(key, value);
            }
        });
    }

    public void expire(final String key, final int seconds) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(ShardedJedis jedis) {
                jedis.expire(key, seconds);
            }
        });
    }

    public void expire(final byte[] key, final int seconds) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(ShardedJedis jedis) {
                jedis.expire(key, seconds);
            }
        });
    }

    public void set(final byte[] key, final byte[] value) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(ShardedJedis jedis) {
                jedis.set(key, value);
            }
        });
    }

    public void setex(final String key, final String value, final int seconds) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(ShardedJedis jedis) {
                jedis.setex(key, seconds, value);
            }
        });
    }

    /**
     * 如果key还不存在则进行设置，返回true，否则返回false.
     */
    public Boolean setnx(final String key, final String value) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(ShardedJedis jedis) {
                return jedis.setnx(key, value) == 1 ? true : false;
            }
        });
    }

    public Long incr(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(ShardedJedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    public Long decr(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(ShardedJedis jedis) {
                return jedis.decr(key);
            }
        });
    }

    // ////////////// 关于List ///////////////////////////
    public Long lpush(final String key, final String... values) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(ShardedJedis jedis) {
                return jedis.lpush(key, values);
            }
        });
    }

    public String rpop(final String key) {
        return execute(new JedisAction<String>() {

            @Override
            public String action(ShardedJedis jedis) {
                return jedis.rpop(key);
            }
        });
    }

    /**
     * 返回List长度, key不存在时返回0，key类型不是list时抛出异常.
     */
    public Long llen(final String key) {
        return execute(new JedisAction<Long>() {

            @Override
            public Long action(ShardedJedis jedis) {
                return jedis.llen(key);
            }
        });
    }

    /**
     * 删除List中的第一个等于value的元素，value不存在或key不存在时返回false.
     */
    public Boolean lremOne(final String key, final String value) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(ShardedJedis jedis) {
                Long count = jedis.lrem(key, 1, value);
                return (count == 1);
            }
        });
    }

    /**
     * 删除List中的所有等于value的元素，value不存在或key不存在时返回false.
     */
    public Boolean lremAll(final String key, final String value) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(ShardedJedis jedis) {
                Long count = jedis.lrem(key, 0, value);
                return (count > 0);
            }
        });
    }

    // ////////////// 关于Sorted Set ///////////////////////////
    /**
     * 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
     */
    public Boolean zadd(final String key, final String member, final double score) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(ShardedJedis jedis) {
                return jedis.zadd(key, score, member) == 1 ? true : false;
            }
        });
    }

    /**
     * 删除sorted set中的元素，成功删除返回true，key或member不存在返回false。
     */
    public Boolean zrem(final String key, final String member) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(ShardedJedis jedis) {
                return jedis.zrem(key, member) == 1 ? true : false;
            }
        });
    }

    /**
     * 当key不存在时返回null.
     */
    public Double zscore(final String key, final String member) {
        return execute(new JedisAction<Double>() {

            @Override
            public Double action(ShardedJedis jedis) {
                return jedis.zscore(key, member);
            }
        });
    }

    /**
     * 返回sorted set长度, key不存在时返回0.
     */
    public Long zcard(final String key) {
        return execute(new JedisAction<Long>() {

            @Override
            public Long action(ShardedJedis jedis) {
                return jedis.zcard(key);
            }
        });
    }


    public Map<String, String> hgetall(final String key) {
        return execute(new JedisAction<Map<String, String>>() {
            @Override
            public Map<String, String> action(ShardedJedis jedis) {
                return jedis.hgetAll(key);
            }
        });
    }

    public List<byte[]> lrange(final byte[] key, final int i, final int j) {
        return execute(new JedisAction<List<byte[]>>() {
            @Override
            public List<byte[]> action(ShardedJedis jedis) {
                return jedis.lrange(key, i, j);
            }
        });
    }

    public String hget(final String key, final String field) {
        return execute(new JedisAction<String>() {
            @Override
            public String action(ShardedJedis jedis) {
                return jedis.hget(key, field);
            }
        });

    }

    public byte[] hget(final byte[] key, final byte[] field) {
        return execute(new JedisAction<byte[]>() {
            @Override
            public byte[] action(ShardedJedis jedis) {
                return jedis.hget(key, field);
            }
        });

    }
}
