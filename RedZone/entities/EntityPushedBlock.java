package entities;

import java.util.List;
import java.util.ListIterator;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.BlockRotation;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;
import dangerzone.items.ItemSword;
import dangerzone.items.Items;
import mechanics.Orienter;

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

/**
 * EntityPushedBlock is an entity that is used to represent pushed blocks.
 * <p>
 * When a solid block is hit by a TractorBeam or a RepulsorBeam, it turns into
 * an EntityPushedBlock. This EntityPushedBlock then can be affected by forces,
 * and will settle back into a block when it is pushed far enough. The block
 * inherits the blockID and itemID of the block it originates from, and will
 * break under the same circumstances the block would, but block-related
 * entities will not recognize the pushed block entity.
 * 
 * @author eaglgenes101
 * @see TractorBeam
 * @see RepulsorBeam
 * @see ModelBlock
 */

public class EntityPushedBlock extends Entity
{
	public EntityPushedBlock(World w)
	{
		super(w);
		uniquename = "RedZone:EntityPushedBlock";
		width = 1.0f;
		height = 1.0f;
		takesFallDamage = false;
		ignoreCollisions = false;
		movement_friction = false;
	}

	public void init()
	{
		this.motionx = 0;
		this.motiony = 0;
		this.motionz = 0;
		setVarInt(22, (int) posx);
		setVarInt(23, (int) posy);
		setVarInt(24, (int) posz);
		setBID(world.getblock(dimension, (int) posx, (int) posy, (int) posz));
		setIID(world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));

		double x_rot_times = (getIID() & BlockRotation.X_MASK) / BlockRotation.X_ROT_90;
		double y_rot_times = (getIID() & BlockRotation.Y_MASK) / BlockRotation.Y_ROT_90;
		double z_rot_times = (getIID() & BlockRotation.Z_MASK) / BlockRotation.Z_ROT_90;

		double x_rot = x_rot_times * Math.PI / 2.0;
		double y_rot = y_rot_times * Math.PI / 2.0;
		double z_rot = z_rot_times * Math.PI / 2.0;

		double[] rotPosition = Orienter.quartToBodyZYX(Orienter.bodyxyzToQuart(x_rot, y_rot, z_rot));

		this.rotation_pitch = (float) (0 * rotPosition[2] / Math.PI * 180.0);
		this.rotation_yaw = 0;
		this.rotation_roll = (float) (0 * rotPosition[0] / Math.PI * 180.0);

		if (Blocks.getBlock(getBID()) != null)
			setMaxHealth(Blocks.getBlock(getBID()).maxdamage);
		setHealth(getMaxHealth());
		world.setblock(dimension, (int) posx, (int) posy, (int) posz, 0);
	}

	@Override
	public void doEntityCollisions(float deltaT)
	{
		float wdth = getWidth();
		List<Entity> nearby_list = null;
		ListIterator<Entity> li;

		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		nearby_list = DangerZone.server.entityManager.findEntitiesInRange((wdth / 2) + 8f, dimension, posx, posy, posz);

		if (nearby_list != null)
		{
			if (!nearby_list.isEmpty())
			{
				double dir;
				double dist;
				Entity e = null;
				li = nearby_list.listIterator();
				while (li.hasNext())
				{
					e = (Entity) li.next();
					if (e != this && !e.ignoreCollisions)
					{ //don't bump self!
						if ((e.posy >= posy && e.posy < posy + getHeight())
								|| (e.posy + e.getHeight() > posy && e.posy + e.getHeight() < posy + getHeight())
								|| (posy >= e.posy && posy < e.posy + e.getHeight())
								|| (posy + getHeight() > e.posy && posy + getHeight() < e.posy + e.getHeight()))
						{
							dist = e.getHorizontalDistanceFromEntity(this); //Center to center
							dist -= (wdth / 2); //width of me
							dist -= (e.getWidth() / 2); //width of it
							if (dist < 0)
							{
								//do x-z bumping...
								dir = Math.atan2(e.posz - this.posz, e.posx - this.posx);
								dist = -0.25f;
								e.motionx -= Math.cos(dir) * dist * deltaT;
								e.motionz -= Math.sin(dir) * dist * deltaT;
							}
						}
					}

					if (e instanceof Player)
						((Player) e).server_thread.sendVelocityUpdateToPlayer(e.motionx, e.motiony, e.motionz);
				}
			}
		}
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
			Blocks.doBreakBlock(this.getBID(), world, dimension, (int) posx, (int) posy, (int) posz);
			this.doDeathDrops();
			this.deadflag = true;
			this.onDeath();
		}
		else
		{
			double combinedDistance = posx - getVarInt(22) + posy - getVarInt(23) + posz - getVarInt(24) - 1.5;
			if (combinedDistance > 1 || combinedDistance < -1 || lifetimeticker > 50)
			{
				posx = getVarInt(22) + Math.signum(Math.round(posx - 0.5 - getVarInt(22))) + 0.5;
				posy = getVarInt(23) + Math.signum(Math.round(posy - 0.5 - getVarInt(23))) + 0.5;
				posz = getVarInt(24) + Math.signum(Math.round(posz - 0.5 - getVarInt(24))) + 0.5;
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
					ic.getItem().leftClickOnBlock((Player) e, dimension, (int) posx, (int) posy, (int) posz, 0);
				}
				if (e.getGameMode() != GameModes.SURVIVAL)
				{
					dmg = (int) getHealth() + 1; //Just shortcut the entire process
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
			Blocks.doBreakBlock(bid, world, dimension, (int) posx, (int) posy, (int) posz);

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