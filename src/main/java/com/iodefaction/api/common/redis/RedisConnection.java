package com.iodefaction.api.common.redis;

import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

public class RedisConnection {
    private @Getter
    final RedissonClient redissonClient;

    public RedisConnection(RedisCredentials redisCredentials) {
        this.redissonClient = initConnection(redisCredentials);
    }

    public RedissonClient initConnection(RedisCredentials redisCredentials) {
        final Config config = new Config();

        config.setCodec(new JsonJacksonCodec());
        config.setThreads(4);
        config.setNettyThreads(4);
        config.useSingleServer()
                .setAddress(redisCredentials.toRedisURI())
                .setPassword(redisCredentials.getPassword())
                .setDatabase(redisCredentials.getDatabase())
                .setClientName(redisCredentials.getClientName());

        return Redisson.create(config);
    }
}
