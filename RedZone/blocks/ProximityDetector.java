package blocks;

import java.util.List;
import java.util.ListIterator;

import dangerzone.DangerZone;
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
 * Proximity detectors activate whenever mobs are near them.
 * <p>
 * Proximity detectors are designed for situations where a wide space needs to
 * be monitored for mobs. The signal they emit is inversely proportional to the
 * distance between the mob and the block (up to 16 blocks away; mobs further
 * away can't be detected), and directly proportional to the number of mobs,
 * giving the minimal signal of one for one mob 16 blocks away.
 * 
 * @author eaglgenes101
 * @see PoweredComponent
 */

public class ProximityDetector extends Block implements PoweredComponent
{

	public ProximityDetector(String n, String txt)
	{
		super(n, txt);
		alwaystick = true;
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		return (signalStrength(w, d, x, y, z) >= 16) ? 63 : (int) (signalStrength(w, d, x, y, z) * 4);
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta)
	{
		return true;
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

	protected double signalStrength(World w, int d, int x, int y, int z)
	{
		double rawOutput = 0;
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
					if (e instanceof EntityLiving)
					{
						EntityLiving el = (EntityLiving) e;
						double dx = el.posx - (x + 0.5);
						double dy = el.posy - (y + 0.5);
						double dz = el.posz - (z + 0.5);
						rawOutput += 1 / Math.sqrt(dx * dx + dy * dy + dz * dz);
					}
				}
			}
		}

		return rawOutput;
	}

}
