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

public class EntityBlock extends Entity
{
	float distance;
	int meta;

	public EntityBlock(World w, int side, int meta)
	{
		super(w);
		uniquename = "RedZone:EntityBlock";
		width = 1.0f;
		height = 1.0f;
		distance = 0;
		this.takesFallDamage = false;
		switch (side)
		{
			case 0:
				motiony = 1;
				break;
			case 1:
				motionz = 1;
				break;
			case 2:
				motionz = -1;
				break;
			case 3:
				motionx = -1;
				break;
			case 4:
				motionx = 1;
				break;
			case 5:
				motiony = -1;
				break;
			default:
				// Do nothing	
		}
		this.meta = meta;
	}

	public void update(float deltaT)
	{
		if (world.isServer)
		{
			if (Blocks.getMaxStack(getBID()) == 0)
			{
				deadflag = true; //illegal block ID!
			}
			else
			{
				if (distance >= 1.0)
				{
					world.setblockandmeta(dimension, Math.round(posx), Math.round(posy), Math.round(posz), getBID(), meta);
					deadflag = true;
				}
				else
					distance += deltaT;
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

	@Override
	public void doDeathAnimation()
	{
		motiony = motionx = motionz = 0;
	}

}