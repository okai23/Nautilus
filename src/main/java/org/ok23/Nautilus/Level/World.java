package org.ok23.Nautilus.Level;

import org.ok23.Nautilus.Player.Player;
import org.ok23.Nautilus.Entity.*;

import java.util.ArrayList;
import java.util.List;

public class World
{
    private List<Player> players = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();

    private String name;
    private int dimension;

    public World(String name, int dimension)
    {
        this.name = name;
        this.dimension = dimension;
    }

    public void addPlayer(Player p)
    {
        if(!players.contains(p)) players.add(p);
    }

    public void removePlayer(Player p)
    {
        if(players.contains(p)) players.remove(p);
    }

    public void addEntity(Entity p)
    {
        if(!entities.contains(p)) entities.add(p);
    }

    public void removeEntity(Entity p)
    {
        if(entities.contains(p)) entities.remove(p);
    }
}
