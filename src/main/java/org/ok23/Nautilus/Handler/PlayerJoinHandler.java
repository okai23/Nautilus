package org.ok23.Nautilus.Handler;

import org.cloudburstmc.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket;
import org.ok23.Nautilus.Logging.Logger;
import org.ok23.Nautilus.Player.Player;

public class PlayerJoinHandler extends GenericHandler
{
    public PlayerJoinHandler(Player player)
    {
        super(player);
    }

    public void handlePlayerJoin(SetLocalPlayerAsInitializedPacket packet)
    {
        Logger.info(player.getDisplayName() + " (ID " + packet.getRuntimeEntityId() + ") joined the game.");

        player.sendMessage("You joined!");
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setMaxHunger(20);
        player.setHunger(20);
        player.setAirSupply(400);
    }
}
