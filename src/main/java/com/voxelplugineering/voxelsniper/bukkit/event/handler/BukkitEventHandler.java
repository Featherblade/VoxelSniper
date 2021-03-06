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
package com.voxelplugineering.voxelsniper.bukkit.event.handler;

import org.bukkit.Material;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.event.SnipeEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent.SniperDestroyEvent;
import com.voxelplugineering.voxelsniper.service.config.Configuration;
import com.voxelplugineering.voxelsniper.service.eventbus.EventBus;
import com.voxelplugineering.voxelsniper.service.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.util.Context;

/**
 * An event handler for bukkit's events to post the events to Gunsmith from.
 */
public class BukkitEventHandler implements org.bukkit.event.Listener
{

    private final PlayerRegistry<org.bukkit.entity.Player> pr;
    private final EventBus bus;
    private final org.bukkit.Material arrowMaterial;

    /**
     * Creates a new {@link BukkitEventHandler}.
     */
    @SuppressWarnings({ "unchecked" })
    public BukkitEventHandler(Context context)
    {
        this.pr = context.getRequired(PlayerRegistry.class);
        this.bus = context.getRequired(EventBus.class);
        Configuration conf = context.getRequired(Configuration.class);
        this.arrowMaterial = Material.valueOf(conf.get("arrowMaterial", String.class).or("ARROW"));
    }

    /**
     * An event handler for player join events.
     * 
     * @param event the {@link org.bukkit.event.player.PlayerJoinEvent}
     */
    @org.bukkit.event.EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event)
    {
        Optional<Player> s = this.pr.getPlayer(event.getPlayer().getName());
        if (s.isPresent())
        {
            SniperEvent.SniperCreateEvent sce = new SniperEvent.SniperCreateEvent(s.get());
            this.bus.post(sce);
        }
    }

    /**
     * An event handler for quit events, proxies to Gunsmith's {@link SniperDestroyEvent}.
     * 
     * @param event the event
     */
    @org.bukkit.event.EventHandler
    public void onPlayerLeave(org.bukkit.event.player.PlayerQuitEvent event)
    {
        Optional<Player> s = this.pr.getPlayer(event.getPlayer());
        if (s.isPresent())
        {
            SniperEvent.SniperDestroyEvent sde = new SniperEvent.SniperDestroyEvent(s.get());
            this.bus.post(sde);
        }
    }

    /**
     * An event handler for player interact events.
     * 
     * @param event the {@link org.bukkit.event.player.PlayerInteractEvent}
     */
    @org.bukkit.event.EventHandler
    public void onPlayerInteractEvent(org.bukkit.event.player.PlayerInteractEvent event)
    {
        org.bukkit.entity.Player p = event.getPlayer();
        if (p.getItemInHand().getType() == this.arrowMaterial && (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR))
        {
            Optional<Player> s = this.pr.getPlayer(event.getPlayer());
            if (s.isPresent())
            {
                SnipeEvent se = new SnipeEvent(s.get(), p.getLocation().getYaw(), p.getLocation().getPitch());
                this.bus.post(se);
            }
        }
    }
}
