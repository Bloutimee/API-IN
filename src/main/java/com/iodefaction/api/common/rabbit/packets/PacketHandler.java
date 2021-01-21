package com.iodefaction.api.common.rabbit.packets;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public abstract class PacketHandler {
    private final BiMap<Integer, Class<? extends Packet>> packets;

    public PacketHandler() {
        this.packets = HashBiMap.create();

        registerPackets();
    }

    public abstract void registerPackets();

    public Class<? extends Packet> getPacket(int id) {
        return packets.getOrDefault(id, null);
    }

    public void registerPacket(int id, Class<? extends Packet> packetClass) {
        if(this.packets.containsKey(id)) {
            System.out.println("Packet id " + id + " is already taken!");
            return;
        }

        this.packets.put(id, packetClass);
    }

    public int getId(Class<? extends Packet> packetClass) {
        return packets.inverse().getOrDefault(packetClass, -1);
    }
}
