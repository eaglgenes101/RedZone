package blocks;

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
 * Inactive power sticks are the active form of power sticks.
 * <p>
 * A power stick becomes inactive when supplied with at least 1 power. As soon
 * as power is exhausted, a power stick will become active again. Unlike for
 * wires, the active power stick's glow effect supplies illumination.
 * <p>
 * Inactive power sticks, as technical blocks, are not obtainable in-game
 * through any means.
 * 
 * @author eaglgenes101
 * @see BentWire
 */

public class PowerStickInactive extends PowerStick
{
	public PowerStickInactive(String n, String txt)
	{
		super(n, txt);
		brightness = 0.0f; //No light
		showInInventory = false;
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		return 0;
	}

}
