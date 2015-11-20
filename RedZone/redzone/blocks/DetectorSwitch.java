package redzone.blocks;

import java.util.List;
import java.util.ListIterator;

import redzone.base.RedZoneMain;
import dangerzone.DangerZone;
import dangerzone.Player;
import dangerzone.World;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;
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
 * Detector Switch. 
 * 
/*/

public class DetectorSwitch extends Wire
{

	public DetectorSwitch(String n)
	{
		super(n);

		isStone = true;
		mindamage = 5;
		maxdamage = 20;
		showInInventory = true;

		topname = "RedZone_res/blocks/detector.png";
		bottomname = "RedZone_res/blocks/detector.png";
		leftname = "RedZone_res/blocks/detector.png";
		rightname = "RedZone_res/blocks/detector.png";
		frontname = "RedZone_res/blocks/detector.png";
		backname = "RedZone_res/blocks/detector.png";
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta)
	{
		return true;
	}

	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return RedZoneMain.DETECTOR_SWITCH.blockID;
	}

	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		if (entityInBlock(w, d, x, y, z))
			return 63;
		return 0;
	}
	
	protected boolean entityInBlock(World w, int d, int x, int y, int z){
		List<Entity> nearby_list = null;
		ListIterator<Entity> li;
		
		//Get a list of entities within reach of largest mob expected because we may hit their hitbox!
		if(w.isServer){
			nearby_list = DangerZone.server.entityManager.findEntitiesInRange(16.0f, d, x, y, z);
		}else{
			nearby_list = DangerZone.entityManager.findEntitiesInRange(16.0f, d, x, y, z);
		}
		if(nearby_list != null){
			li = nearby_list.listIterator();
			Entity e;
			while(li.hasNext()){
				e = (Entity)li.next();
				if(!(e.canthitme) && ! e.ignoreCollisions){
					if(x == (int)e.posx && y == (int)e.posy && z == (int)e.posz){
						return true;
					}
					if(e instanceof EntityLiving){
						EntityLiving el = (EntityLiving)e;
						int intheight = (int)(el.height+0.995f);
						float dx, dz;
						for(int k=0;k<intheight;k++){
							if((int)el.posy+k == y){
								dx = el.posx - ((float)x + 0.5f);
								dz = el.posz - ((float)z + 0.5f);	
								if(Math.sqrt((dx*dx)+(dz*dz)) < (0.5f + (el.width/2.0f))){
									return true;
								}	
							}
						}
					}
				}
			}
		}		
		
		return false;		
	}

}
