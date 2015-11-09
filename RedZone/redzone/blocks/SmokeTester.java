package redzone.blocks;

import org.newdawn.slick.opengl.Texture;

import redzone.mechanics.PoweredComponent;
import dangerzone.StitchedTexture;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.blocks.Block;
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
 * Smoke tester. Generates fire particles if powered. 
 * 
/*/

public class SmokeTester extends Block implements PoweredComponent
{
	
	public SmokeTester(String n, String txt)
	{
		super(n, txt);
		alwaystick = true;
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		return 0;
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta)
	{
		return false;
	}
	
	@Override
	public void finishStep(World w, int d, int x, int y, int z)
	{
		if ((w.getblockmeta(d, x, y, z)&POWER_MASK) > 0)
			Utils.spawnParticlesFromServer(w, "DangerZone:ParticleFire", 1, d, x+0.5f, y+0.5f, z+0.5f);
	}
	
	@Override
	public void tickMe(World w, int d, int x, int y, int z)
	{
		FastBlockTicker.addFastTick(d, x, y, z);
	}

}
