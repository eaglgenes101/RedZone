package redzone.entities;

import org.lwjgl.input.Mouse;

import redzone.base.RedZoneMain;
import dangerzone.InventoryContainer;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityChest;
import dangerzone.items.Item;
import dangerzone.items.Items;

public class EntityDispenser extends Entity
{
	public boolean shouldOutput = false;
	public int targetX = 0;
	public int targetY = 0;
	public int targetZ = 0;
	public int targetSide = -1;
	public EntityChest ec = null;
	
	public EntityDispenser(World w)
	{
		super(w);
		uniquename = "RedZone:EntityDispenser";
		ignoreCollisions = true;
		width = 0.01f;
		height = 0.01f;
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
		if(world.getblock(dimension, (int)posx, (int)posy, (int)posz) != RedZoneMain.DISPENSER.blockID)
		{
			this.deadflag = true;
			return;
		}
		else if (shouldOutput)
		{
			if (world.isServer)
				rightclick(this.world, targetX, targetY, targetZ, targetSide);
			shouldOutput = false;
		}
		super.update(deltaT);
	}

	// Do right-clicks by a phantom "player"
	public void rightclick(World world, int focus_x, int focus_y, int focus_z, int side)
	{
		boolean rightcontinue = true;
		int inventoryIndex = -1;
		for (int i = 0; i < ec.inventory.length; i++)
		{
			if (ec.getVarInventory(i) != null)
			{
				inventoryIndex = i;
				System.out.println("Found inventory container!");
				break;
			}
		}
		if (inventoryIndex < 0)
			return;
		
		InventoryContainer ic = ec.getVarInventory(inventoryIndex);
		
		if (ic != null)
		{
			Item it = ic.getItem();
			if (it != null)
				rightcontinue = it.onRightClick(this, null, ic);
			Block bl = ic.getBlock();
			if (bl != null)
				rightcontinue = bl.onRightClick(this, null, ic);
			if (rightcontinue)
			{
				System.out.println("Decrementing!");
				ic.count--;
				ec.setVarInventoryChanged(inventoryIndex);
				if (ic.count <= 0)
				{
					ic = null;
				}
			}
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
		
		if (focus_x > 0 && focus_y >= 0 && focus_z > 0)
		{
			int fbid = world.getblock(dimension, focus_x, focus_y, focus_z);
			boolean cont = Blocks.rightClickOnBlock(fbid, null, dimension, focus_x, focus_y, focus_z);
			if (cont)
			{
				if (bid != 0)
				{
					Blocks.doPlaceBlock(bid, fbid, null, world, dimension, focus_x, focus_y, focus_z, side);
				}
				else if (ic != null && iid != 0)
				{
					boolean delme = Items.rightClickOnBlock(iid, null, dimension, focus_x, focus_y, focus_z, side);
					world.playSound(Blocks.getHitSound(fbid), dimension, focus_x, focus_y, focus_z, 0.35f, 1.0f);
					if (delme)
					{
						if (ic.count == 1)
						{
							ic.currentuses++;
							ec.setVarInventoryChanged(inventoryIndex);
						}
						else
						{
							ic.count--;
							ec.setVarInventoryChanged(inventoryIndex);
							if (ic.count <= 0)
								ic = null;
						}
					}
				}
			}
		}
		
	}

}
