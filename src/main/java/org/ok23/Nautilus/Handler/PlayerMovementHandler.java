package org.ok23.Nautilus.Handler;

import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.ok23.Nautilus.Player.Player;

public class PlayerMovementHandler extends GenericHandler
{
    public PlayerMovementHandler(Player player)
    {
        super(player);
    }

    public void handlePlayerMovement(PlayerAuthInputPacket packet)
    {
        player.setPosition(packet.getPosition());
        player.setRotation(Vector2f.from(packet.getInteractRotation().getX(), packet.getInteractRotation().getY()));
        // TODO: store velocity and jumping etc states
    }
}
