/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 The Voxel Plugineering Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.voxelplugineering.voxelsniper.sponge.entity;

import java.util.UUID;

import org.spongepowered.api.text.Texts;

import com.voxelplugineering.voxelsniper.entity.AbstractPlayer;
import com.voxelplugineering.voxelsniper.entity.EntityType;
import com.voxelplugineering.voxelsniper.service.registry.WorldRegistry;
import com.voxelplugineering.voxelsniper.sponge.util.SpongeUtilities;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.math.Vector3d;
import com.voxelplugineering.voxelsniper.world.CommonLocation;
import com.voxelplugineering.voxelsniper.world.World;

/**
 * Wraps a {@link org.spongepowered.api.entity.player.Player}.
 */
public class SpongePlayer extends AbstractPlayer<org.spongepowered.api.entity.player.Player>
{

    private final WorldRegistry<org.spongepowered.api.world.World> worlds;
    private static final EntityType PLAYER_TYPE = SpongeUtilities.getEntityType(org.spongepowered.api.entity.living.Living.class);

    /**
     * Creates a new {@link SpongePlayer}.
     * 
     * @param player The player to wrap
     */
    @SuppressWarnings("unchecked")
    public SpongePlayer(Context context, org.spongepowered.api.entity.player.Player player)
    {
        super(player, context);
        this.worlds = context.getRequired(WorldRegistry.class);
    }

    @Override
    public boolean isPlayer()
    {
        return true;
    }

    @Override
    public void sendMessage(String msg)
    {
        getThis().sendMessage(Texts.of(msg));
    }

    @Override
    public void sendMessage(String format, Object... args)
    {
        sendMessage(String.format(format, args));
    }

    @Override
    public double getHealth()
    {
        return getThis().getHealthData().getHealth();
    }

    @Override
    public World getWorld()
    {
        return this.worlds.getWorld(getThis().getWorld().getName()).get();
    }

    @Override
    public String getName()
    {
        return getThis().getName();
    }

    @Override
    public EntityType getType()
    {
        return PLAYER_TYPE;
    }

    @Override
    public com.voxelplugineering.voxelsniper.world.Location getLocation()
    {
        com.flowpowered.math.vector.Vector3d position = getThis().getLocation().getPosition();
        return new CommonLocation(getWorld(), position.getX(), position.getY(), position.getZ());
    }

    @Override
    public void setLocation(com.voxelplugineering.voxelsniper.world.Location newLocation)
    {
        getThis().setLocation(SpongeUtilities.getSpongeLocation(newLocation));
    }

    @Override
    public UUID getUniqueId()
    {
        return getThis().getUniqueId();
    }

    @Override
    public void setHealth(double health)
    {
        getThis().getHealthData().setHealth(health);
    }

    @Override
    public double getMaxHealth()
    {
        return getThis().getHealthData().getMaxHealth();
    }

    @Override
    public Vector3d getRotation()
    {
        return SpongeUtilities.getGunsmithVector(getThis().getRotation());
    }

    @Override
    public void setRotation(Vector3d rotation)
    {
        getThis().setRotation(SpongeUtilities.getSpongeVector(rotation));
    }

    @Override
    public boolean remove()
    {
        throw new UnsupportedOperationException("Cannot remove player entities");
    }

}
