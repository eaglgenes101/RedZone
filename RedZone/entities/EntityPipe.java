package entities;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import mechanics.Orienter;
import dangerzone.ChestInventoryPacket;
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
 * Base pipe entity. 
 * 
/*/

public abstract class EntityPipe extends EntityItemSupplier
{
	
	protected ChestInventoryPacket cip = null;
	
	protected double[] inVector;
	
	protected double[] outVector;

	public EntityPipe(World w)
	{
		super(w);
		ignoreCollisions = true;
		width = 0.01f;
		height = 0.01f;
		if (cip == null)
			cip = new ChestInventoryPacket();
	}
	
	@Override
	public InventoryContainer get(Entity other, int power)
	{
		
		if (power <= 0)
			return new InventoryContainer();

		double[] from = Orienter.getDirection(inVector,
				world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));
		int[] roundedFrom = {(int) Math.round(from[0]), (int) Math.round(from[1]),
				(int) Math.round(from[2])};
		
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
		EntityChest ec = null;
		EntityItemSupplier eis = null;

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
						int xdiff = (int) e.posx - (int) posx;
						int ydiff = (int) e.posy - (int) posy;
						int zdiff = (int) e.posz - (int) posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (Arrays.equals(checkArray, roundedFrom))
						{
							ec = (EntityChest) e;
							break;
						}
					}
					else if (e instanceof EntityItemSupplier)
					{

						int xdiff = (int) e.posx - (int) posx;
						int ydiff = (int) e.posy - (int) posy;
						int zdiff = (int) e.posz - (int) posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (Arrays.equals(checkArray, roundedFrom))
						{
							eis = (EntityItemSupplier) e;
							break;
						}
					}
				}
			}
		}

		if (ec != null)
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
				return new InventoryContainer();

			InventoryContainer returnIC = new InventoryContainer(ic.bid, ic.iid, 1, ic.currentuses);

			ic.count--;

			if (ic.count <= 0)
				ic = null;
			cip.inventoryUpdateToServer(ec.entityID, inventoryIndex, ic);

			return returnIC;
		}
		else if (eis != null)
		{
			return eis.get(this, power-1);
		}
		return new InventoryContainer();
	}

	@Override
	public boolean hasItem(Entity other, int power)
	{
		if (power <= 0)
			return false;

		double[] from = Orienter.getDirection(inVector,
				world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));
		int[] roundedFrom = {(int) Math.round(from[0]), (int) Math.round(from[1]),
				(int) Math.round(from[2])};
		
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
		EntityChest ec = null;
		EntityItemSupplier eis = null;

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
						int xdiff = (int) e.posx - (int) posx;
						int ydiff = (int) e.posy - (int) posy;
						int zdiff = (int) e.posz - (int) posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (Arrays.equals(checkArray, roundedFrom))
						{
							ec = (EntityChest) e;
							break;
						}
					}
					else if (e instanceof EntityItemSupplier)
					{
						int xdiff = (int) e.posx - (int) posx;
						int ydiff = (int) e.posy - (int) posy;
						int zdiff = (int) e.posz - (int) posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (Arrays.equals(checkArray, roundedFrom))
						{
							eis = (EntityItemSupplier) e;
							break;
						}
					}
				}
			}
		}

		if (ec != null)
		{
			for (int i = 0; i < ec.inventory.length; i++)
			{
				if (ec.inventory[i] != null && ec.inventory[i].count > 0)
				{
					return true;
				}
			}
		}
		if (eis != null)
		{
			return eis.hasItem(this, power-1);
		}
		return false;
	}

}
