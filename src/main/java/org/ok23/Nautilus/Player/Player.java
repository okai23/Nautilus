package org.ok23.Nautilus.Player;

import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.data.BuildPlatform;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.entity.*;
import org.cloudburstmc.protocol.bedrock.packet.*;

import org.ok23.Nautilus.Entity.*;
import org.ok23.Nautilus.Handler.PacketUtils;

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

    private int maxAirSupply = 400;
    private int airSupply = 400;
    private boolean isBreathing = true;

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
        sendTitle(message, type, 0, 70, 20);
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

    public int getMaxHealth()
    {
        return maxHealth;
    }

    public int getMaxHunger()
    {
        return maxHunger;
    }

    public void resetHealth()
    {
        setHealth(maxHealth);
    }

    public void resetHunger()
    {
        setHunger(maxHunger);
    }

    @Override
    public void setHealth(int newHealth)
    {
        super.setHealth(newHealth);

        updateAttribute("minecraft:health", 0, maxHealth, health);
    }

    public void setAirSupply(int airSupply)
    {
        this.airSupply = airSupply;

        if(this.airSupply < 0) this.airSupply = 0;
        if(this.airSupply > maxAirSupply) this.airSupply = maxAirSupply;

        SetEntityDataPacket packet = new SetEntityDataPacket();
        packet.setRuntimeEntityId(entityId);

        packet.getMetadata().put(EntityDataTypes.AIR_SUPPLY, (short) 400);
        packet.getMetadata().put(EntityDataTypes.AIR_SUPPLY_MAX, (short) 400);
        packet.getMetadata().setFlag(EntityFlag.BREATHING, true);

        session.sendPacket(packet);
    }

    public int getAirSupply()
    {
        return airSupply;
    }

    public void setMaxAirSupply(int maxAirSupply)
    {
        this.maxAirSupply = maxAirSupply;

        if(airSupply > maxAirSupply) airSupply = maxAirSupply;
    }

    public int getMaxAirSupply()
    {
        return maxAirSupply;
    }

    public void setIsBreathing(boolean isBreathing)
    {
        this.isBreathing = isBreathing;
    }

    public boolean getIsBreathing()
    {
        return isBreathing;
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

        updateAttribute("minecraft:hunger", 0, maxHunger, hunger);
    }

    public void updateAttribute(String attribute, float min, float max, float value)
    {
        AttributeContainer container = new AttributeContainer();
        container.appendData(attribute, min, max, value);

        UpdateAttributesPacket packet = new UpdateAttributesPacket();
        packet.setAttributes(container.getAttributes());
        packet.setRuntimeEntityId(getEntityId());
        session.sendPacket(packet);
    }

    public int getHunger()
    {
        return hunger;
    }
}
