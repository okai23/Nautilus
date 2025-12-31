package org.ok23.Nautilus.Handler;

import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketType;
import org.cloudburstmc.protocol.common.PacketSignal;
import org.ok23.Nautilus.Logging.Logger;
import org.ok23.Nautilus.Logging.LoggerSeverity;
import org.ok23.Nautilus.Player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GamePacketRouter implements BedrockPacketHandler
{
    private final Player player;

    private final Map<BedrockPacketType, Consumer<BedrockPacket>> listeners = new HashMap<>();

    // Sub-handler Definitions
    private final PlayerJoinHandler playerJoinHandler;
    private final PlayerMovementHandler playerMovementHandler;
    private final PlayerDeathHandler playerDeathHandler;
    private final PlayerInventoryHandler playerInventoryHandler;

    public GamePacketRouter(Player player)
    {
        this.player = player;

        this.playerJoinHandler = new PlayerJoinHandler(player);
        this.playerMovementHandler = new PlayerMovementHandler(player);
        this.playerDeathHandler = new PlayerDeathHandler(player);
        this.playerInventoryHandler = new PlayerInventoryHandler(player);

        register(BedrockPacketType.SET_LOCAL_PLAYER_AS_INITIALIZED, playerJoinHandler::handlePlayerJoin);
        register(BedrockPacketType.PLAYER_AUTH_INPUT, playerMovementHandler::handlePlayerMovement);
        register(BedrockPacketType.RESPAWN, playerDeathHandler::handlePlayerDeath);
        register(BedrockPacketType.INTERACT, playerInventoryHandler::handleInteract);
        register(BedrockPacketType.CONTAINER_CLOSE, playerInventoryHandler::handleContainerClose);
    }

    @SuppressWarnings("unchecked")
    private <T extends BedrockPacket> void register(BedrockPacketType type, Consumer<T> consumer)
    {
        listeners.put(type, (Consumer<BedrockPacket>) consumer);
    }

    @Override
    public PacketSignal handlePacket(BedrockPacket packet)
    {
        Consumer<BedrockPacket> listener = listeners.get(packet.getPacketType());

        if (listener != null)
        {
            listener.accept(packet);

            return PacketSignal.HANDLED;
        }

        Logger.info("Received an unknown packet type: " + packet.getPacketType().toString(), LoggerSeverity.WARNING);

        return PacketSignal.UNHANDLED;
    }

}
