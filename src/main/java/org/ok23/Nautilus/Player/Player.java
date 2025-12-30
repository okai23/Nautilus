package org.ok23.Nautilus.Player;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.data.BuildPlatform;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.packet.*;

import org.ok23.Nautilus.Entity.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity
{
    private BedrockServerSession session;
    private GameType gameMode;
    private BuildPlatform platform;
    private String displayName;
    private String uuid;
    private int xuid;
    private Skin skin;

    private int maxHunger = 20;
    private int hunger = maxHunger;

    public Player(int entityId, BedrockServerSession session)
    {
        super(EntityType.PLAYER, entityId);

        this.session = session;
    }

    public BedrockServerSession getSession()
    {
        return session;
    }

    public void sendMessage(String message)
    {
        TextPacket packet = new TextPacket();
        packet.setNeedsTranslation(false);
        packet.setXuid("");
        packet.setType(TextPacket.Type.RAW);
        packet.setFilteredMessage(message);
        packet.setMessage("");
        packet.setPlatformChatId("");
        packet.setSourceName("");
        session.sendPacket(packet);
    }

    public void sendTitle(String message, SetTitlePacket.Type type, int fadeIn, int stay, int fadeOut)
    {
        SetTitlePacket packet = new SetTitlePacket();
        packet.setType(type);
        packet.setText(message);
        packet.setFilteredTitleText(message);
        packet.setFadeInTime(fadeIn);
        packet.setStayTime(stay);
        packet.setFadeOutTime(fadeOut);
        packet.setXuid("");
        packet.setPlatformOnlineId("");
        session.sendPacket(packet);
    }

    public void sendTitle(String message, SetTitlePacket.Type type)
    {
        sendTitle(message, type, 10, 70, 20);
    }

    public void clearTitle(SetTitlePacket.Type type)
    {
        sendTitle("", type);
    }

    public void clearAllTitles()
    {
        sendTitle("", SetTitlePacket.Type.TITLE);
        sendTitle("", SetTitlePacket.Type.SUBTITLE);
        sendTitle("", SetTitlePacket.Type.ACTIONBAR);
    }

    // TODO: bossbar wrappers.

    public void sendSound(String sound, Vector3f location, float volume, float pitch)
    {
        PlaySoundPacket packet = new PlaySoundPacket();
        packet.setSound(sound);
        packet.setVolume(volume);
        packet.setPitch(pitch);
        packet.setPosition(location);
        session.sendPacket(packet);
    }

    public void stopSound(String sound, boolean stoppingAllSounds)
    {
        StopSoundPacket packet = new StopSoundPacket();
        packet.setSoundName(sound);
        packet.setStoppingAllSound(false);
        session.sendPacket(packet);
    }

    public void stopSound(String sound)
    {
        stopSound(sound, false);
    }

    public void stopAllSounds()
    {
        stopSound("", true);
    }

    public void setSkin(Skin skin)
    {
        this.skin = skin;
    }

    public Skin getSkin()
    {
        return skin;
    }

    public GameType getGameMode()
    {
        return gameMode;
    }

    public void setGameMode(GameType mode)
    {
        if(mode == gameMode) return;

        SetPlayerGameTypePacket packet = new SetPlayerGameTypePacket();
        packet.setGamemode(mode.ordinal());
        session.sendPacket(packet);
    }

    public BuildPlatform getPlatform()
    {
        return platform;
    }

    public void setPlatform(BuildPlatform platform)
    {
        this.platform = platform;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setXuid(int xuid)
    {
        this.xuid = xuid;
    }

    public int getXuid()
    {
        return xuid;
    }

    // TODO: update health, hunger etc.

    @Override
    public void setMaxHealth(int maxHealth)
    {
        super.setMaxHealth(maxHealth);

        setHealth(health);
    }

    @Override
    public void setHealth(int newHealth)
    {
        super.setHealth(newHealth);

        List<AttributeData> attributeData = new ArrayList<>();
        attributeData.add(new AttributeData("minecraft:health", (float) 0, (float) maxHealth, (float) health));

        UpdateAttributesPacket packet = new UpdateAttributesPacket();
        packet.setAttributes(attributeData);
        packet.setRuntimeEntityId(getEntityId());
        session.sendPacket(packet);
    }

    public void setMaxHunger(int maxHunger)
    {
        this.maxHunger = maxHunger;

        if(hunger > maxHunger) hunger = maxHunger;
    }

    public void setHunger(int newHunger)
    {
        if(newHunger > maxHunger) hunger = maxHunger;
        else if(newHunger < 0) hunger = 0;
        else hunger = newHunger;
    }

    public int getHunger()
    {
        return health;
    }
}
