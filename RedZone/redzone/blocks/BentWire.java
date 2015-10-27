package redzone.blocks;

import dangerzone.Player;
import dangerzone.World;
import redzone.base.RedZoneMain;
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
 * Bent wire block. 
 * Front and top direction determines facing directions. 
 * 
/*/

public class BentWire extends Wire
{

	public BentWire(String n)
	{
		super(n);
		topname = "RedZone_res/blocks/transparent.png";
		bottomname = "RedZone_res/blocks/bent_inactive.png";
		leftname = "RedZone_res/blocks/transparent.png";
		rightname = "RedZone_res/blocks/transparent.png";
		frontname = "RedZone_res/blocks/transparent.png";
		backname = "RedZone_res/blocks/transparent.png";
	}
	
	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta) 
	{
		if (dx > 1 || dx < -1 || dy > 1 || dy < -1 || dz > 1 || dz < -1) return false;
		double[] locvec={1.0,0.0,1.0};
		double[] itsvec = Orienter.getDirection(locvec, meta);
		int[] rounded = { (int)Math.round(itsvec[0]), (int)Math.round(itsvec[1]), (int)Math.round(itsvec[2]) };
		boolean facingSideX = (rounded[0]==dx) && (dx != 0) && (dy == 0 || dz == 0);
		boolean facingSideY = (rounded[1]==dy) && (dy != 0) && (dx == 0 || dz == 0);
		boolean facingSideZ = (rounded[2]==dz) && (dz != 0) && (dy == 0 || dx == 0);
		return facingSideX ^ facingSideY ^ facingSideZ;
	}
	
	public void finishStep(World w, int d, int x, int y, int z)
	{
		int toBlockID = (w.getblockmeta(d, x, y, z)&POWER_MASK)==0 ? RedZoneMain.BENT_WIRE.blockID : RedZoneMain.BENT_WIRE_ACTIVE.blockID;
		w.setblockandmetanonotify(d, x, y, z, toBlockID, w.getblockmeta(d, x, y, z));
		return;
	}
	

	
	public int getItemDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return RedZoneMain.BENT_WIRE_ITEM.itemID;
	}

}
