package blocks;

import java.util.List;
import java.util.ListIterator;

import dangerzone.DangerZone;
import dangerzone.Player;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;
import dangerzone.threads.FastBlockTicker;
import mechanics.PoweredComponent;

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
 * Detector switches activate whenever a mob is inside them.
 * 
 * Detector switches are designed for traps. Though wispy and highly visible to
 * trap-builders, they are deceptively durable, and, at night or in enclosed
 * areas, can easily be tripped accidentally by mobs and players that are not
 * aware of them, or are too distracted to notice or avoid them. Detector
 * switches may also be placed out in the open as decoys to direct other mobs
 * and players to more favorable areas.
 * 
 * @author eaglgenes101
 * @see PoweredComponent
 */

public class DetectorSwitch extends Block implements PoweredComponent
{

	public DetectorSwitch(String n, String txt)
	{
		super(n, txt);

		isSolidForRendering = false;
		isSolid = false;
		renderAllSides = true;
		maxdamage = 50;
		showInInventory = true;
		alwaystick = true;
		breaksound = "DangerZone:leavesbreak";
		placesound = "DangerZone:leavesplace";
		hitsound = "DangerZone:leaves_hit";
		renderSmaller = true;
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta)
	{
		return true;
	}

	@Override
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return RedZoneBlocks.DETECTOR_SWITCH.blockID;
	}

	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		if (entityInBlock(w, d, x, y, z))
			return 63;
		return 0;
	}

	@Override
	public void finishStep(World w, int d, int x, int y, int z)
	{
	}

	@Override
	public void tickMe(World w, int d, int x, int y, int z)
	{
		FastBlockTicker.addFastTick(d, x, y, z);
	}

	public void tickMeFast(World w, int d, int x, int y, int z)
	{
		((PoweredComponent) this).powerBump(w, d, x, y, z);
	}

	// The below methods were copied from DangerZone in accordance with the DangerZone license,
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

	protected boolean entityInBlock(World w, int d, int x, int y, int z)
	{
		List<Entity> nearby_list = null;
		ListIterator<Entity> li;

		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		if (w.isServer)
		{
			nearby_list = DangerZone.server.entityManager.findEntitiesInRange(16.0f, d, x, y, z);
		}
		else
		{
			nearby_list = DangerZone.entityManager.findEntitiesInRange(16.0f, d, x, y, z);
		}
		if (nearby_list != null)
		{
			li = nearby_list.listIterator();
			Entity e;
			while (li.hasNext())
			{
				e = (Entity) li.next();
				if (!(e.canthitme) && !e.ignoreCollisions)
				{
					if (x == (int) e.posx && y == (int) e.posy && z == (int) e.posz)
					{
						return true;
					}
					if (e instanceof EntityLiving)
					{
						EntityLiving el = (EntityLiving) e;
						int intheight = (int) (el.height + 0.995f);
						float dx, dz;
						for (int k = 0; k < intheight; k++)
						{
							if ((int) el.posy + k == y)
							{
								dx = el.posx - ((float) x + 0.5f);
								dz = el.posz - ((float) z + 0.5f);
								if (Math.sqrt((dx * dx) + (dz * dz)) < (0.5f + (el.width / 2.0f)))
								{
									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

}
