package blocks;

import items.RedZoneItems;

import java.util.Arrays;

import mechanics.Orienter;
import dangerzone.Player;
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
 * Straight wire blocks are one of four kinds of wires, which transmit signals
 * without affecting or being affected by them.
 * <p>
 * Like other wire blocks, straight wire blocks are placed by using a wire item
 * on a flat surface, and are positioned according to wire positioning rules.
 * They are attached to a surface, and will drop if the block they are attached
 * to is not solid during a block update.
 * <p>
 * A straight wire's active area is determined by its orientation. At each end
 * of a straight wire, there is a cross-shaped active area centered on each
 * outside face.
 * <p>
 * When powered, straight wire blocks glow just like other kinds of wires.
 * 
 * @author eaglgenes101
 * @see Wire
 * @see StraightWireActive
 */

public class StraightWire extends Wire
{
	public StraightWire(String n)
	{
		super(n);
		topname = "RedZone_res/res/blocks/transparent.png";
		bottomname = "RedZone_res/res/blocks/straight_inactive.png";
		leftname = "RedZone_res/res/blocks/transparent.png";
		rightname = "RedZone_res/res/blocks/transparent.png";
		frontname = "RedZone_res/res/blocks/transparent.png";
		backname = "RedZone_res/res/blocks/transparent.png";
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta)
	{
		if (dx > 1 || dx < -1 || dy > 1 || dy < -1 || dz > 1 || dz < -1)
			return false;
		double[] vec = Orienter.getDirection(Orienter.NORTH_VECTOR, meta);
		int[] rounded = {(int) Math.round(vec[0]), (int) Math.round(vec[1]), (int) Math.round(vec[2])};
		if (Arrays.equals(rounded, Orienter.NORTH_COMPARE) || Arrays.equals(rounded, Orienter.SOUTH_COMPARE))
			return (dz != 0) && (dx == 0 || dy == 0);
		else if (Arrays.equals(rounded, Orienter.EAST_COMPARE) || Arrays.equals(rounded, Orienter.WEST_COMPARE))
			return (dx != 0) && (dy == 0 || dz == 0);
		else if (Arrays.equals(rounded, Orienter.UP_COMPARE) || Arrays.equals(rounded, Orienter.DOWN_COMPARE))
			return (dy != 0) && (dx == 0 || dz == 0);
		else
			return false; //Something went wrong
	}

	public void finishStep(World w, int d, int x, int y, int z)
	{
		int toBlockID = getPowerLevel(w, d, x, y, z) == 0 ? RedZoneBlocks.STRAIGHT_WIRE.blockID
				: RedZoneBlocks.STRAIGHT_WIRE_ACTIVE.blockID;
		w.setblockandmetanonotify(d, x, y, z, toBlockID, w.getblockmeta(d, x, y, z));
		return;
	}

	public int getItemDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return RedZoneItems.STRAIGHT_WIRE_ITEM.itemID;
	}

}
