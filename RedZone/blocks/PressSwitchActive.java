package blocks;

import dangerzone.Player;
import dangerzone.World;
import dangerzone.items.ItemPickAxe;
import dangerzone.items.Items;

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
 * Active form of press switch.  
 * 
/*/

/**
 * Active press switches are the active form of press switches.
 * <p>
 * After being clicked, press switches turn on. To turn them back off, left
 * click them. To turn them on again, left click them again. To break them, use
 * a pickaxe.
 * <p>
 * Active press switches, as technical blocks, are not obtainable in-game
 * through any means.
 * 
 * @author eaglgenes101
 * @see PressSwitch
 */

public class PressSwitchActive extends PressSwitch
{

	public PressSwitchActive(String n)
	{
		super(n);

		topname = "RedZone_res/res/blocks/transparent.png";
		bottomname = "RedZone_res/res/blocks/switch_on.png";
		leftname = "RedZone_res/res/blocks/transparent.png";
		rightname = "RedZone_res/res/blocks/transparent.png";
		frontname = "RedZone_res/res/blocks/transparent.png";
		backname = "RedZone_res/res/blocks/transparent.png";
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		return 63;
	}

	@Override
	public boolean leftClickOnBlock(Player p, int dimension, int x, int y, int z)
	{
		if (Items.getItem(p.getHotbar(p.gethotbarindex()).iid) instanceof ItemPickAxe)
			return true;
		p.world.setblockandmetanonotify(dimension, x, y, z, RedZoneBlocks.PRESS_SWITCH.blockID,
				p.world.getblockmeta(dimension, x, y, z));
		return false;
	}

}
