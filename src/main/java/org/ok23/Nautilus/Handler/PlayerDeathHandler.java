package org.ok23.Nautilus.Handler;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.PlayStatusPacket;
import org.cloudburstmc.protocol.bedrock.packet.RespawnPacket;
import org.ok23.Nautilus.Player.Player;

public class PlayerDeathHandler extends GenericHandler
{
    public PlayerDeathHandler(Player player)
    {
        super(player);
    }

    public void handlePlayerDeath(RespawnPacket packet)
    {
        // TODO: implement death flag in player and sync with other clients

        if(packet.getState() == RespawnPacket.State.CLIENT_READY)
        {
            player.resetHealth();
            player.resetHunger();

            RespawnPacket response = new RespawnPacket();
            response.setRuntimeEntityId(player.getEntityId());
            response.setPosition(Vector3f.from(0, 75, 0));
            response.setState(RespawnPacket.State.SERVER_READY);

            PlayStatusPacket status = new PlayStatusPacket();
            status.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
            player.getSession().sendPacket(status);

            player.getSession().sendPacket(response);
        }
    }
}
