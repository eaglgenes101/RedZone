package blocks;

import java.util.List;
import java.util.ListIterator;

import mechanics.PoweredComponent;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.StitchedTexture;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.entities.Entity;
import dangerzone.threads.FastBlockTicker;
import entities.EntityDropper;
import entities.EntityParticleStreamer;

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
 * 
 * Hurt Particle Block. Generates hurt particles in the direction it's faced in
 * (Or at least it should)
 * 
/*/

public class ParticleStreamer extends Block implements PoweredComponent
{

	Texture ttop = null;
	Texture tbottom = null;
	Texture tleft = null;
	Texture tright = null;
	Texture tfront = null;
	Texture tback = null;

	String topname;
	String bottomname;
	String leftname;
	String rightname;
	String frontname;
	String backname;

	StitchedTexture sttop = new StitchedTexture();
	StitchedTexture stbottom = new StitchedTexture();
	StitchedTexture stleft = new StitchedTexture();
	StitchedTexture stright = new StitchedTexture();
	StitchedTexture stfront = new StitchedTexture();
	StitchedTexture stback = new StitchedTexture();

	public ParticleStreamer(String n)
	{
		super(n, "");
		topname = "RedZone_res/res/blocks/dispenser_side.png";
		bottomname = "RedZone_res/res/blocks/dispenser_side.png";
		leftname = "RedZone_res/res/blocks/dispenser_side.png";
		rightname = "RedZone_res/res/blocks/dispenser_side.png";
		frontname = "RedZone_res/res/blocks/particle_streamer_front.png";
		backname = "RedZone_res/res/blocks/dispenser_side.png";

		mindamage = 5;
		maxdamage = 80;
		maxstack = 8;
		isStone = true;
		hasFront = true;
		isSolidForRendering = false;
	}
	
	@Override
	public void tickMe(World w, int dimension, int x, int y, int z)
	{
		FastBlockTicker.addFastTick(dimension, x, y, z);
	}
	
	public void tickMeFast(World w, int dimension, int x, int y, int z)
	{
		((PoweredComponent)this).powerBump(w, dimension, x, y, z);
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
		List<Entity> nearby_list = null;
		EntityParticleStreamer ed = null;

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
					if (e instanceof EntityParticleStreamer)
					{
						if ((int) e.posx == x && (int) e.posy == y && (int) e.posz == z)
						{
							ed = (EntityParticleStreamer) e;
							break;
						}
						ed = null;
					}
				}
			}
		}
		
		if (ed == null)
		{ // where did our entity go???
			if (!w.isServer)
			{
				// System.out.printf("spawning new chest entity\n");
				Entity eb = w.createEntityByName("RedZone:EntityParticleStreamer", d, 
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

	public void onBlockPlaced(World w, int dimension, int x, int y, int z)
	{
		if (!w.isServer)
		{
			// System.out.printf("onBlockPlaced spawning new dispenser entity\n");
			Entity eb = w.createEntityByName("RedZone:EntityParticleStreamer", dimension, (float) (x) + 0.5f,
					(float) (y) + 0.5f, (float) (z) + 0.5f);
			if (eb != null)
			{
				eb.init();
				w.spawnEntityInWorld(eb);
			}
		}
	}

	// side 0 = top
	// side 1 = front
	// side 2 = back
	// side 3 = left
	// side 4 = right
	// side 5 = bottom
	public Texture getTexture(int side)
	{

		if (ttop == null)
		{
			ttop = initBlockTexture(topname);
		}
		if (tbottom == null)
		{
			tbottom = initBlockTexture(bottomname);
		}
		if (tleft == null)
		{
			tleft = initBlockTexture(leftname);
		}
		if (tright == null)
		{
			tright = initBlockTexture(rightname);
		}
		if (tfront == null)
		{
			tfront = initBlockTexture(frontname);
		}
		if (tback == null)
		{
			tback = initBlockTexture(backname);
		}

		if (side == 0)
			return ttop;
		if (side == 5)
			return tbottom;
		if (side == 3)
			return tleft;
		if (side == 4)
			return tright;
		if (side == 1)
			return tfront;
		if (side == 2)
			return tback;
		return null;
	}

	public StitchedTexture getStitchedTexture(int side)
	{
		if (side == 0)
			return sttop;
		if (side == 5)
			return stbottom;
		if (side == 3)
			return stleft;
		if (side == 4)
			return stright;
		if (side == 1)
			return stfront;
		return stback;
	}

	public String getStitchedTextureName(int side)
	{
		if (side == 0)
			return topname;
		if (side == 5)
			return bottomname;
		if (side == 3)
			return leftname;
		if (side == 4)
			return rightname;
		if (side == 1)
			return frontname;
		return backname;
	}
}
