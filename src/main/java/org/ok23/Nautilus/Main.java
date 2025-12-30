package org.ok23.Nautilus;

import org.ok23.Nautilus.Server.NautilusServer;
import org.cloudburstmc.protocol.bedrock.codec.v898.Bedrock_v898;

public class Main
{
    public static void main(String[] args)
    {
        NautilusServer.createInstance("0.0.0.0", 19132, 10, Bedrock_v898.CODEC);

        NautilusServer.getInstance().setDefaultMotd("motd1", "motd2");

        NautilusServer.getInstance().start();
    }
}
