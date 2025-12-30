package Level;

import org.ok23.Nautilus.Player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BossEventPacket;

import java.util.ArrayList;
import java.util.List;

public class Bossbar
{
    private String title;

    private float healthPercent;

    List<Player> boundPlayers = new ArrayList<>();

    private int colour;
    private int overlay = 0;

    private boolean canDarkenScreen = false;

    public Bossbar(String title, float healthPercent, int colour, boolean canDarkenScreen)
    {
        this.title = title;
        this.healthPercent = healthPercent;
        this.colour = colour;
        this.canDarkenScreen = canDarkenScreen;
    }

    // TODO: Implement bossbar creation and sync.

    public void setTitle(String title)
    {
        BossEventPacket packet = new BossEventPacket();
        packet.setAction(BossEventPacket.Action.UPDATE_NAME);
        packet.setTitle("");
        packet.setFilteredTitle(title);

        for(Player p : boundPlayers) p.getSession().sendPacket(packet);
    }
}
