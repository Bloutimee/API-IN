package com.iodefaction.api.common.rabbit;

import com.rabbitmq.client.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RabbitCredentials {
    @Getter
    private final String host;
    @Getter
    private final String user;
    @Getter
    private final String password;
    @Getter
    private final String queueName;

    @Getter
    private final int port;

    public ConnectionFactory toConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setPassword(password);
        factory.setUsername(user);

        factory.setVirtualHost("/");

        return factory;
    }
}
