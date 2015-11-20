package redzone.blocks;


import java.util.Arrays;

import redzone.base.RedZoneMain;
import redzone.mechanics.Orienter;
import redzone.mechanics.PoweredComponent;
import dangerzone.Player;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.blocks.LightStick;
import dangerzone.threads.FastBlockTicker;

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
 * Power stick. Emits light and sends a signal to its front if powered, otherwise does nothing. 
 * Front direction determines which blocks it considers its front.
 * 
/*/

public class PowerStick extends LightStick implements PoweredComponent
{
	float blockrenderwidth = 16;
	int myrenderid = 0;
	boolean compiled = false;
	
	public PowerStick(String n, String txt) 
	{
		super(n, txt);
		brightness = 0.4f; //Some light
		alwaystick = true;
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z) 
	{
		return 63;
	}
	
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return RedZoneMain.POWER_STICK.blockID;
	}
	
	@Override
	public void tickMe(World w, int d, int x, int y, int z)
	{
		FastBlockTicker.addFastTick(d, x, y, z);
		Utils.spawnParticlesFromServer(w, "DangerZone:ParticleFire", (w.getblockmeta(d, x, y, z)&POWER_MASK)/16, d, x+0.5f, y+0.5f, z+0.5f);
	}
	
	public void finishStep(World w, int d, int x, int y, int z)
	{
		boolean wasPowered = false;
		for (int dx = -1; dx < 2; dx++)
			for (int dy = -1; dy < 2; dy++)
				for (int dz = -1; dz < 2; dz++)
				{
					Block input = Blocks.getBlock(w.getblock(d, x+dx, y+dy, z+dz));
					if (input instanceof PoweredComponent)
						if (((PoweredComponent) input).canConnect(-dx, -dy, -dz, w.getblockmeta(d, x+dx, y+dy, z+dz)))
						{
							if( !( dx==0 && dy==0 && dz==0 ) )
							{
								if (((PoweredComponent)input).getStatus(w, d, x+dx, y+dy, z+dz))
								{
									wasPowered = true;
								}
							}
						}
				}
		int toBlockID = wasPowered ? RedZoneMain.POWER_STICK_INACTIVE.blockID : RedZoneMain.POWER_STICK.blockID;
		w.setblockandmetanonotify(d, x, y, z, toBlockID, w.getblockmeta(d, x, y, z));
		return;
	}
	
	@Override
	public void tickMeFast(World w, int dimension, int x, int y, int z)
	{
		((PoweredComponent)this).powerBump(w, dimension, x, y, z); 
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta) 
	{
		double[] itsvec;
		switch(meta>>8)
		{
			case 0:
				itsvec = Orienter.UP_VECTOR;
				break;
			case 1: 
				itsvec = Orienter.NORTH_VECTOR;
				break;
			case 2: 
				itsvec = Orienter.SOUTH_VECTOR;
				break;
			case 3: 
				itsvec = Orienter.EAST_VECTOR;
				break;
			case 4: 
				itsvec = Orienter.WEST_VECTOR;
				break;
			case 5: 
				itsvec = Orienter.DOWN_VECTOR;
				break;
			default:
				itsvec = Orienter.UP_VECTOR;
				break;
		}
		int[] rounded = { (int)Math.round(itsvec[0]), (int)Math.round(itsvec[1]), (int)Math.round(itsvec[2]) };
		if (Orienter.getSideForm(rounded) < 0)
		{
			System.out.println(Arrays.toString(itsvec));
		}
		boolean facingSideX = (rounded[0]==dx) && (dx != 0) && (dy == 0 || dz == 0);
		boolean facingSideY = (rounded[1]==dy) && (dy != 0) && (dx == 0 || dz == 0);
		boolean facingSideZ = (rounded[2]==dz) && (dz != 0) && (dx == 0 || dy == 0);
		return facingSideX || facingSideY || facingSideZ;
	}
	
}