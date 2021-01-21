package com.iodefaction.api.common.redis;

import lombok.Getter;

public class RedisCredentials {
    private @Getter
    final String host;
    private @Getter
    final
    String password;
    private @Getter
    final String clientName;
    private @Getter
    final int port;
    private @Getter
    final int database;

    public RedisCredentials(String host, String password, String clientName, int port, int database) {
        this.host = host;
        this.password = password;
        this.clientName = clientName;
        this.port = port;
        this.database = database;
    }

    public RedisCredentials(String host, String password, int port, int database) {
        this(host, password, "GateAPI", port, database);
    }

    public String toRedisURI() {
        StringBuilder stringBuilder = new StringBuilder("redis://");

        stringBuilder.append(host).append(":").append(port);

        return stringBuilder.toString();
    }
}
