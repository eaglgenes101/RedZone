package blocks;

import items.RedZoneItems;
import mechanics.PoweredComponent;
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
 * Toggleable press switch.  
 * 
/*/

/**
 * Press switches are components that can be set to supply or not supply a
 * current at the user's control.
 * 
 * Press switches are designed for manual operation. To turn on, left click
 * them. To turn them back off, left click them again. To break them, use a
 * pickaxe on them.
 * 
 * @author eaglgenes101
 * @see PressSwitchActive
 * @see Wire
 */

public class PressSwitch extends Wire
{

	public PressSwitch(String n)
	{
		super(n);

		isStone = true;
		mindamage = 5;
		maxdamage = 20;

		topname = "RedZone_res/res/blocks/transparent.png";
		bottomname = "RedZone_res/res/blocks/switch_off.png";
		leftname = "RedZone_res/res/blocks/transparent.png";
		rightname = "RedZone_res/res/blocks/transparent.png";
		frontname = "RedZone_res/res/blocks/transparent.png";
		backname = "RedZone_res/res/blocks/transparent.png";
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta)
	{
		return true;
	}

	public int getItemDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return RedZoneItems.PRESS_SWITCH_ITEM.itemID;
	}

	@Override
	public boolean leftClickOnBlock(Player p, int dimension, int x, int y, int z)
	{
		if (Items.getItem(p.getHotbar(p.gethotbarindex()).iid) instanceof ItemPickAxe)
			return true;
		p.world.setblockandmetanonotify(dimension, x, y, z, RedZoneBlocks.PRESS_SWITCH_ACTIVE.blockID,
				p.world.getblockmeta(dimension, x, y, z));
		return false;
	}

}
