package redzone.blocks;

import redzone.base.RedZoneMain;
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
 * Active form press switch.  
 * 
/*/

public class PressSwitchActive extends PressSwitch
{

	public PressSwitchActive(String n)
	{
		super(n);

		topname = "RedZone_res/blocks/transparent.png";
		bottomname = "RedZone_res/blocks/switch_on.png";
		leftname = "RedZone_res/blocks/transparent.png";
		rightname = "RedZone_res/blocks/transparent.png";
		frontname = "RedZone_res/blocks/transparent.png";
		backname = "RedZone_res/blocks/transparent.png";
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		return 63;
	}
	
	@Override
	public boolean leftClickOnBlock(Player p, int dimension, int x, int y, int z)
	{
		if (Items.getItem(p.getHotbar(p.gethotbarindex()).iid) instanceof ItemPickAxe )
			return true;
		p.world.setblockandmetanonotify(dimension, x, y, z,
				RedZoneMain.PRESS_SWITCH.blockID, 
				p.world.getblockmeta(dimension, x, y, z) );
		return false;
	}
	
	

}
