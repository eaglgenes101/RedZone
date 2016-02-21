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
	float origx;
	float origy;
	float origz;

	public EntityBlock(World w)
	{
		super(w);
		uniquename = "RedZone:EntityBlock";
		width = 1.0f;
		height = 1.0f;
		this.takesFallDamage = false;
		canthitme = false;
		ignoreCollisions = false;
	}
	
	public void init()
	{
		int x = Math.round(posx);
		int y = Math.round(posy);
		int z = Math.round(posz);
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
		if (world.isServer)
		{
			if (Blocks.getMaxStack(getBID()) == 0)
			{
				deadflag = true; //illegal block ID!
			}
			else
			{
				float combinedDistance = posx-origx + posy-origy + posz-origz;
				if (combinedDistance > 1 || combinedDistance < -1)
				{
					world.setblockandmeta(dimension, Math.round(posx), Math.round(posy), Math.round(posz), getBID(), getIID());
					System.out.println("Died at combinedDistance" + combinedDistance);
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

	@Override
	public void doDeathAnimation()
	{
		motiony = motionx = motionz = 0;
	}

}