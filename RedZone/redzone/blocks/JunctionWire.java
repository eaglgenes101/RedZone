package redzone.blocks;

import java.util.Arrays;

import dangerzone.Player;
import dangerzone.World;
import redzone.items.RedZoneItems;
import redzone.mechanics.Orienter;

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
 * Junction wire block. 
 * Top direction determines rotational symmetry axis. 
 * 
/*/

public class JunctionWire extends Wire
{
	

	public JunctionWire(String n)
	{
		super(n);
		topname = "RedZone_res/res/blocks/transparent.png";
		bottomname = "RedZone_res/res/blocks/junction_inactive.png";
		leftname = "RedZone_res/res/blocks/transparent.png";
		rightname = "RedZone_res/res/blocks/transparent.png";
		frontname = "RedZone_res/res/blocks/transparent.png";
		backname = "RedZone_res/res/blocks/transparent.png";
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta) 
	{
		if (dx > 1 || dx < -1 || dy > 1 || dy < -1 || dz > 1 || dz < -1) return false;
		double[] vec = Orienter.getDirection(Orienter.UP_VECTOR, meta);
		int[] rounded = {(int) Math.round(vec[0]), (int) Math.round(vec[1]), (int) Math.round(vec[2])};
		if (Arrays.equals(rounded, Orienter.UP_COMPARE) || Arrays.equals(rounded, Orienter.DOWN_COMPARE))
		{
			return (dx == 0 ^ dz == 0);
		}
		else if (Arrays.equals(rounded, Orienter.NORTH_COMPARE) || Arrays.equals(rounded, Orienter.SOUTH_COMPARE))
		{
			return ( dx == 0 ^ dy == 0 ); 
		}
		else if (Arrays.equals(rounded, Orienter.EAST_COMPARE) || Arrays.equals(rounded, Orienter.WEST_COMPARE))
		{
			return ( dy == 0 ^ dz == 0 );
		}
		else return false; //Something went wrong
	}
	
	
	public void finishStep(World w, int d, int x, int y, int z)
	{
		int toBlockID = (w.getblockmeta(d, x, y, z)&POWER_MASK)==0 ? RedZoneBlocks.JUNCTION_WIRE.blockID : RedZoneBlocks.JUNCTION_WIRE_ACTIVE.blockID;
		w.setblockandmetanonotify(d, x, y, z, toBlockID, w.getblockmeta(d, x, y, z));
		return;
	}
	
	
	public int getItemDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return RedZoneItems.JUNCTION_WIRE_ITEM.itemID;
	}

}
