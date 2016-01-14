package blocks;

import mechanics.PoweredComponent;
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
/*/

/**
 * Smoke testers generate smoke particles when supplied with a signal.
 * 
 * Unlike most components, smoke testers cannot themselves connect to other
 * powered components. This makes them useful for testing new components and
 * contraptions without causing side effects, like other components would.
 * 
 * @author eaglgenes101
 * @see PoweredComponent
 */

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
		if (getPowerLevel(w, d, x, y, z) > 0)
			Utils.spawnParticlesFromServer(w, "DangerZone:ParticleFire", 1, d, x + 0.5f, y + 0.5f, z + 0.5f);
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

}
