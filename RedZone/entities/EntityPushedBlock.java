package entities;

import org.newdawn.slick.opengl.Texture;

import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;

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

public class EntityPushedBlock extends Entity
{
	float origx;
	float origy;
	float origz;
	float deathtimer;

	public EntityPushedBlock(World w)
	{
		super(w);
		uniquename = "RedZone:EntityPushedBlock";
		width = 1.0f;
		height = 1.0f;
		this.takesFallDamage = false;
		movement_friction = false;
		deathtimer = 10000;
	}
	
	public void init()
	{
		int x = (int)posx;
		int y = (int)posy;
		int z = (int)posz;
		setBID(world.getblock(dimension, x, y, z));
		setIID(world.getblockmeta(dimension, x, y, z));
		origx = posx;
		origy = posy;
		origz = posz;
		this.rotation_pitch = 0;
		this.rotation_roll = 0;
		this.rotation_yaw = 0;
		
	}

	public void update(float deltaT)
	{
		if (Blocks.getMaxStack(getBID()) == 0)
		{
			deadflag = true; //illegal block ID!
		}
		else
		{
			float combinedDistance = posx-origx + posy-origy + posz-origz;
			if (combinedDistance > 1 || combinedDistance < -1 || deathtimer < 0)
			{
				world.setblockandmeta(dimension, (int)(posx), (int)(posy), (int)(posz), getBID(), getIID());
				deadflag = true;
				System.out.printf("Started at (%f, %f, %f), died at (%f, %f, %f)\n", origx, origy, origz, posx, posy, posz);
			}
		}
		deathtimer -= deltaT;
		super.update(deltaT);
	}

	public Texture getTexture()
	{
		if (texture == null)
		{
			texture = Blocks.getTexture(getBID());
			if (texture == null)
				deadflag = true;
		}
		return texture;
	}

	public boolean isDying()
	{
		return false; //we are already dead.
	}

	@Override
	public void doDeathAnimation()
	{
		motiony = motionx = motionz = 0;
	}

}