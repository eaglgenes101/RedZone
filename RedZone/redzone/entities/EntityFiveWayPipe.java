package redzone.entities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import redzone.blocks.RedZoneBlocks;
import redzone.mechanics.Orienter;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityChest;

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
 * Corner pipe entity. 
 * 
/*/

public class EntityFiveWayPipe extends EntityPipe
{


	public EntityFiveWayPipe(World w)
	{
		super(w);
		uniquename = "RedZone:EntityFiveWayPipe";
		inVector = Orienter.EAST_VECTOR;
		outVector = Orienter.UP_VECTOR;
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
		if (myBlockID != RedZoneBlocks.FIVE_WAY_PIPE.blockID)
		{
			this.deadflag = true;
			return;
		}
		super.update(deltaT);
	}
	
	@Override
	public InventoryContainer get(Entity other, int power)
	{
		
		if (power <= 0)
			return new InventoryContainer();
		
		double[] to = Orienter.getDirection(outVector, 
				world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));
		int[] roundedTo = {(int) Math.round(to[0]), (int) Math.round(to[1]),
				(int) Math.round(to[2])};

		int xsep = (int) other.posx - (int) posx;
		int ysep = (int) other.posy - (int) posy;
		int zsep = (int) other.posz - (int) posz;

		int[] sepArray = {xsep, ysep, zsep};

		if (!(Arrays.equals(sepArray, roundedTo)))
			return new InventoryContainer();

		List<Entity> nearby_list = null;
		ArrayList<EntityChest> chests = new ArrayList<>();
		ArrayList<EntityItemSupplier> itemSuppliers = new ArrayList<>();

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
					if (e instanceof EntityChest)
					{
						int xdiff = (int) posx - (int) e.posx;
						int ydiff = (int) posy - (int) e.posy;
						int zdiff = (int) posz - (int) e.posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (!Arrays.equals(checkArray, roundedTo) && Orienter.getSideForm(checkArray) >= 0)
						{
							chests.add((EntityChest) e);
						}
					}
					else if (e instanceof EntityItemSupplier)
					{

						int xdiff = (int) posx - (int) e.posx;
						int ydiff = (int) posy - (int) e.posy;
						int zdiff = (int) posz - (int) e.posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (!Arrays.equals(checkArray, roundedTo) && Orienter.getSideForm(checkArray) >= 0)
						{
							itemSuppliers.add((EntityItemSupplier) e);
						}
					}
				}
			}
		}

		for (EntityChest ec : chests)
		{

			InventoryContainer ic = null;
			int inventoryIndex = -1;

			for (int i = 0; i < ec.inventory.length; i++)
			{
				if (ec.inventory[i] != null && ec.inventory[i].count > 0)
				{
					ic = ec.inventory[i];
					inventoryIndex = i;
					break;
				}
			}

			if (inventoryIndex == -1 || ic == null)
				continue;

			InventoryContainer returnIC = new InventoryContainer(ic.bid, ic.iid, 1, ic.currentuses);

			ic.count--;

			if (ic.count <= 0)
				ic = null;
			cip.inventoryUpdateToServer(ec.entityID, inventoryIndex, ic);

			return returnIC;
		}
		
		for (EntityItemSupplier eis : itemSuppliers)
		{
			if(eis.hasItem(this, power-1))
				return eis.get(this, power-1);
		}
		return new InventoryContainer();
	}

	@Override
	public boolean hasItem(Entity other, int power)
	{
		if (power <= 0)
			return false;
		
		double[] to = Orienter.getDirection(outVector, 
				world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));
		int[] roundedTo = {(int) Math.round(to[0]), (int) Math.round(to[1]),
				(int) Math.round(to[2])};

		int xsep = (int) other.posx - (int) posx;
		int ysep = (int) other.posy - (int) posy;
		int zsep = (int) other.posz - (int) posz;

		int[] sepArray = {xsep, ysep, zsep};

		if (!(Arrays.equals(sepArray, roundedTo)))
			return false;

		List<Entity> nearby_list = null;
		ArrayList<EntityChest> chests = new ArrayList<>();
		ArrayList<EntityItemSupplier> itemSuppliers = new ArrayList<>();

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
					if (e instanceof EntityChest)
					{
						int xdiff = (int) posx - (int) e.posx;
						int ydiff = (int) posy - (int) e.posy;
						int zdiff = (int) posz - (int) e.posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (!Arrays.equals(checkArray, roundedTo) && Orienter.getSideForm(checkArray) >= 0)
						{
							chests.add((EntityChest) e);
						}
					}
					else if (e instanceof EntityItemSupplier)
					{

						int xdiff = (int) posx - (int) e.posx;
						int ydiff = (int) posy - (int) e.posy;
						int zdiff = (int) posz - (int) e.posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (!Arrays.equals(checkArray, roundedTo) && Orienter.getSideForm(checkArray) >= 0)
						{
							itemSuppliers.add((EntityItemSupplier) e);
						}
					}
				}
			}
		}

		for (EntityChest ec : chests)
		{

			InventoryContainer ic = null;
			int inventoryIndex = -1;

			for (int i = 0; i < ec.inventory.length; i++)
			{
				if (ec.inventory[i] != null && ec.inventory[i].count > 0)
				{
					ic = ec.inventory[i];
					inventoryIndex = i;
					break;
				}
			}

			if (inventoryIndex == -1 || ic == null)
				continue;

			return true;
		}
		
		for (EntityItemSupplier eis : itemSuppliers)
		{
			if(eis.hasItem(this, power-1))
				return true;
		}
		return false;
	}

}
