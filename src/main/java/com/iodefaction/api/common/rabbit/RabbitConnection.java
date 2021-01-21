package com.iodefaction.api.common.rabbit;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.iodefaction.api.common.rabbit.consumers.PacketConsumer;
import com.iodefaction.api.common.rabbit.packets.Packet;
import com.iodefaction.api.common.rabbit.packets.PacketHandler;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;

public class RabbitConnection {

    @Getter
    private final RabbitCredentials credentials;

    @Getter
    private final Connection connection;

    @Getter
    private final Channel channel;

    @Getter
    private final PacketHandler packetHandler;

    @SneakyThrows
    public RabbitConnection(RabbitCredentials credentials, PacketHandler packetHandler) {
        this.credentials = credentials;

        this.connection = credentials.toConnectionFactory()
                .newConnection();

        this.channel = this.connection.createChannel();

        this.packetHandler = packetHandler;

        this.channel.queueDeclare(credentials.getQueueName(), true, false, false, null);
        this.channel.exchangeDeclare(credentials.getQueueName(), "fanout", true);

        this.channel.queueBind(credentials.getQueueName(), credentials.getQueueName(), credentials.getQueueName());

        this.channel.basicConsume(credentials.getQueueName(), new PacketConsumer(channel, packetHandler));
    }

    public void send(Packet packet) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();

        Class<? extends Packet> packetClass = packet.getClass();
        int id = packetHandler.getId(packetClass);

        if(id == -1) {
            System.out.println(packetClass.getName() + " is not recognized, is it registered ?");
            return;
        }

        dataOutput.writeInt(id);
        packet.write(dataOutput);

        try {
            this.channel.basicPublish(credentials.getQueueName(), credentials.getQueueName(), MessageProperties.PERSISTENT_TEXT_PLAIN, dataOutput.toByteArray());
        } catch (IOException e) {
            System.out.println(packetClass.getName() + " (" + id + ") packet throw exception at sending ! (" + e.getClass().getSimpleName() + ")");
        }
    }

    @SneakyThrows
    public void close() {
        channel.close();
        connection.close();
    }
}
