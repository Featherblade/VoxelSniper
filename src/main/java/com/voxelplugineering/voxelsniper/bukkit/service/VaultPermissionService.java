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
package com.voxelplugineering.voxelsniper.bukkit.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import net.milkbowl.vault.permission.Permission;

import com.voxelplugineering.voxelsniper.bukkit.entity.BukkitPlayer;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.AbstractService;
import com.voxelplugineering.voxelsniper.service.permission.PermissionProxy;
import com.voxelplugineering.voxelsniper.util.Context;

/**
 * A permission proxy for Vault permissions.
 */
public class VaultPermissionService extends AbstractService implements PermissionProxy
{

    /**
     * A reference to Vault's permission service.
     */
    private Permission permissionService = null;

    /**
     * Creates a new {@link VaultPermissionService}.
     */
    public VaultPermissionService(Context context)
    {
        super(context);
    }

    @Override
    protected void _init()
    {
        org.bukkit.plugin.RegisteredServiceProvider<Permission> rsp = org.bukkit.Bukkit.getServicesManager().getRegistration(Permission.class);
        if (rsp != null)
        {
            this.permissionService = rsp.getProvider();
        } else
        {
            throw new RuntimeException("Failed to initialized VaultPermissionProxy service");
        }
    }

    @Override
    protected void _shutdown()
    {
        this.permissionService = null;
    }

    @Override
    public boolean hasPermission(Player sniper, String permission)
    {
        check("hasPermission");
        checkNotNull(sniper, "Sniper cannot be null");
        checkNotNull(permission, "Permission cannot be null!");
        checkArgument(!permission.isEmpty(), "Permission cannot be empty");
        return sniper instanceof BukkitPlayer && this.permissionService.playerHas(((BukkitPlayer) sniper).getThis(), permission);
    }
}
