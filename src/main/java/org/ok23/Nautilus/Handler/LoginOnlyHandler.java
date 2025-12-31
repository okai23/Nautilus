package org.ok23.Nautilus.Handler;

import org.ok23.Nautilus.Logging.Logger;
import org.ok23.Nautilus.Server.NautilusServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v898.Bedrock_v898;
import org.cloudburstmc.protocol.bedrock.data.*;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.common.PacketSignal;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;

import java.util.ArrayList;
import java.util.UUID;

public final class LoginOnlyHandler implements BedrockPacketHandler
{
    private final BedrockServerSession session;
    private final int entityID = NautilusServer.getInstance().getNextAvailableEntityID();

    public LoginOnlyHandler(BedrockServerSession session)
    {
        this.session = session;
    }

    @Override
    public PacketSignal handlePacket(BedrockPacket packet)
    {
        BedrockPacketHandler.super.handlePacket(packet);
        return PacketSignal.HANDLED;
    }

    @Override
    public PacketSignal handle(RequestNetworkSettingsPacket packet)
    {
        NetworkSettingsPacket response = new NetworkSettingsPacket();
        response.setCompressionThreshold(1);
        response.setCompressionAlgorithm(PacketCompressionAlgorithm.ZLIB);

        session.sendPacketImmediately(response);
        session.setCompression(PacketCompressionAlgorithm.ZLIB);

        return PacketSignal.HANDLED;
    }

    @Override
    public PacketSignal handle(LoginPacket packet)
    {
        BedrockCodec codec = Bedrock_v898.CODEC;
        session.setCodec(codec);

        NautilusServer.getInstance().addPlayerFromLoginPacket(packet, session);

        //encryption

        PlayStatusPacket status = new PlayStatusPacket();
        status.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        session.sendPacketImmediately(status);

        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        resourcePacksInfo.setForcedToAccept(false);
        resourcePacksInfo.setScriptingEnabled(false);
        resourcePacksInfo.setWorldTemplateId(UUID.randomUUID());
        resourcePacksInfo.setWorldTemplateVersion("*");
        resourcePacksInfo.setVibrantVisualsForceDisabled(true);
        resourcePacksInfo.setHasAddonPacks(false);
        session.sendPacketImmediately(resourcePacksInfo);

        return PacketSignal.HANDLED;
    }

    @Override
    public PacketSignal handle(DisconnectPacket packet)
    {
        System.out.println("Client Disconnected: " + packet.getReason());
        return PacketSignal.HANDLED;
    }

    @Override
    public void onDisconnect(CharSequence reason)
    {
        System.out.println("Raknet Disconnection: " + reason.toString());
    }

    public PacketSignal handle(ResourcePackClientResponsePacket packet)
    {
        switch (packet.getStatus())
        {
            case COMPLETED:

                NbtMap airState = NbtMap.builder()
                        .putString("name", "minecraft:air")
                        .putCompound("states", NbtMap.EMPTY)
                        .putInt("version", 17959425)
                        .build();

                StartGamePacket sg = new StartGamePacket();
                sg.setUniqueEntityId(entityID);
                sg.setRuntimeEntityId(entityID);
                sg.setPlayerGameType(GameType.SURVIVAL);
                sg.setPlayerPosition(Vector3f.from(0, 75, 0));
                sg.setDefaultSpawn(Vector3i.from(0, 75, 0));
                sg.setRotation(Vector2f.from(0, 0));
                sg.setWorldTemplateId(UUID.randomUUID());
                sg.setSpawnBiomeType(SpawnBiomeType.DEFAULT);
                sg.setSeed(-1L);
                sg.setDimensionId(0);
                sg.setGeneratorId(1);
                sg.setServerEngine("Nautilus");
                sg.setWorldId("");
                sg.setScenarioId("");
                sg.setOwnerId("");
                sg.setCustomBiomeName("");
                sg.setEducationProductionId("");
                sg.setDefaultPlayerPermission(PlayerPermission.OPERATOR);
                sg.setForceExperimentalGameplay(OptionalBoolean.empty());
                sg.setChatRestrictionLevel(ChatRestrictionLevel.NONE);
                sg.setLevelId("world");
                sg.setLevelName("world");
                sg.setPremiumWorldTemplateId("00000000-0000-0000-0000-000000000000");
                sg.setServerId("");
                sg.setLevelGameType(GameType.SURVIVAL);
                sg.setDifficulty(1);
                sg.setAchievementsDisabled(true);
                sg.setDayCycleStopTime(0);
                sg.setMultiplayerGame(true);
                sg.setBroadcastingToLan(true);
                sg.setCommandsEnabled(true);
                sg.setVanillaVersion("*");
                sg.setServerChunkTickRange(4);
                sg.setPlatformBroadcastMode(GamePublishSetting.PUBLIC);
                sg.setXblBroadcastMode(GamePublishSetting.PUBLIC);
                sg.setAuthoritativeMovementMode(AuthoritativeMovementMode.CLIENT);
                sg.setRewindHistorySize(0);
                sg.setServerAuthoritativeBlockBreaking(false);
                sg.setCurrentTick(0);
                sg.setEnchantmentSeed(0);
                sg.setMultiplayerCorrelationId("");
                sg.setInventoriesServerAuthoritative(false);
                sg.setPlayerPropertyData(NbtMap.EMPTY);
                sg.getBlockProperties().add(new BlockPropertyData("minecraft:air", airState));
                sg.getGamerules().add(new GameRuleData<>("showcoordinates", true));

                sg.setItemDefinitions(new ArrayList<>());
                session.sendPacketImmediately(sg);

                NbtMap plainsDefinition = NbtMap.builder()
                        .putInt("id", 0)
                        .putString("name", "minecraft:plains")
                        .putFloat("downfall", 0.0f)
                        .putFloat("temperature", 0.8f)
                        .build();

                BiomeDefinitionListPacket bdlp = new BiomeDefinitionListPacket();
                bdlp.setDefinitions(NbtMap.builder()
                        .putCompound("minecraft:plains", plainsDefinition)
                        .build());
                session.sendPacket(bdlp);

                try
                {
                    var is = this.getClass().getClassLoader().getResourceAsStream("entity_identifiers.nbt");
                    if (is == null) System.err.println("CRITICAL: entity_identifiers.nbt is MISSING.");

                    NbtMap entities = (NbtMap) NbtUtils.createNetworkReader(is).readTag();
                    AvailableEntityIdentifiersPacket entityIdPacket = new AvailableEntityIdentifiersPacket();
                    entityIdPacket.setIdentifiers(entities);
                    session.sendPacket(entityIdPacket);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                CreativeContentPacket creative = new CreativeContentPacket();
                session.sendPacket(creative);

                ItemComponentPacket itemComponents = new ItemComponentPacket();
                session.sendPacket(itemComponents);
                break;

            case HAVE_ALL_PACKS:
                ResourcePackStackPacket rs = new ResourcePackStackPacket();
                rs.setForcedToAccept(false);
                rs.setGameVersion("*");
                rs.setExperimentsPreviouslyToggled(false);
                session.sendPacketImmediately(rs);
                break;
        }
        return PacketSignal.HANDLED;
    }

    @Override
    public PacketSignal handle(RequestChunkRadiusPacket packet)
    {
        ChunkRadiusUpdatedPacket chunkRadiusUpdatePacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatePacket.setRadius(packet.getRadius());
        session.sendPacketImmediately(chunkRadiusUpdatePacket);

        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
        session.sendPacket(playStatus);

        session.setPacketHandler(new GamePacketRouter(NautilusServer.getInstance().getPlayerFromSession(session)));

        return PacketSignal.HANDLED;
    }


}