package com.iodefaction.api.common.rabbit.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public interface Packet {
    void read(ByteArrayDataInput input);
    void write(ByteArrayDataOutput output);
    void process();
}
