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

	public InventoryContainer getFirst()
	{
		for (int i = 0; i < inventory.length; i++)
		{
			if (inventory[i] != null && inventory[i].count > 0)
			{
				return inventory[i];
			}
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

	public EntityDispenser(World w)
	{
		super(w);
		uniquename = "RedZone:EntityDispenser";
		width = 0.001f;
		height = 0.001f;
	}

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

		// This is extremely stupid, but it has to be done...
		// Yes, I copied a bunch of code.

		motionx = motiony = motionz = 0;
		long too_long;
		int different = 0;
		float rate = DangerZone.entityupdaterate;
		rate /= DangerZone.serverentityupdaterate;
		// rate *= 0.75f;

		lifetimeticker++;
		if (deltaT > 1.5f)
			lifetimeticker += (int) ((deltaT + 0.5f) - 1);

		// because we got blown into the low 20,000's one time... oops!
		if (posy > 1024 && motiony > 0)
		{
			posy = 1024;
			motiony = -1;
		}

		if (posy < -1024 && motiony < 0)
		{
			posy = -1024;
			motiony = 1;
		}

		// Standard movement processing
		if (!this.world.isServer)
		{
			if (movement_friction)
			{
				motionx *= (1.0f - (0.35f * deltaT * rate));
				motiony *= (1.0f - (0.05f * deltaT * rate));
				motionz *= (1.0f - (0.35f * deltaT * rate));
				rotation_pitch_motion *= (1.0f - (0.25f * deltaT * rate));
				rotation_yaw_motion *= (1.0f - (0.25f * deltaT * rate));
				rotation_roll_motion *= (1.0f - (0.25f * deltaT * rate));
			}
			posy += motiony * deltaT * rate;
			posx += motionx * deltaT * rate;
			posz += motionz * deltaT * rate;
			rotation_pitch += rotation_pitch_motion * deltaT * rate;
			rotation_yaw += rotation_yaw_motion * deltaT * rate;
			rotation_roll += rotation_roll_motion * deltaT * rate;

			display_posx = posx;
			display_posy = posy;
			display_posz = posz;
			display_rotation_pitch = rotation_pitch;
			display_rotation_yaw = rotation_yaw;
			display_rotation_roll = rotation_roll;

		}
		else
		{
			if (movement_friction)
			{
				motionx *= (1.0f - (0.35f * deltaT));
				motiony *= (1.0f - (0.05f * deltaT));
				motionz *= (1.0f - (0.35f * deltaT));
				rotation_pitch_motion *= (1.0f - (0.25f * deltaT));
				rotation_yaw_motion *= (1.0f - (0.25f * deltaT));
				rotation_roll_motion *= (1.0f - (0.25f * deltaT));
			}
			posy += motiony * deltaT;
			posx += motionx * deltaT;
			posz += motionz * deltaT;
			rotation_pitch += rotation_pitch_motion * deltaT;
			rotation_yaw += rotation_yaw_motion * deltaT;
			rotation_roll += rotation_roll_motion * deltaT;
		}

		while (rotation_yaw < 0)
			rotation_yaw += 360f;
		rotation_yaw %= 360f;
		while (rotation_pitch < 0)
			rotation_pitch += 360f;
		rotation_pitch %= 360f;
		while (rotation_roll < 0)
			rotation_roll += 360f;
		rotation_roll %= 360f;

		while (rotation_yaw_head < 0)
			rotation_yaw_head += 360f;
		rotation_yaw_head %= 360f;
		while (rotation_pitch_head < 0)
			rotation_pitch_head += 360f;
		rotation_pitch_head %= 360f;
		while (rotation_roll_head < 0)
			rotation_roll_head += 360f;
		rotation_roll_head %= 360f;

		// Try to make sure rider and mount are in sync...
		Entity e = getRiddenEntity();
		if (e != null)
		{
			motionx = e.motionx;
			motiony = e.motiony;
			motionz = e.motionz;
			posx = (float) (e.posx + (Math.sin(Math.toRadians(e.rotation_yaw)) * e.getRiderXZoffset()));
			posy = e.posy + e.getRiderYoffset();
			posz = (float) (e.posz + (Math.cos(Math.toRadians(e.rotation_yaw)) * e.getRiderXZoffset()));
			display_posx = posx;
			display_posy = posy;
			display_posz = posz;
		}
		e = getRiderEntity();
		if (e != null)
		{
			e.motionx = motionx;
			e.motiony = motiony;
			e.motionz = motionz;
			e.posx = (float) (posx + (Math.sin(Math.toRadians(rotation_yaw)) * getRiderXZoffset()));
			e.posy = posy + getRiderYoffset();
			e.posz = (float) (posz + (Math.cos(Math.toRadians(rotation_yaw)) * getRiderXZoffset()));
			e.display_posx = e.posx;
			e.display_posy = e.posy;
			e.display_posz = e.posz;
		}
		if (!this.world.isServer)
			return; // Only player on Client sends packets.

		different = 0;
		// See if we should send a position/rotation update to someone...
		currtime = System.currentTimeMillis();
		too_long = currtime - lasttime;
		if (too_long >= 1000)
			different++; // At least every second, regardless.
		// if(too_long >= update_interval){
		if (changed != 0)
			different++;
		if (prevdimension != dimension || prevposx != posx || prevposy != posy || prevposz != posz)
			different++;
		if (prevrotation_yaw != rotation_yaw || prevrotation_pitch != rotation_pitch
				|| prevrotation_roll != rotation_roll)
			different++;
		if (prevrotation_yaw_head != rotation_yaw_head || prevrotation_pitch_head != rotation_pitch_head
				|| prevrotation_roll_head != rotation_roll_head)
			different++;
		if (different == 0)
		{ // keep sending for a few more...
			diffticker++;
			if (diffticker < 10)
			{
				different++;
			}
		}
		else
		{
			diffticker = 0;
		}

		if (different != 0)
		{
			lasttime = currtime;
			prevdimension = dimension;
			prevposy = posy;
			prevposx = posx;
			prevposz = posz;
			prevrotation_yaw = rotation_yaw;
			prevrotation_pitch = rotation_pitch;
			prevrotation_roll = rotation_roll;
			prevrotation_yaw_head = rotation_yaw_head;
			prevrotation_pitch_head = rotation_pitch_head;
			prevrotation_roll_head = rotation_roll_head;

			if (this.world.isServer)
			{
				// System.out.printf("time = %d\n", (int)(currtime%1000));
				DangerZone.server.sendEntityUpdateToAll(this, false);
			}
		}
		// }
	}

	public boolean rightClickedByPlayer(Player p, InventoryContainer ic)
	{
		if (world.getblock(dimension, (int) posx, (int) posy, (int) posz) != RedZoneMain.DISPENSER.blockID)
		{
			if (world.isServer)
			{
				dumpInventory();
			}
			this.deadflag = true;
			return false;
		}
		Blocks.rightClickOnBlock(RedZoneMain.DISPENSER.blockID, p, dimension, (int) posx, (int) posy, (int) posz);
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

	// Do right-clicks by a phantom "player"
	public void rightclick(World world, int focus_x, int focus_y, int focus_z, int side)
	{
		if (world.isServer)
		{
			InventoryContainer ic = getFirst();
			boolean rightcontinue = true;
			if (ic != null)
			{
				Item it = ic.getItem();
				if (it != null)
				{
					rightcontinue = it.onRightClick(this, null, ic);
					System.out.println("Dispensing item...");
				}
				Block bl = ic.getBlock();
				if (bl != null)
				{
					rightcontinue = bl.onRightClick(this, null, ic);
					System.out.println("Dispensing block...");
				}
				if (rightcontinue)
				{
					ic.count--;
					if (ic.count <= 0)
					{
						ic = null;
					}
				}
			}
			else
				System.out.println("Can't find anything to dispense!");
		}
	}

}
