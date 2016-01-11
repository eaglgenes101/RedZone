package entities;

import java.util.List;
import java.util.ListIterator;

import mechanics.Orienter;
import blocks.Dropper;
import blocks.RedZoneBlocks;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.ThrownBlockItem;
import dangerzone.threads.FastBlockTicker;

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
 * 
 * 
 * Dropper Entity. 
 * 
/*/

public class EntityDropper extends Entity
{

	public EntityDropper(World w)
	{
		super(w);
		uniquename = "RedZone:EntityDropper";
		ignoreCollisions = true;
		width = 0.01f;
		height = 0.01f;
		setVarInt(21, 0);
	}

	// The below methods were copied from DangerZone in accordance with the
	// DangerZone license,
	// reproduced down below for your convenience. Please do follow it.

	/*
	 * This code is copyright Richard H. Clark, TheyCallMeDanger, OreSpawn,
	 * 2015-2020. You may use this code for reference for modding the DangerZone
	 * game program, and are perfectly welcome to cut'n'paste portions for your
	 * mod as well. DO NOT USE THIS CODE FOR ANY PURPOSE OTHER THAN MODDING FOR
	 * THE DANGERZONE GAME. DO NOT REDISTRIBUTE THIS CODE.
	 * 
	 * This copyright remains in effect until January 1st, 2021. At that time,
	 * this code becomes public domain.
	 * 
	 * WARNING: There are bugs. Big bugs. Little bugs. Every size in-between
	 * bugs. This code is NOT suitable for use in anything other than this
	 * particular game. NO GUARANTEES of any sort are given, either express or
	 * implied, and Richard H. Clark, TheyCallMeDanger, OreSpawn are not
	 * responsible for any damages, direct, indirect, or otherwise. You should
	 * have made backups. It's your own fault for not making them.
	 * 
	 * NO ATTEMPT AT SECURITY IS MADE. This code is USE AT YOUR OWN RISK.
	 * Regardless of what you may think, the reality is, that the moment you
	 * connected your computer to the Internet, Uncle Sam, among many others,
	 * hacked it. DO NOT KEEP VALUABLE INFORMATION ON INTERNET-CONNECTED
	 * COMPUTERS. Or your phone...
	 */

	public void update(float deltaT)
	{
		int myBlockID = world.getblock(dimension, (int) posx, (int) posy, (int) posz);
		Block myBlock = Blocks.getBlock(myBlockID);
		if (myBlockID != RedZoneBlocks.DROPPER.blockID)
		{
			this.deadflag = true;
			return;
		}
		else if (((Dropper) myBlock).getStatus(world, dimension, (int) posx, (int) posy, (int) posz))
		{
			if (FastBlockTicker.cycle % 2 != getVarInt(21))
			{
				double[] getRelativeForward = Orienter.getDirection(Orienter.NORTH_VECTOR, world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));
				int[] rounded = {(int) Math.round(getRelativeForward[0]), (int) Math.round(getRelativeForward[1]), (int) Math.round(getRelativeForward[2])};
				positionSelf(world, dimension, (int) posx, (int) posy, (int) posz);
				rightclick(this.world, Orienter.getSideForm(rounded));
				setVarInt(21, FastBlockTicker.cycle % 2);
			}
		}
		super.update(deltaT);
	}

	private void positionSelf(World w, int d, int x, int y, int z)
	{
		double[] getRelativeForward = Orienter.getDirection(Orienter.NORTH_VECTOR, w.getblockmeta(d, x, y, z));
		int[] rounded = {(int) Math.round(getRelativeForward[0]), (int) Math.round(getRelativeForward[1]), (int) Math.round(getRelativeForward[2])};
		switch (Orienter.getSideForm(rounded))
		{
			case 0: // Top
				rotation_pitch_head = 90;
				break;
			case 1: // Front
				rotation_yaw_head = 0;
				rotation_pitch_head = 0;
				break;
			case 2: // Back
				rotation_yaw_head = 180;
				rotation_pitch_head = 0;
				break;
			case 3: // Left
				rotation_yaw_head = 270;
				rotation_pitch_head = 0;
				break;
			case 4: // Right
				rotation_yaw_head = 90;
				rotation_pitch_head = 0;
				break;
			case 5: // Bottom
				rotation_pitch_head = -90;
				break;
			default: // Dunno
				break;
		}
	}

	// Generate those block/item entities!
	public void rightclick(World world, int side)
	{
		List<Entity> nearby_list = null;
		InventoryContainer ic = null;

		nearby_list = DangerZone.entityManager.findEntitiesInRange(3, dimension, (int) posx, (int) posy, (int) posz);
		if (nearby_list != null)
		{
			if (!nearby_list.isEmpty())
			{
				Entity e = null;
				ListIterator<Entity> li;
				li = nearby_list.listIterator();
				while (li.hasNext())
				{
					e = (Entity) li.next();
					if (e instanceof EntityItemSupplier)
					{
						int xdiff = (int) posx - (int) e.posx;
						int ydiff = (int) posy - (int) e.posy;
						int zdiff = (int) posz - (int) e.posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (Orienter.getSideForm(checkArray) >= 0)
						{
							if (((EntityItemSupplier) e).hasItem(this, 7))
							{
								ic = ((EntityItemSupplier) e).getItem(this, 7);
								break;
							}
						}
					}
				}
			}
		}

		if (ic == null)
		{
			return;
		}

		if (ic.count > 0)
		{
			//Make a throwable item entity!
			if(world.isServer)
			{
				double[] getRelativeForward = Orienter.getDirection(Orienter.NORTH_VECTOR, 
						world.getblockmeta(dimension, (int)posx, (int)posy, (int)posz));
				
				ThrownBlockItem e = (ThrownBlockItem)world.createEntityByName("DangerZone:ThrownBlockItem", 
						dimension, posx+(float)getRelativeForward[0]/2, 
						posy+(float)getRelativeForward[1]/2, posz+(float)getRelativeForward[2]/2);
				if(e != null)
				{
					e.init();
					e.setBID(ic.bid);
					e.setIID(ic.iid);
					e.thrower = this;
					e.setDirectionAndVelocity(
							(float)Math.sin(Math.toRadians(rotation_yaw_head))*(float)Math.cos(Math.toRadians(rotation_pitch_head)), 
							-(float)Math.sin(Math.toRadians(rotation_pitch_head)),
							(float)Math.cos(Math.toRadians(rotation_yaw_head))*(float)Math.cos(Math.toRadians(rotation_pitch_head)),
							1f, 0.01f);
					
					world.spawnEntityInWorld(e);
				}
				world.playSound("DangerZone:bow", dimension, posx, posy, posz, 0.5f, 0.5f+((world.rand.nextFloat()*0.2f
						-world.rand.nextFloat())*0.3f));
			}
		}

	}
}
