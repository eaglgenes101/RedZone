package redzone.mechanics;

import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
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
 * All powered components in RedZone implement this interface!
 * 
/*/

public interface PoweredComponent 
{
	public static final int POWER_MASK = 0x003F;
	public static final int NOT_POWER_MASK = 0xFFC0;
	
	public static final int CYCLE_MASK = 0x0040;
	public static final int NOT_CYCLE_MASK = 0xFFBF;
	
	public static final int STATUS_MASK = 0x0080;
	public static final int NOT_STATUS_MASK = 0xFF7F;
	
	
	/**
	 * How much power does the component start with each tick?
	 * Call neighborBlock.getpowerlevel(w, d, x, y, z, w.getblockmeta(d, x, y, z)) to read a block's power level 
	 * @param w The world
	 * @param d The dimension
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	 * @return The power level of the block at the start of the tick (with default notifyNeighborChanged)
	**/
	public int basePowerLevel (World w, int d, int x, int y, int z);
	
	/**
	 * Can this block send a signal to a block with a given displacement?
	 * Used to establish connections between active powered components
	 * It can be assumed that dx, dy, and dz are all between -1 and 1 (Good idea to check, though)
	 * @param dx The x-displacement of the target to this block
	 * @param dy The y-displacement of the target to this block
	 * @param dz The z-displacement of the target to this block
	 * @param meta The metadata of this block
	 * @return Whether this block can send power to the given block
	**/
	public boolean canConnect (int dx, int dy, int dz, int meta);
	
	/**
	 * Set the cycle status of this block.
	 * @param w The world
	 * @param d The dimension
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	 * @param shouldMatch Whether the set cycle should match the clock cycle
	**/
	public default void setCycle(World w, int d, int x, int y, int z, boolean shouldMatch)
	{
		boolean whichMatch = (shouldMatch == (FastBlockTicker.cycle%2 == 0));
		w.setblockandmetanonotify(d, x, y, z, w.getblock(d, x, y, z), (w.getblockmeta(d, x, y, z)&NOT_CYCLE_MASK) | 
				(whichMatch? 0 : CYCLE_MASK));
	}
	
	/**
	 * What is this block's cycle match status? 
	 * @param w The world
	 * @param d The dimension
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	 * @return This block's cycle match status
	**/
	public default boolean getCycle(World w, int d, int x, int y, int z)
	{
		return (w.getblockmeta(d, x, y, z)&CYCLE_MASK) == ((FastBlockTicker.cycle%2)*CYCLE_MASK);
	}
	
	/**
	 * Set the status of this block. 
	 * @param w The world
	 * @param d The dimension
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	**/
	public default void setStatus(World w, int d, int x, int y, int z)
	{
		w.setblockandmetanonotify(d, x, y, z, w.getblock(d, x, y, z), 
				(w.getblockmeta(d, x, y, z)&NOT_STATUS_MASK) | 
				(((w.getblockmeta(d, x, y, z)&POWER_MASK)>0)?STATUS_MASK:0)
		);
	}
	
	/**
	 * What was the status of this block?
	 * @param w The world
	 * @param d The dimension
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	 * @return The status of this block 
	**/
	public default boolean getStatus(World w, int d, int x, int y, int z)
	{
		return (w.getblockmeta(d, x, y, z)&STATUS_MASK) == STATUS_MASK;
	}
	
	/**
	 * A method that sets all block's base power levels before the power levels get updated
	 * @param w The world
	 * @param d The dimension
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	**/
	public default void initStep (World w, int d, int x, int y, int z)
	{
		if (!this.getCycle(w, d, x, y, z)) //If they don't match
		{
			FastBlockTicker.addFastTick(d, x, y, z);
			this.setStatus(w, d, x, y, z);
			this.setCycle(w, d, x, y, z, true); //set to match
			for (int dx = -1; dx < 2; dx++)
				for (int dy = -1; dy < 2; dy++)
					for (int dz = -1; dz < 2; dz++)
					{
						Block input = Blocks.getBlock(w.getblock(d, x+dx, y+dy, z+dz));
						if (input instanceof PoweredComponent && 
								(
										(canConnect(dx, dy, dz, w.getblockmeta(d, x, y, z))) || 
										(canConnect(-dx, -dy, -dz, w.getblockmeta(d, x+dx, y+dy, z+dz))))
								)
							((PoweredComponent) input).initStep(w, d, x+dx, y+dy, z+dz); // Update! 
					}
			w.setblockandmetanonotify(d, x, y, z, w.getblock(d, x, y, z), (w.getblockmeta(d, x, y, z)&NOT_POWER_MASK) | 
					(this.basePowerLevel(w, d, x, y, z)&POWER_MASK));
		}
	}
	
	/**
	 * A method that sets the actual power levels
	 * @param w The world
	 * @param d The dimension
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	**/
	public default void powerStep (World w, int d, int x, int y, int z)
	{
		if (this.getCycle(w, d, x, y, z))
		{
			int maxpower = w.getblockmeta(d, x, y, z)&POWER_MASK; //Current power level
			boolean increased = false;
			for (int dx = -1; dx < 2; dx++)
				for (int dy = -1; dy < 2; dy++)
					for (int dz = -1; dz < 2; dz++)
					{
						Block input = Blocks.getBlock(w.getblock(d, x+dx, y+dy, z+dz));
						if (input instanceof PoweredComponent)
							if (((PoweredComponent) input).canConnect(-dx, -dy, -dz, w.getblockmeta(d, x+dx, y+dy, z+dz)))
								if( !( dx==0 && dy==0 && dz==0 ) )
									if ((w.getblockmeta(d, x+dx, y+dy, z+dz)&POWER_MASK) - 1 > maxpower)
									{
										maxpower = (w.getblockmeta(d, x+dx, y+dy, z+dz)&POWER_MASK) - 1;
										increased = true;
									}
					}
			w.setblockandmetanonotify(d, x, y, z, w.getblock(d, x, y, z), (w.getblockmeta(d, x, y, z)&NOT_POWER_MASK)|(maxpower&POWER_MASK));
			setCycle(w, d, x, y, z, false); //Let's not confuse any other blocks. We've updated now. 
			for(int dx = -1; dx < 2; dx++)
				for (int dy = -1; dy < 2; dy++)
					for (int dz = -1; dz < 2; dz++)
					{
						Block input = Blocks.getBlock(w.getblock(d, x+dx, y+dy, z+dz));
						if (input instanceof PoweredComponent)
							if (canConnect(dx, dy, dz, w.getblockmeta(d, x, y, z)))
								if ( !(dx==0 && dy==0 && dz==0))
									if (((PoweredComponent)input).getCycle(w, d, x+dx, y+dy, z+dz) || increased)
										//Either its status is new or we increased power. Either way, bump it!
										((PoweredComponent) input).powerStep(w, d, x+dx, y+dy, z+dz);
					}
		}
	}
	
	/**
	 * A method that finishes off the block
	 * @param w The world
	 * @param d The dimension
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	**/
	public default void closingStep (World w, int d, int x, int y, int z)
	{
		if (!this.getCycle(w, d, x, y, z))
		{
			this.finishStep(w, d, x, y, z); //Shouldn't run more than once each time, but what can I do?
			this.setCycle(w, d, x, y, z, true); // Ding! Set to match (next tick, it won't match any longer)
			for (int dx = -1; dx < 2; dx++)
				for (int dy = -1; dy < 2; dy++)
					for (int dz = -1; dz < 2; dz++)
					{
						Block input = Blocks.getBlock(w.getblock(d, x+dx, y+dy, z+dz));
						if (input instanceof PoweredComponent && 
								(
										(canConnect(dx, dy, dz, w.getblockmeta(d, x, y, z))) || 
										(canConnect(-dx, -dy, -dz, w.getblockmeta(d, x+dx, y+dy, z+dz)))
								)
								&& (!((PoweredComponent)input).getCycle(w, d, x+dx, y+dy, z+dz)))
							if( !( dx==0 && dy==0 && dz==0 ) )
								((PoweredComponent) input).closingStep(w, d, x+dx, y+dy, z+dz);
					}
		}
	}
	
	/**
	 * What happens after everything happens? You decide!
	 * @param w The world
	 * @param d The dimension
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	**/
	public void finishStep (World w, int d, int x, int y, int z);
	
	/**
	 * A power updating function (because the block notification system is buffered in a less-than-useful way)
	 * @param w The world
	 * @param d The dimension
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	**/
	public default void powerBump(World w, int d, int x, int y, int z)
	{
		this.initStep(w, d, x, y, z);
		this.powerStep(w, d, x, y, z);
		this.closingStep(w, d, x, y, z);
	}

}
