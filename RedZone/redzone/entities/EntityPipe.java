package redzone.entities;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import redzone.base.RedZoneMain;
import redzone.blocks.Dispenser;
import redzone.mechanics.Orienter;
import dangerzone.ChestInventoryPacket;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityChest;
import dangerzone.items.Item;
import dangerzone.items.ItemSpawnEgg;
import dangerzone.items.Items;
import dangerzone.threads.FastBlockTicker;

public class EntityPipe extends EntityItemSupplier
{

	ChestInventoryPacket cip = null;

	public EntityPipe(World w)
	{
		super(w);
		uniquename = "RedZone:EntityPipe";
		ignoreCollisions = true;
		width = 0.01f;
		height = 0.01f;
		setVarInt(21, 0);
		if (cip == null)
			cip = new ChestInventoryPacket();
	}

	public InventoryContainer get(Entity other)
	{

		double[] getRelativeForward = Orienter.getDirection(Orienter.UP_VECTOR,
				world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));
		int[] rounded = {(int) Math.round(getRelativeForward[0]), (int) Math.round(getRelativeForward[1]),
				(int) Math.round(getRelativeForward[2])};
		int[] backRounded = {-rounded[0], -rounded[1], -rounded[2]};

		int xsep = (int) posx - (int) other.posx;
		int ysep = (int) posy - (int) other.posy;
		int zsep = (int) posz - (int) other.posz;

		int[] sepArray = {xsep, ysep, zsep};

		if (!(Arrays.equals(sepArray, rounded)))
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
						int xdiff = (int) posx - (int) e.posx;
						int ydiff = (int) posy - (int) e.posy;
						int zdiff = (int) posz - (int) e.posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (Arrays.equals(checkArray, backRounded))
						{
							ec = (EntityChest) e;
							break;
						}
					}
					else if (e instanceof EntityItemSupplier)
					{

						int xdiff = (int) posx - (int) e.posx;
						int ydiff = (int) posy - (int) e.posy;
						int zdiff = (int) posz - (int) e.posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (Arrays.equals(checkArray, backRounded))
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
			return eis.get(this);
		}
		return new InventoryContainer();
	}

	public boolean hasItem(Entity other)
	{

		double[] getRelativeForward = Orienter.getDirection(Orienter.UP_VECTOR,
				world.getblockmeta(dimension, (int) posx, (int) posy, (int) posz));
		int[] rounded = {(int) Math.round(getRelativeForward[0]), (int) Math.round(getRelativeForward[1]),
				(int) Math.round(getRelativeForward[2])};
		int[] backRounded = {-rounded[0], -rounded[1], -rounded[2]};

		int xsep = (int) posx - (int) other.posx;
		int ysep = (int) posy - (int) other.posy;
		int zsep = (int) posz - (int) other.posz;

		int[] sepArray = {xsep, ysep, zsep};
		if (!(Arrays.equals(sepArray, rounded)))
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
						int xdiff = (int) posx - (int) e.posx;
						int ydiff = (int) posy - (int) e.posy;
						int zdiff = (int) posz - (int) e.posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (Arrays.equals(checkArray, backRounded))
						{
							ec = (EntityChest) e;
							break;
						}
					}
					else if (e instanceof EntityItemSupplier)
					{
						int xdiff = (int) posx - (int) e.posx;
						int ydiff = (int) posy - (int) e.posy;
						int zdiff = (int) posz - (int) e.posz;
						int[] checkArray = {xdiff, ydiff, zdiff};
						if (Arrays.equals(checkArray, backRounded))
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
			return eis.hasItem(this);
		}
		return false;
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
		if (myBlockID != RedZoneMain.PIPE.blockID)
		{
			this.deadflag = true;
			return;
		}
		super.update(deltaT);
	}

}