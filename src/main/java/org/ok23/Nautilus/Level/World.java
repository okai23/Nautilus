package org.ok23.Nautilus.Level;

import org.ok23.Nautilus.Player.Player;
import org.ok23.Nautilus.Entity.*;

import java.util.List;

public class World
{
    private List<Player> players;
    private List<Entity> entities;

    private String name = "world";
    private int dimension = 0;

    public World(String name, int dimension)
    {
        this.name = name;
        this.dimension = dimension;
    }
}
