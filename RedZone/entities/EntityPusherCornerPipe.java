package entities;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import mechanics.Orienter;
import blocks.PusherCornerPipe;
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
 * 
 * Pushing straight pipe entity. 
 * 
/*/

public class EntityPusherCornerPipe extends EntityCornerPipe
{

	public EntityPusherCornerPipe(World w)
	{
		super(w);
		uniquename = "RedZone:EntityPusherCornerPipe";
		setVarInt(21, 0);
	}
	
	public void update(float deltaT)
	{
		int myBlockID = world.getblock(dimension, (int) posx, (int) posy, (int) posz);
		Block myBlock = Blocks.getBlock(myBlockID);
		
		if (myBlockID != RedZoneBlocks.PUSHER_CORNER_PIPE.blockID)
		{
			this.deadflag = true;
			return;
		}
		
		else if ( ((PusherCornerPipe) myBlock).getStatus(world, dimension, (int) posx, (int) posy, (int) posz))
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
					InventoryContainer ic = get(ec, 7);
					
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
