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
package com.voxelplugineering.voxelsniper.forge;

import net.minecraft.server.MinecraftServer;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.forge.entity.ForgePlayer;
import com.voxelplugineering.voxelsniper.forge.service.command.ForgeConsoleProxy;
import com.voxelplugineering.voxelsniper.forge.world.ForgeWorld;
import com.voxelplugineering.voxelsniper.service.Builder;
import com.voxelplugineering.voxelsniper.service.PlayerRegistryService;
import com.voxelplugineering.voxelsniper.service.WorldRegistryService;
import com.voxelplugineering.voxelsniper.service.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.service.registry.RegistryProvider;
import com.voxelplugineering.voxelsniper.service.registry.WorldRegistry;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.Pair;
import com.voxelplugineering.voxelsniper.world.World;

/**
 * Server specific operations.
 */
public class ServerProxy extends CommonProxy
{

    @Override
    public int getOnlinePlayerCount()
    {
        return MinecraftServer.getServer().getCurrentPlayerCount();
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(target = WorldRegistry.class)
    public WorldRegistry<?> getWorldRegistry(Context context)
    {
        return new WorldRegistryService<net.minecraft.world.World>(context, new WorldRegistryProviderServer(context));
    }

    /**
     * Builder
     * 
     * @return The service
     */
    @Builder(target = PlayerRegistry.class)
    public PlayerRegistry<?> getPlayerRegistry(Context context)
    {
        return new PlayerRegistryService<net.minecraft.entity.player.EntityPlayer>(context, new SniperRegistryProviderServer(context),
                new ForgeConsoleProxy());
    }

    /**
     * A world registry provider for forge.
     */
    private class WorldRegistryProviderServer implements RegistryProvider<net.minecraft.world.World, World>
    {

        private final Context context;

        private WorldRegistryProviderServer(Context context)
        {
            this.context = context;
        }

        @Override
        public Optional<Pair<net.minecraft.world.World, World>> get(String name)
        {
            net.minecraft.world.WorldServer w = null;
            for (net.minecraft.world.WorldServer ws : MinecraftServer.getServer().worldServers)
            {
                if (ws.getWorldInfo().getWorldName().equals(name))
                {
                    w = ws;
                    break;
                }
            }
            if (w == null)
            {
                return Optional.absent();
            }
            return Optional.of(new Pair<net.minecraft.world.World, World>(w, new ForgeWorld(this.context, w)));
        }
    }

    /**
     * A player registry provider for forge.
     */
    private class SniperRegistryProviderServer implements RegistryProvider<net.minecraft.entity.player.EntityPlayer, Player>
    {

        private final Context context;

        public SniperRegistryProviderServer(Context context)
        {
            this.context = context;
        }

        @Override
        public Optional<Pair<net.minecraft.entity.player.EntityPlayer, Player>> get(String name)
        {
            net.minecraft.entity.player.EntityPlayer player = null;
            for (Object e : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
            {
                net.minecraft.entity.player.EntityPlayer entity = (net.minecraft.entity.player.EntityPlayer) e;
                if (entity.getName().equals(name))
                {
                    player = entity;
                    break;
                }
            }
            if (player == null)
            {
                return Optional.absent();
            }
            ForgePlayer fp = new ForgePlayer(player, this.context);
            fp.init();
            return Optional.of(new Pair<net.minecraft.entity.player.EntityPlayer, Player>(player, fp));
        }

    }

}
