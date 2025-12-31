package org.ok23.Nautilus.Handler;

import org.cloudburstmc.protocol.bedrock.packet.*;
import org.ok23.Nautilus.Player.Player;

public class PlayerInventoryHandler extends GenericHandler
{
    byte id = 0;

    public PlayerInventoryHandler(Player player)
    {
        super(player);
    }

    public void handleInteract(InteractPacket packet)
    {

    }

    public void handleContainerClose(ContainerClosePacket packet)
    {

    }

}
