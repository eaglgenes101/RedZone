package redzone.blocks;

import java.util.List;
import java.util.ListIterator;

import dangerzone.DangerZone;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.threads.FastBlockTicker;
import redzone.entities.EntityPusherCornerPipe;
import redzone.entities.EntityStraightPipe;
import redzone.entities.EntityPusherStraightPipe;
import redzone.mechanics.PoweredComponent;

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
 * Pushing corner pipe.   
 * 
/*/

public class PusherCornerPipe extends CornerPipe implements PoweredComponent
{
	
	public PusherCornerPipe(String n)
	{
		super(n);

		topname = "RedZone_res/res/blocks/transparent.png";
		bottomname = "RedZone_res/res/blocks/pusher_pipe_bottom.png";
		leftname = "RedZone_res/res/blocks/pusher_pipe_edge.png";
		rightname = "RedZone_res/res/blocks/transparent.png";
		frontname = "RedZone_res/res/blocks/pusher_pipe_left_edge.png";
		backname = "RedZone_res/res/blocks/pusher_pipe_right_edge.png";
	}

	public void tickMeFast(World w, int d, int x, int y, int z)
	{
		FastBlockTicker.addFastTick(d, x, y, z);
		List<Entity> nearby_list = null;
		EntityPusherCornerPipe ed = null;

		nearby_list = DangerZone.entityManager.findEntitiesInRange(2, d, x, y, z);
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
					if (e instanceof EntityPusherCornerPipe)
					{
						if ((int) e.posx == x && (int) e.posy == y && (int) e.posz == z)
						{
							ed = (EntityPusherCornerPipe) e;
							break;
						}
						ed = null;
					}
				}
			}
		}
		
		if (ed == null)
		{ // where did our entity go???
			if (w.isServer)
			{
				// System.out.printf("spawning new chest entity\n");
				Entity eb = w.createEntityByName("RedZone:EntityPusherCornerPipe", d, 
						(float) (x) + 0.5f,
						(float) (y) + 0.5f, 
						(float) (z) + 0.5f);
				if (eb != null)
				{
					eb.init();
					w.spawnEntityInWorld(eb);
				}
			}
		}
		((PoweredComponent)this).powerBump(w, d, x, y, z); 
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		return 0;
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta)
	{
		return true;
	}

	@Override
	public void finishStep(World w, int d, int x, int y, int z)
	{
		return;
	}
	

	// The below method was copied from DangerZone in accordance with the DangerZone license,
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
	
	public void onBlockPlaced(World w, int dimension, int x, int y, int z)
	{
		if (w.isServer)
		{
			// System.out.printf("onBlockPlaced spawning new dispenser entity\n");
			Entity eb = w.createEntityByName("RedZone:EntityPusherCornerPipe", dimension, (float) (x) + 0.5f,
					(float) (y) + 0.5f, (float) (z) + 0.5f);
			if (eb != null)
			{
				eb.init();
				w.spawnEntityInWorld(eb);
			}
		}
	}

}
