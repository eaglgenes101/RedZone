package blocks;

import mechanics.Orienter;
import items.RedZoneItems;
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
 * Bent wire blocks are one of four kinds of wires, which transmit signals
 * without affecting or being affected by them.
 * 
 * Like other wire blocks, bent wire blocks are placed by using a wire item on a
 * flat surface, and are positioned according to wire positioning rules. They
 * are attached to a surface, and will drop if the block they are attached to is
 * not solid during a block update. 
 * <p>
 * A bent wire's active area is determined by its orientation. At each end of a
 * bent wire, there is a T-shaped active area centered on the outside face. The
 * two active areas face outwards, and do not overlap. 
 * <p>
 * When powered, bent wire blocks glow just like other kinds of wires. 
 * 
 * @author eaglgenes101
 * @see Wire
 * @see BentWireActive
 */

public class BentWire extends Wire
{

	public BentWire(String n)
	{
		super(n);
		topname = "RedZone_res/res/blocks/transparent.png";
		bottomname = "RedZone_res/res/blocks/bent_inactive.png";
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
		double[] locvec = {-1.0, 0.0, 1.0};
		double[] itsvec = Orienter.getDirection(locvec, meta);
		int[] rounded = {(int) Math.round(itsvec[0]), (int) Math.round(itsvec[1]), (int) Math.round(itsvec[2])};
		boolean facingSideX = (rounded[0] == dx) && (dx != 0) && (dy == 0 || dz == 0);
		boolean facingSideY = (rounded[1] == dy) && (dy != 0) && (dx == 0 || dz == 0);
		boolean facingSideZ = (rounded[2] == dz) && (dz != 0) && (dy == 0 || dx == 0);
		return facingSideX ^ facingSideY ^ facingSideZ;
	}

	public void finishStep(World w, int d, int x, int y, int z)
	{
		int toBlockID = getPowerLevel(w, d, x, y, z) == 0 ? RedZoneBlocks.BENT_WIRE.blockID
				: RedZoneBlocks.BENT_WIRE_ACTIVE.blockID;
		w.setblockandmetanonotify(d, x, y, z, toBlockID, w.getblockmeta(d, x, y, z));
		return;
	}

	public int getItemDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return RedZoneItems.BENT_WIRE_ITEM.itemID;
	}

}
