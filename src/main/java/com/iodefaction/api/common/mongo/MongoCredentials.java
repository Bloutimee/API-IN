package com.iodefaction.api.common.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MongoCredentials {
    private @Getter
    final String user;
    private @Getter
    final
    String password;
    private @Getter
    final
    String database;
    private @Getter
    final String host;
    private @Getter
    final int port;
}
