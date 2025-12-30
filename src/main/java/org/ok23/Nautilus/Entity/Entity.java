package org.ok23.Nautilus.Entity;

import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;

public class Entity
{
    protected EntityType type;

    protected Vector3f position;
    protected Vector2f rotation;

    protected int entityId;

    protected int maxHealth = 20;
    protected int health = maxHealth;

    public Entity(EntityType type, int entityId)
    {
        this.type = type;
        this.entityId = entityId;

        position = Vector3f.from(0, 0, 0);
        rotation = Vector2f.from(0, 0);
    }

    public int getEntityId()
    {
        return entityId;
    }

    public void setMaxHealth(int maxHealth)
    {
        this.maxHealth = maxHealth;

        if(health > maxHealth) health = maxHealth;
    }

    public void setHealth(int newHealth)
    {
        if(newHealth > maxHealth) health = maxHealth;
        else if(newHealth < 0) health = 0;
        else health = newHealth;
    }

    public int getHealth()
    {
        return health;
    }

    // TODO: handle death :(
}
