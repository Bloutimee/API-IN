package com.iodefaction.api.common.rabbit.consumers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.iodefaction.api.common.rabbit.packets.Packet;
import com.iodefaction.api.common.rabbit.packets.PacketHandler;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.Getter;

import java.io.IOException;

public class PacketConsumer extends DefaultConsumer {

    @Getter
    private final PacketHandler packetHandler;

    public PacketConsumer(Channel channel, PacketHandler packetHandler) {
        super(channel);

        this.packetHandler = packetHandler;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        long deliveryTag = envelope.getDeliveryTag();
        ByteArrayDataInput dataInput = ByteStreams.newDataInput(body);

        int packetId = dataInput.readInt();

        Class<? extends Packet> packetClass = packetHandler.getPacket(packetId);

        getChannel().basicAck(deliveryTag, false);

        if(packetClass == null) {
            System.out.println(packetId + " packet is not recognized!");
            return;
        }

        try {
            Packet packet = packetClass.newInstance();

            packet.read(dataInput);
            packet.process();
        } catch (Exception e) {
            System.out.println("Could not create instance of " + packetClass.getName() + " (" + packetId
                    + ")" + " packet !");
        }
    }
}
