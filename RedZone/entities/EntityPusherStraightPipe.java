package entities;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import mechanics.Orienter;
import blocks.PusherStraightPipe;
import blocks.RedZoneBlocks;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityChest;
import dangerzone.items.Item;
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
/*/

/**
 * EntityPusherStraightPipe is an entity that is used internally by
 * PusherStraightPipe to handle block/item movement.
 * <p>
 * To prevent needless memory usage, each block type in DangerZone shares one
 * class. Like other special blocks in RedZone, pipes utilize special internal
 * entities to handle behavior that can't be implemented through the block
 * itself. All pipe entities inherit from one base entity class, and only differ
 * by which directions the pipes will accept as input.
 * <p>
 * In the case of pusher pipes, however, extra functionality is implemented so
 * that they can push as well as draw items.
 * 
 * @author eaglgenes101
 * @see EntityStraightPipe
 * @see blocks.PusherStraightPipe
 * @see mechanics.ItemSupplier
 */

public class EntityPusherStraightPipe extends EntityStraightPipe
{

	public EntityPusherStraightPipe(World w)
	{
		super(w);
		uniquename = "RedZone:EntityPusherStraightPipe";
		setVarInt(21, 0);
	}
	
	public void update(float deltaT)
	{
		int myBlockID = world.getblock(dimension, (int) posx, (int) posy, (int) posz);
		Block myBlock = Blocks.getBlock(myBlockID);
		
		if (myBlockID != RedZoneBlocks.PUSHER_STRAIGHT_PIPE.blockID)
		{
			this.deadflag = true;
			return;
		}
		
		else if ( ((PusherStraightPipe) myBlock).getStatus(world, dimension, (int) posx, (int) posy, (int) posz))
		{
			if (FastBlockTicker.cycle % 2 != getVarInt(21))
			{
				double[] receiveFrom = Orienter.getDirection(outVector, 
						world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));
				int[] roundedReceiveFrom = {(int) Math.round(receiveFrom[0]), (int) Math.round(receiveFrom[1]),
						(int) Math.round(receiveFrom[2])};
				
				EntityChest ec = null;
				
				List<Entity> nearby_list = null;
				nearby_list = DangerZone.entityManager.findEntitiesInRange(4, dimension, (int) posx, (int) posy, (int) posz);
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
							if (e instanceof EntityChest)
							{
								int xdiff = (int) e.posx - (int) posx;
								int ydiff = (int) e.posy - (int) posy;
								int zdiff = (int) e.posz - (int) posz;
								int[] checkArray = {xdiff, ydiff, zdiff};
								if (Arrays.equals(checkArray, roundedReceiveFrom))
								{
									ec = (EntityChest)e;
									break;
								}
							}
						}
					}
				}
				
				if (ec != null)
				{
					InventoryContainer ic = getItem(ec, 7);
					
					//If we have an item to give
					if (ic.count > 0)
					{
						int toInventoryIndex = -1;
						for (int i = 0; i < ec.inventory.length; i++)
						{
							//Check if filled container
							if (ec.inventory[i] != null && ec.inventory[i].count > 0)
							{
								if (ec.inventory[i].bid == ic.bid && ec.inventory[i].iid == ic.iid 
										&& ec.inventory[i].currentuses >= ic.currentuses)
								{
									if (ic.bid != 0)
									{
										Block corrBlock = Blocks.getBlock(ic.bid);
										if (ec.inventory[i].count < corrBlock.maxstack)
										{
											toInventoryIndex = i;
											break;
										}
									}
									else if (ic.iid != 0)
									{
										Item corrItem = Items.getItem(ic.iid);
										if (ec.inventory[i].count < corrItem.maxstack)
										{
											toInventoryIndex = i;
											break;
										}
									}
								}
								else if (ec.inventory[i].bid == 0 && ec.inventory[i].iid == 0)
								{
									ec.inventory[i] = null;
									toInventoryIndex = i;
								}
							}
							// If not available, fill an empty container
							else if (ec.inventory[i] == null || ec.inventory[i].count <= 0)
							{
								toInventoryIndex = i;
							}
						}
						if (toInventoryIndex >= 0 && ec.inventory[toInventoryIndex] == null)
							ec.inventory[toInventoryIndex] = new InventoryContainer(ic.bid, ic.iid, 0, 0);
						
						//If we have a valid container
						if (toInventoryIndex >= 0)
						{
							ec.inventory[toInventoryIndex].count++;
							cip.inventoryUpdateToServer(ec.entityID, toInventoryIndex, ec.inventory[toInventoryIndex]);
						}
					}
					
				}
				
				setVarInt(21, FastBlockTicker.cycle % 2);
			}
		}
		super.update(deltaT);
	}

}
