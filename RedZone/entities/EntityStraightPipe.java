package entities;

import mechanics.Orienter;
import blocks.RedZoneBlocks;
import dangerzone.World;

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
 * EntityStraightPipe is an entity that is used internally by StraightPipe to handle
 * block/item movement.
 * <p>
 * To prevent needless memory usage, each block type in DangerZone shares one
 * class. Like other special blocks in RedZone, pipes utilize special internal
 * entities to handle behavior that can't be implemented through the block
 * itself. All pipe entities inherit from one base entity class, and only differ
 * by which directions the pipes will accept as input.
 * 
 * @author eaglgenes101
 * @see EntityPipe
 * @see blocks.Pipe
 * @see mechanics.ItemSupplier
 */

public class EntityStraightPipe extends EntityPipe
{


	public EntityStraightPipe(World w)
	{
		super(w);
		uniquename = "RedZone:EntityStraightPipe";
		inVector = Orienter.DOWN_VECTOR;
		outVector = Orienter.UP_VECTOR;
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
		int myBlockID = world.getblock(dimension, (int) posx, (int) posy, (int) posz);
		if (myBlockID != RedZoneBlocks.STRAIGHT_PIPE.blockID)
		{
			this.deadflag = true;
			return;
		}
		super.update(deltaT);
	}

}
