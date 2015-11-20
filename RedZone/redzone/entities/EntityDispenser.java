package redzone.entities;

import java.util.List;
import java.util.ListIterator;

import redzone.base.RedZoneMain;
import redzone.blocks.Dispenser;
import redzone.mechanics.Orienter;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.items.Item;
import dangerzone.items.ItemSpawnEgg;
import dangerzone.items.Items;
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
 * Dispenser Entity. 
 * 
 /*/

public class EntityDispenser extends Entity
{

	public EntityDispenser(World w)
	{
		super(w);
		uniquename = "RedZone:EntityDispenser";
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
		if (myBlockID != RedZoneMain.DISPENSER.blockID)
		{
			this.deadflag = true;
			return;
		}
		else if (((Dispenser) myBlock).getStatus(world, dimension, (int) posx, (int) posy, (int) posz))
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

	// Do right-clicks by a phantom "player"
	public void rightclick(World world, int side)
	{
		double[] getRelativeForward = Orienter.getDirection(Orienter.NORTH_VECTOR, world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));
		int[] rounded = {(int) Math.round(getRelativeForward[0]), (int) Math.round(getRelativeForward[1]), (int) Math.round(getRelativeForward[2])};

		List<Entity> nearby_list = null;
		InventoryContainer ic = null;

		nearby_list = DangerZone.entityManager.findEntitiesInRange(5, dimension, (int) posx, (int) posy, (int) posz);
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
							if (((EntityItemSupplier) e).hasItem(this))
							{
								ic = ((EntityItemSupplier) e).get(this);
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
			Item it = ic.getItem();
			if (it != null)
			{
				it.onRightClick(this, null, ic);
				if (it instanceof ItemSpawnEgg)
				{
					ItemSpawnEgg ise = (ItemSpawnEgg) it;
					Entity e = world.createEntityByName(ise.critter, dimension, 1, 1, 1);
					if (e != null)
					{
						e.init();
						e.posx = posx + rounded[0] * e.width - e.width / 2;
						e.posy = posy + rounded[1] * e.height - e.height / 2;
						e.posz = posz + rounded[2] * e.width - e.width / 2;
						world.spawnEntityInWorld(e);
					}
				}
			}
			Block bl = ic.getBlock();
			if (bl != null)
				bl.onRightClick(this, null, ic);
		}

		int bid = 0;
		int iid = 0;
		if (ic != null)
		{
			if (ic.count >= 1)
			{
				bid = ic.bid;
				iid = ic.iid;
			}
		}

		int fbid = world.getblock(dimension, (int) posx, (int) posy, (int) posz);
		boolean cont = Blocks.rightClickOnBlock(fbid, null, dimension, (int) posx, (int) posy, (int) posz);
		if (cont)
		{
			if (bid != 0)
			{
				if (world.getblock(dimension, (int) posx + rounded[0], (int) posy + rounded[1], (int) posz + rounded[2]) == 0)
				{
					Blocks.doPlaceBlock(bid, fbid, null, world, dimension, (int) posx, (int) posy, (int) posz, side);
				}
			}
			else if (iid != 0)
			{
				Items.rightClickOnBlock(iid, null, dimension, (int) posx, (int) posy, (int) posz, side);
				world.playSound(Blocks.getHitSound(fbid), dimension, (int) posx, (int) posy, (int) posz, 0.35f, 1.0f);
			}
		}

	}
}
