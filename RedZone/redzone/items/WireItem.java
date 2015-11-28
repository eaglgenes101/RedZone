package redzone.items;

import dangerzone.Player;
import dangerzone.blocks.Block;
import dangerzone.items.Item;

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
 * Wire item. 
 * 
/*/

public class WireItem extends Item
{
	int blockFormID;
	Block blockForm;
	
	public WireItem(String n, String txt, Block blockit)
	{
		super(n, txt);
		blockForm = blockit;
		blockFormID = blockit.blockID;
	}
	
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z, int side)
	{
		if(p != null && !p.world.isServer)
		{	
			int bid = p.world.getblock(dimension, x, y, z);
			blockForm.doPlaceBlock(bid, p, p.world, dimension, x, y, z, side);
		}
		return true;
	}

}
