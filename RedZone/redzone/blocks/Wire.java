package redzone.blocks;

import org.newdawn.slick.opengl.Texture;

import redzone.mechanics.Orienter;
import redzone.mechanics.PoweredComponent;
import dangerzone.DangerZone;
import dangerzone.GameModes;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.StitchedTexture;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.EntityBlockItem;
import dangerzone.threads.FastBlockTicker;

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
 * Base wire block. 
 * 
/*/

public class Wire extends Block implements PoweredComponent
{
	Texture ttop = null;
	Texture tbottom = null;
	Texture tleft = null;
	Texture tright = null;
	Texture tfront = null;
	Texture tback = null;
	String topname;
	String bottomname;
	String leftname;
	String rightname;
	String frontname;
	String backname;
	StitchedTexture sttop = new StitchedTexture();
	StitchedTexture stbottom = new StitchedTexture();
	StitchedTexture stleft = new StitchedTexture();
	StitchedTexture stright = new StitchedTexture();
	StitchedTexture stfront = new StitchedTexture();
	StitchedTexture stback = new StitchedTexture();
	
	float blockrenderwidth = 16;
	boolean compiled = false;
	int myrenderid = 0;
	
	public Wire(String n)
	{
		super(n, "Wire");
		isSolidForRendering = false;
		isSolid = false;
		maxdamage = 1;
		hasFront = true;
		renderAllSides = true;
		alwaystick = true;
		breaksound = "DangerZone:leavesbreak";
		placesound = "DangerZone:leavesplace";
		hitsound =   "DangerZone:leaves_hit";
		showInInventory = false;
		renderSmaller = true;
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z) 
	{
		return 0; // It's a wire!
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta) 
	{
		return false; // Generic object; what did you expect?
	}
	
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return 0;
	}

	@Override
	public void finishStep(World w, int d, int x, int y, int z)
	{
	}
	
	@Override
	public void tickMe(World w, int dimension, int x, int y, int z)
	{
		FastBlockTicker.addFastTick(dimension, x, y, z);
	}
	
	public void tickMeFast(World w, int dimension, int x, int y, int z)
	{
		((PoweredComponent)this).powerBump(w, dimension, x, y, z);
	}
	
	@Override
	public void onBlockPlaced(World w, int dimension, int x, int y, int z)
	{
		FastBlockTicker.addFastTick(dimension, x, y, z);
	}
	
	@Override
	public void notifyNeighborChanged(World w, int d, int x, int y, int z)
	{
		int bid = 0;
		int meta = w.getblockmeta(d, x, y, z);
		
		// Avoided a giant mess of conditionals by using rotation systems :)
		double[] vec = Orienter.getDirection(Orienter.DOWN_VECTOR, meta);
		int[] thisway = {(int) Math.round(vec[0]), (int) Math.round(vec[1]), (int) Math.round(vec[2])};
		bid = w.getblock(d, x+thisway[0], y+thisway[1], z+thisway[2]);
		
		if(!Blocks.isSolid(bid))
		{
			w.setblock(d, x, y, z, 0);
			EntityBlockItem e = (EntityBlockItem)w.createEntityByName(DangerZone.blockitemname, d, x+0.5f, y+0.5f, z+0.5f);
			if(e != null)
			{
				e.setBID(this.blockID);
				e.setIID(0);
				DangerZone.player.world.spawnEntityInWorld(e);
			}			
		}
	}
	
	//client-side only
	public void doPlaceBlock(int focusbid, Player p, World w, int dimension, int x, int y, int z, int side)
	{
		if(!Blocks.isSolid(focusbid))return;
		if(Blocks.isLeaves(focusbid))return;
		
		int meta = 0;
		float dx = 1.0f/3.0f;
		float dy = 2.0f/3.0f;
		float dz = 1;
		
		if(p != null)
		{
			dx = (x+0.5f) - p.posx;
			dy = (y+0.5f) - p.posy;
			dz = (z+0.5f) - p.posz;
		}
		
		switch(side)
		{
			case 0:
				if(Math.abs(dx)>Math.abs(dz))
				{
					if(dx > 0) meta = Orienter.metadataTable[Orienter.UP][Orienter.WEST]; //fine
					else meta = Orienter.metadataTable[Orienter.UP][Orienter.EAST];	//fine
				}
				else
				{
					if(dz > 0) meta = Orienter.metadataTable[Orienter.UP][Orienter.NORTH];	//fine
					else meta = Orienter.metadataTable[Orienter.UP][Orienter.SOUTH]; //fine
				}
				w.setblockandmeta(dimension, x, y+1, z, this.blockID, meta);
				break; 
			case 1:
				if(Math.abs(dx)>Math.abs(dy))
				{
					if(dx > 0) meta = Orienter.metadataTable[Orienter.NORTH][Orienter.WEST]; //wrong
					else meta = Orienter.metadataTable[Orienter.NORTH][Orienter.EAST]; //wrong
				}
				else
				{
					if(dy > 0) meta = Orienter.metadataTable[Orienter.NORTH][Orienter.UP]; //fine 
					else meta = Orienter.metadataTable[Orienter.NORTH][Orienter.DOWN]; //wrong
				}
				w.setblockandmeta(dimension, x, y, z+1, this.blockID, meta);
				break;
			case 2:
				if(Math.abs(dx)>Math.abs(dy))
				{
					if(dx > 0) meta = Orienter.metadataTable[Orienter.SOUTH][Orienter.WEST]; //fine
					else meta = Orienter.metadataTable[Orienter.SOUTH][Orienter.EAST];		//wrong
				}
				else
				{
					if(dy > 0) meta = Orienter.metadataTable[Orienter.SOUTH][Orienter.UP]; //wrong
					else meta = Orienter.metadataTable[Orienter.SOUTH][Orienter.DOWN]; //wrong
				}
				w.setblockandmeta(dimension, x, y, z-1, this.blockID, meta);
				break;
			case 3:
				if(Math.abs(dy)>Math.abs(dz))
				{
					if(dy > 0) meta = Orienter.metadataTable[Orienter.WEST][Orienter.UP]; //wrong
					else meta = Orienter.metadataTable[Orienter.WEST][Orienter.DOWN];		//fine
				}
				else
				{
					if(dz > 0) meta = Orienter.metadataTable[Orienter.WEST][Orienter.NORTH];	//wrong
					else meta = Orienter.metadataTable[Orienter.WEST][Orienter.SOUTH]; //wrong
				}
				w.setblockandmeta(dimension, x-1, y, z, this.blockID, meta);
				break;
			case 4:
				if(Math.abs(dy)>Math.abs(dz))
				{
					if(dy > 0) meta = Orienter.metadataTable[Orienter.EAST][Orienter.UP]; //wrong
					else meta = Orienter.metadataTable[Orienter.EAST][Orienter.DOWN];		//wrong
				}
				else
				{
					if(dz > 0) meta = Orienter.metadataTable[Orienter.EAST][Orienter.NORTH];	//fine
					else meta = Orienter.metadataTable[Orienter.EAST][Orienter.SOUTH]; //wrong
				}
				w.setblockandmeta(dimension, x+1, y, z, this.blockID, meta);
				break;
			case 5:
				if(Math.abs(dx)>Math.abs(dz))
				{
					if(dx > 0) meta = Orienter.metadataTable[Orienter.DOWN][Orienter.WEST]; //fine
					else meta = Orienter.metadataTable[Orienter.DOWN][Orienter.EAST];		//fine
				}
				else
				{
					if(dz > 0) meta = Orienter.metadataTable[Orienter.DOWN][Orienter.NORTH];	//fine
					else meta = Orienter.metadataTable[Orienter.DOWN][Orienter.SOUTH];  //fine
				}
				w.setblockandmeta(dimension, x, y-1, z, this.blockID, meta);
				break;
		}
		
		w.playSound(Blocks.getPlaceSound(this.blockID), dimension, x, y, z, 0.5f, 1.0f);
		if(p == null)return;
		if(p.getGameMode() == GameModes.SURVIVAL)
		{
			InventoryContainer ic = p.getHotbar(p.gethotbarindex());
			if(ic != null)
			{
				ic.count--;
				p.setHotbarChanged(p.gethotbarindex());
				if(ic.count <= 0)
					p.setHotbar(p.gethotbarindex(), null);
			}
		}
		return;
	}
	
	// The 3 below methods were copied from DangerZone in accordance with the DangerZone license,
	// reproduced down below for your convenience. Please do follow it. 
	
	/*
	 * This code is copyright Richard H. Clark, TheyCallMeDanger, OreSpawn, 2015-2020.
	 * You may use this code for reference for modding the DangerZone game program,
	 * and are perfectly welcome to cut'n'paste portions for your mod as well.
	 * DO NOT USE THIS CODE FOR ANY PURPOSE OTHER THAN MODDING FOR THE DANGERZONE GAME.
	 * DO NOT REDISTRIBUTE THIS CODE. 
	 * 
	 * This copyright remains in effect until January 1st, 2021. 
	 * At that time, this code becomes public domain.
	 * 
	 * WARNING: There are bugs. Big bugs. Little bugs. Every size in-between bugs.
	 * This code is NOT suitable for use in anything other than this particular game. 
	 * NO GUARANTEES of any sort are given, either express or implied, and Richard H. Clark, 
	 * TheyCallMeDanger, OreSpawn are not responsible for any damages, direct, indirect, or otherwise. 
	 * You should have made backups. It's your own fault for not making them.
	 * 
	 * NO ATTEMPT AT SECURITY IS MADE. This code is USE AT YOUR OWN RISK.
	 * Regardless of what you may think, the reality is, that the moment you 
	 * connected your computer to the Internet, Uncle Sam, among many others, hacked it.
	 * DO NOT KEEP VALUABLE INFORMATION ON INTERNET-CONNECTED COMPUTERS.
	 * Or your phone...
	 * 
	 */
	
	public Texture getTexture(int side)
	{

		if(ttop == null){
			ttop = initBlockTexture(topname);
		}
		if(tbottom == null){
			tbottom = initBlockTexture(bottomname);
		}
		if(tleft == null){
			tleft = initBlockTexture(leftname);
		}
		if(tright == null){
			tright = initBlockTexture(rightname);
		}
		if(tfront == null){
			tfront = initBlockTexture(frontname);
		}
		if(tback == null){
			tback = initBlockTexture(backname);
		}
		
		if(side == 0)return ttop;
		if(side == 5)return tbottom;
		if(side == 3)return tleft;
		if(side == 4)return tright;
		if(side == 1)return tfront;
		if(side == 2)return tback;
		return null;
	}
	
	public StitchedTexture getStitchedTexture(int side)
	{	
		if(side == 0)return sttop;
		if(side == 5)return stbottom;
		if(side == 3)return stleft;
		if(side == 4)return stright;
		if(side == 1)return stfront;
		return stback;
	}
	
	public String getStitchedTextureName(int side)
	{
		if(side == 0)return topname;
		if(side == 5)return bottomname;
		if(side == 3)return leftname;
		if(side == 4)return rightname;
		if(side == 1)return frontname;
		return backname;
	}

}
