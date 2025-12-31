package org.ok23.Nautilus.Server;

import org.ok23.Nautilus.Handler.*;
import org.ok23.Nautilus.Level.World;
import org.ok23.Nautilus.Logging.Logger;
import org.ok23.Nautilus.Player.Player;
import org.ok23.Nautilus.Player.Skin;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.protocol.bedrock.BedrockPong;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.data.BuildPlatform;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.netty.initializer.BedrockServerInitializer;
import org.cloudburstmc.protocol.bedrock.packet.LoginPacket;
import org.cloudburstmc.protocol.bedrock.util.EncryptionUtils;

import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class NautilusServer
{
    private static NautilusServer instance;

    private NautilusServer(String address, int port, int maxPlayerCount, BedrockCodec serverCodec)
    {
        this.bindAddress = new InetSocketAddress(address, port);
        this.maxPlayerCount = maxPlayerCount;
        this.serverCodec = serverCodec;
    }

    public static void createInstance(String address, int port, int maxPlayerCount, BedrockCodec serverCodec)
    {
        if(instance == null) instance = new NautilusServer(address, port, maxPlayerCount, serverCodec);
    }

    public static NautilusServer getInstance()
    {
        return instance;
    }

    private final InetSocketAddress bindAddress;
    private Channel serverChannel;
    private NioEventLoopGroup group;
    private BedrockPong defaultPong;

    private String motd1 = "My org.ok23.Nautilus.Server";
    private String motd2;

    private int playerCount = 0;
    private final int maxPlayerCount;
    private GameType gameType;
    private PlayerPermission defaultPermission = PlayerPermission.VISITOR;

    private int nextAvailableEntityID = 0;
    private KeyPair serverKeyPair = EncryptionUtils.createKeyPair();

    public KeyPair getServerKeyPair()
    {
        return serverKeyPair;
    }

    private List<Player> players = new ArrayList<>();
    private List<World> worlds = new ArrayList<>();
    private World defaultWorld;

    private final BedrockCodec serverCodec;

    public void setDefaultMotd(String motd1, String motd2)
    {
        this.motd1 = motd1;
        this.motd2 = motd2;

        defaultPong = new BedrockPong()
                .edition("MCPE")
                .motd(motd1)
                .subMotd(motd2)
                .playerCount(playerCount)
                .maximumPlayerCount(maxPlayerCount)
                .gameType("Survival")
                .ipv4Port(bindAddress.getPort())
                .protocolVersion(serverCodec.getProtocolVersion())
                .version(serverCodec.getMinecraftVersion());
    }

    public void start()
    {
        if(worlds.isEmpty()) worlds.add(new World("world", 0));
        defaultWorld = worlds.getFirst();

        group = new NioEventLoopGroup();

        serverChannel = new ServerBootstrap()
                .group(group)
                .channelFactory(RakChannelFactory.server(NioDatagramChannel.class))
                .option(RakChannelOption.RAK_ADVERTISEMENT, defaultPong.toByteBuf())
                .childHandler(new BedrockServerInitializer()
                {
                    @Override
                    protected void initSession(BedrockServerSession session)
                    {
                        Logger.info("New incoming connection from " + session.getSocketAddress().toString());
                        session.setCodec(serverCodec);
                        session.setPacketHandler(new LoginOnlyHandler(session));
                    }
                })
                .bind(bindAddress)
                .syncUninterruptibly()
                .channel();
    }

    public void stop()
    {
        new Thread(() ->
        {
            System.out.println("Stopping server...");
            serverChannel.close().syncUninterruptibly();
            group.shutdownGracefully();
            System.out.println("org.ok23.Nautilus.Server closed.");
        });

        serverChannel.closeFuture().syncUninterruptibly();
        group.shutdownGracefully();
    }

    public void addPlayerFromLoginPacket(LoginPacket packet, BedrockServerSession session)
    {
        Player p = new Player(getNextAvailableEntityID(), session);

        try
        {
            ObjectMapper mapper = new ObjectMapper();

            String clientJwt = packet.getClientJwt();
            String clientPayload = decodeJwtPayload(clientJwt);

            JsonNode clientJson = mapper.readTree(clientPayload);

            p.setDisplayName(clientJson.get("ThirdPartyName").asText());
            p.setPlatform(BuildPlatform.from(clientJson.get("DeviceOS").asInt()));
            p.setUuid(clientJson.get("SelfSignedId").asText());

            Skin skin = new Skin();
            skin.setSkinAnimationData(clientJson.get("SkinAnimationData").asText());
            skin.setSkinColour(clientJson.get("SkinColor").asText());
            skin.setSkinData(clientJson.get("SkinData").asText());
            skin.setSkinGeometryData(clientJson.get("SkinGeometryData").asText());
            skin.setSkinID(clientJson.get("SkinId").asText());
            skin.setSkinImageHeight(clientJson.get("SkinImageHeight").asInt());
            skin.setSkinImageWidth(clientJson.get("SkinImageWidth").asInt());
            skin.setSkinResourcePatch(clientJson.get("SkinResourcePatch").asText());

            p.setSkin(skin);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        players.add(p);
        defaultWorld.addPlayer(p);

        incrementNextAvailableEntityID();
    }

    public Player getPlayerFromSession(BedrockServerSession session)
    {
        for(Player p : players)
        {
            if(p.getSession().equals(session))
            {
                return p;
            }
        }

        return null;
    }

    private String decodeJwtPayload(String jwt)
    {
        String[] parts = jwt.split("\\.");
        if (parts.length < 2) return "{}";

        return new String(Base64.getUrlDecoder().decode(parts[1]));
    }

    public BedrockPong getDefaultPong()
    {
        return defaultPong;
    }

    public int getNextAvailableEntityID()
    {
        return nextAvailableEntityID;
    }

    public void incrementNextAvailableEntityID()
    {
        nextAvailableEntityID++;
    }
}
