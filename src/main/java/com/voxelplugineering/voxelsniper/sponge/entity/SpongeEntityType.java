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

import com.voxelplugineering.voxelsniper.entity.EntityType;

/**
 * A forge entity type.
 */
public class SpongeEntityType implements EntityType
{

    private final Class<? extends org.spongepowered.api.entity.Entity> type;

    /**
     * Creates a new {@link SpongeEntityType}.
     * 
     * @param cls The entity type
     */
    public SpongeEntityType(Class<? extends org.spongepowered.api.entity.Entity> cls)
    {
        this.type = cls;
    }

    @Override
    public String getName()
    {
        return this.type.getName();
    }

    @Override
    public boolean isAlive()
    {
        return org.spongepowered.api.entity.living.Living.class.isAssignableFrom(this.type);
    }

}
