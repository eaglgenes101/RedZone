package redzone.entities;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import redzone.blocks.Dispenser;
import redzone.blocks.PusherPipe;
import redzone.blocks.RedZoneBlocks;
import redzone.mechanics.Orienter;
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

public class EntityPusherPipe extends EntityPipe
{

	public EntityPusherPipe(World w)
	{
		super(w);
		uniquename = "RedZone:EntityPusherPipe";
		setVarInt(21, 0);
	}
	
	public void update(float deltaT)
	{
		int myBlockID = world.getblock(dimension, (int) posx, (int) posy, (int) posz);
		Block myBlock = Blocks.getBlock(myBlockID);
		
		if (myBlockID != RedZoneBlocks.PUSHER_PIPE.blockID)
		{
			this.deadflag = true;
			return;
		}
		
		else if ( ((PusherPipe) myBlock).getStatus(world, dimension, (int) posx, (int) posy, (int) posz))
		{
			if (FastBlockTicker.cycle % 2 != getVarInt(21))
			{
				double[] getRelativeForward = Orienter.getDirection(Orienter.NORTH_VECTOR, world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));
				int[] rounded = {(int) Math.round(getRelativeForward[0]), (int) Math.round(getRelativeForward[1]), (int) Math.round(getRelativeForward[2])};
				int[] backRounded = {-rounded[0], -rounded[1], -rounded[2]};
				
				EntityChest ec = null;
				
				List<Entity> nearby_list = null;
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
							if (e instanceof EntityChest)
							{
								int xdiff = (int) posx - (int) e.posx;
								int ydiff = (int) posy - (int) e.posy;
								int zdiff = (int) posz - (int) e.posz;
								int[] checkArray = {xdiff, ydiff, zdiff};
								if (Arrays.equals(checkArray, backRounded));
								{
									if (e instanceof EntityChest)
									{
										ec = (EntityChest)e;
										break;
									}
								}
							}
						}
					}
				}
				
				if (ec != null)
				{
					InventoryContainer ic = this.get(ec, 7);
					
					//If we have an item to give
					if (ic.count > 0)
					{
						int toInventoryIndex = -1;
						for (int i = 0; i < ec.inventory.length; i++)
						{
							//Check if filled container
							if (ec.inventory[i] != null && ec.inventory[i].count > 0)
							{
								// Check if same block/item
								if (ec.inventory[i].bid == ic.bid && ec.inventory[i].iid == ic.iid 
										&& ec.inventory[i].currentuses == ic.currentuses)
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
							}
							// If not available, fill an empty container
							else if (ec.inventory[i] == null || ec.inventory[i].count <= 0)
							{
								toInventoryIndex = i;
							}
						}
						
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
