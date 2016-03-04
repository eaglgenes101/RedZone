package entities;

import java.util.Random;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityLiving;

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
	int callNumber;

	public EntityPushedBlock(World w)
	{
		super(w);
		uniquename = "RedZone:EntityPushedBlock";
		width = 1.0f;
		height = 1.0f;
		takesFallDamage = false;
		movement_friction = false;
		Random r = new Random();
		callNumber = r.nextInt();
	}

	public void init()
	{
		this.motionx = 0;
		this.motiony = 0;
		this.motionz = 0;
		this.rotation_pitch = 0;
		this.rotation_roll = 0;
		this.rotation_yaw = 0;
		setVarFloat(22, posx);
		setVarFloat(23, posy);
		setVarFloat(24, posz);
		world.setblock(dimension, (int)posx, (int)posy, (int)posz, 0);
	}

	public void update(float deltaT)
	{
		if (Blocks.getMaxStack(getBID()) == 0 && world.isServer)
		{
			deadflag = true; //illegal block ID!
		}
		else if (!Blocks.isSolid(this.getBID()) && world.isServer)
		{
			deadflag = true; //Why are we a non-solid block?
		}
		else if (Blocks.isSolid(world.getblock(dimension, (int)posx, (int)posy, (int)posz)) && world.isServer)
		{
			deadflag = true; //We shouldn't exist here
		}
		else
		{
			float combinedDistance = posx - getVarFloat(22) + posy - getVarFloat(23) + posz - getVarFloat(24);
			if (combinedDistance > 1 || combinedDistance < -1 || lifetimeticker > 100)
			{
				if (world.isServer)
				{
					System.out.println(callNumber + " dead at distance " + Math.abs(combinedDistance));
					posx = (float) ((int)posx + 0.5);
					posy = (float) ((int)posy + 0.5);
					posz = (float) ((int)posz + 0.5);
					motiony = motionx = motionz = 0;
					world.setblockandmeta(dimension, (int) (posx), (int) (posy), (int) (posz), getBID(), getIID());
					deadflag = true;
				}
			}
		}
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

}