package entities;

import java.util.Random;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DamageTypes;
import dangerzone.DangerZone;
import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.WorldRenderer;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityLiving;
import dangerzone.items.Item;
import dangerzone.items.ItemSword;
import dangerzone.items.Items;

/*/
 * Copyright 2015 Eugene "eaglgenes101" Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
/*/

public class EntityPushedBlock extends Entity
{
	public EntityPushedBlock(World w)
	{
		super(w);
		uniquename = "RedZone:EntityPushedBlock";
		width = 1.0f;
		height = 1.0f;
		takesFallDamage = false;
		movement_friction = false;
	}

	public void init()
	{
		this.motionx = 0;
		this.motiony = 0;
		this.motionz = 0;
		this.rotation_pitch = 0;
		this.rotation_roll = 0;
		this.rotation_yaw = 0;
		setVarFloat(22, posx);
		setVarFloat(23, posy);
		setVarFloat(24, posz);
		setBID(world.getblock(dimension, (int) posx, (int) posy, (int) posz));
		setIID(world.getblock(dimension, (int) posx, (int) posy, (int) posz));
		if (Blocks.getBlock(getBID()) != null)
			setMaxHealth(Blocks.getBlock(getBID()).maxdamage);
		setHealth(getMaxHealth());
		//TODO insert life code
		world.setblock(dimension, (int) posx, (int) posy, (int) posz, 0);
	}

	public void update(float deltaT)
	{
		if (Blocks.getMaxStack(getBID()) == 0 && world.isServer)
		{
			deadflag = true; //illegal block ID!
		}
		else if (!Blocks.isSolid(this.getBID()) && world.isServer)
		{
			deadflag = true; //Why are we a non-solid block?
		}
		else if (Blocks.isSolid(world.getblock(dimension, (int) posx, (int) posy, (int) posz)) && world.isServer)
		{
			deadflag = true; //We shouldn't exist here
		}
		else
		{
			float combinedDistance = posx - getVarFloat(22) + posy - getVarFloat(23) + posz - getVarFloat(24);
			if (combinedDistance > 1 || combinedDistance < -1 || lifetimeticker > 100)
			{
				posx = (float) ((int) posx + 0.5);
				posy = (float) ((int) posy + 0.5);
				posz = (float) ((int) posz + 0.5);
				motiony = motionx = motionz = 0;
				if (world.isServer)
				{
					world.setblockandmeta(dimension, (int) (posx), (int) (posy), (int) (posz), getBID(), getIID());
					deadflag = true;
				}
			}
		}
		super.update(deltaT);
	}

	public String getHurtSound()
	{
		return Blocks.getBlock(getBID()).getHitSound();
	}

	public String getDeathSound()
	{
		return Blocks.getBlock(getBID()).getBreakSound();
	}

	/*
	 * Entity is BEING attacked by this.
	 */
	public void doAttackFrom(/* entity that hit me */Entity e, /* DamageTypes */int dt, float pain)
	{
		if (!world.isServer)
			return;
		if (!(e instanceof Player))
			return;
		InventoryContainer ic = e.getHotbar(e.gethotbarindex());
		int bid = getBID();
		if (bid > 0)
		{
			if (Blocks.leftClickOnBlock(bid, (Player) e, dimension, (int) posx, (int) posy, (int) posz))
			{
				int dmg = 1;
				if (ic != null && ic.iid != 0)
				{
					if (Blocks.isWood(bid) || Blocks.isLeaves(bid))
					{
						dmg = Items.getWoodStrength(ic.iid);
					}
					if (Blocks.isStone(bid))
					{
						dmg = Items.getStoneStrength(ic.iid);
					}
					if (Blocks.isDirt(bid))
					{
						dmg = Items.getDirtStrength(ic.iid);
					}
					if (ic.getItem() instanceof ItemSword && !Blocks.isLeaves(bid))
					{
						dmg = 0;
					}
					if (ic.getItem() instanceof ItemSword && Blocks.isLeaves(bid))
					{
						dmg = Items.getAttackStrength(ic.iid);
					}
					if (e.getGameMode() != GameModes.SURVIVAL)
					{
						dmg = (int)getMaxHealth()+1;
					}
					ic.getItem().leftClickOnBlock((Player) e, dimension, (int) posx, (int) posy, (int) posz, 0);
				}

				String particlename = Blocks.getParticleName(bid);
				if (particlename == null || particlename.equals(""))
					particlename = "DangerZone:ParticleBreak";
				Utils.spawnParticles(this.world, particlename, 10, this.dimension, posx, posy, posz, true);
				int md = Blocks.getMinDamage(bid);
				if (dmg >= md)
					setHealth(getHealth() - dmg);
			}

		}

		if (getHealth() > 0)
		{
			if (getHurtSound() != null)
			{
				this.world.playSound(getHurtSound(), dimension, posx, posy, posz, 1.0f,
						1.0f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2f);
			}
			DangerZone.server.sendEntityHitToAll(this);
		}
		else
		{
			if (getDeathSound() != null)
			{
				this.world.playSound(getDeathSound(), dimension, posx, posy, posz, 1.0f,
						1.0f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1f);
			}
			Blocks.doBreakBlock(bid, world, dimension, (int)posx, (int)posy, (int)posz);

			this.doDeathDrops();
			this.deadflag = true;
			this.onDeath();

			DangerZone.server.sendEntityDeathToAll(this);
		}
	}

	public Texture getTexture()
	{
		if (texture == null)
		{
			texture = Blocks.getTexture(getBID());
			if (texture == null)
				deadflag = true;
		}
		return texture;
	}

	public boolean isDying()
	{
		return false; //we are already dead.
	}

}