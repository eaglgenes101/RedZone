package redzone.entities;

import redzone.base.RedZoneMain;
import dangerzone.ChestInventoryPacket;
import dangerzone.DangerZone;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityChest;
import dangerzone.items.Item;

public class EntityDispenser extends EntityChest
{

	public EntityDispenser(World w)
	{
		super(w);
		uniquename = "RedZone:EntityDispenser";
	}

	public InventoryContainer getFirst()
	{
		for (InventoryContainer i : inventory)
		{
			if (i != null)
				return i;
		}
		return null;
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
		if (world.getblock(dimension, (int) posx, (int) posy, (int) posz) != RedZoneMain.DISPENSER.blockID)
		{
			if (world.isServer)
			{
				dumpInventory();
			}
			this.deadflag = true;
			return;
		}
		// if we haven't already, ask the server what our contents are...
		if (!world.isServer)
		{
			if (!contents_requested)
			{
				contents_requested = true;
				ChestInventoryPacket cip = new ChestInventoryPacket();
				cip.requestContents(this.entityID);
			}
		}

		// kick entity out to other players!
		motionx = motiony = motionz = 0;
		super.update(deltaT);
	}
	
	public boolean rightClickedByPlayer(Player p, InventoryContainer ic){
		if(world.getblock(dimension,  (int)posx, (int)posy, (int)posz) != RedZoneMain.DISPENSER.blockID){
			if(world.isServer){
				dumpInventory();
			}
			this.deadflag = true;
			return false;
		}
		Blocks.rightClickOnBlock(RedZoneMain.DISPENSER.blockID, p, dimension, (int)posx, (int)posy, (int)posz);
		return false;
	}

	private void dumpInventory()
	{
		InventoryContainer ic = null;
		for (int i = 0; i < inventory.length; i++)
		{
			ic = inventory[i];
			if (ic != null)
			{
				for (int j = 0; j < ic.count; j++)
				{
					EntityBlockItem e = (EntityBlockItem) world.createEntityByName(DangerZone.blockitemname, dimension,
							posx, posy, posz);
					if (e != null)
					{
						e.setBID(ic.bid);
						e.setIID(ic.iid);
						e.setItemDamage(ic.currentuses);
						e.rotation_pitch = world.rand.nextInt(360);
						e.rotation_yaw = world.rand.nextInt(360);
						e.rotation_roll = world.rand.nextInt(360);
						e.motionx = (world.rand.nextFloat() - world.rand.nextFloat() / 10f);
						e.motiony = world.rand.nextFloat() / 2;
						e.motionz = (world.rand.nextFloat() - world.rand.nextFloat() / 10f);
						world.spawnEntityInWorld(e);
					}
				}
			}
		}
	}

	// Handle right-clicks by a phantom "player"
	public void rightclick(World world, int focus_x, int focus_y, int focus_z, int side, int eid)
	{
		Entity e = null;
		InventoryContainer ic = getFirst();
		boolean rightcontinue = true;
		//We don't have entity focus
		if (world.isServer)
		{
			if (ic != null)
			{
				Item it = ic.getItem();
				if (it != null)
					rightcontinue = it.onRightClick(this, e, ic);
				Block bl = ic.getBlock();
				if (bl != null)
					rightcontinue = bl.onRightClick(this, e, ic);
				if (rightcontinue)
				{
					// System.out.printf("rt click server2 count = %d\n",
					// ic.count);
					ic.count--;
					if (ic.count <= 0)
					{
						ic = null;
					}
				}
			}
		}
	}

}
