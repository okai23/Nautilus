package org.ok23.Nautilus.Handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;

public class PacketUtils
{
    public static void debugPacket(BedrockPacket packet, BedrockServerSession session)
    {
        try
        {
            ByteBuf buffer = Unpooled.buffer();
            session.getCodec().tryEncode(session.getCodec().createHelper(), buffer, packet);

            byte[] bytes = new byte[buffer.readableBytes()];
            buffer.readBytes(bytes);

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x ", b));
            }

            System.out.println("DEBUG HEX (" + packet.getPacketType() + "): " + sb.toString());
        }
        catch (Exception e)
        {
            System.err.println("CRITICAL: Packet failed to encode!");
            e.printStackTrace();
        }
    }
}
