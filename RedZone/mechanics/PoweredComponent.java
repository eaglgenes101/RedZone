package mechanics;

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
/*/

/**
 * Signals in RedZone follow a set of simple rules, as detailed by the
 * PoweredComponent interface.
 * <p>
 * In RedZone, there are 64 levels of power. 0 is the lowest, and 63 is the
 * highest. Powered components may generate any level of power, and will send a
 * signal equal to that level of power minus one to all components that they
 * connect to. This, in turn, will cause updated components to send a signal
 * similarly, and so on until the system is at equilibrium.
 * <p>
 * If you wish to implement this interface, be aware of these details:
 * <ul>
 * <li>Remember to set alwaystick to true, register the block with the fast
 * block ticker, and call powerBump each fast tick. I tried, but I can't
 * override class methods with an interface, so you'll have to do it yourself.
 * <li>All sixteen metadata bits are reserved in RedZone components. The first 8
 * are reserved by DangerZone, the 9th is reserved for maintaining power status,
 * the 10th is reserved for maintaining cycle updates, and the last 6 hold the
 * current power level.
 * <li>Metadata should not be manipulated directly. RedZone assumes that only it
 * will be modifying the last eight bits of metadata, and violating this
 * assumption causes undefined behavior.
 * <li>Leave all the default methods alone unless you really know what you're
 * doing.
 * <li>Signals can only be sent to neighboring blocks. Trying to directly
 * interface with components farther away is undefined behavior, and will cause
 * component behavior to be at the mercy of game quirks.
 * </ul>
 * 
 * @author eaglgenes101
 * @see blocks.Wire
 * @see blocks.PowerStick
 * @see blocks.Dispenser
 * @see blocks.Dropper
 * @see blocks.SmokeTester
 * @see blocks.DetectorSwitch
 * @see blocks.PressSwitch
 * @see blocks.ProximityDetector
 */

public interface PoweredComponent
{
	public static final int POWER_MASK = 0x003F;
	public static final int NOT_POWER_MASK = 0xFFC0;

	public static final int CYCLE_MASK = 0x0040;
	public static final int NOT_CYCLE_MASK = 0xFFBF;

	public static final int STATUS_MASK = 0x0080;
	public static final int NOT_STATUS_MASK = 0xFF7F;

	/**
	 * Returns a component's current power level.
	 * 
	 * This method should be called directly to determine how much power a
	 * powered component has.
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 * @return The current power level of the block
	 */
	public default int getPowerLevel(World w, int d, int x, int y, int z)
	{
		return w.getblockmeta(d, x, y, z) & POWER_MASK;
	}

	/**
	 * Determines how much power this component starts with each tick.
	 * 
	 * This method is called by initStep to start each powered component off
	 * with a base level of power. Go ahead and use it for yourself.
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 * @return The power level of the block at the start of the tick (with
	 *         default notifyNeighborChanged)
	 */
	public int basePowerLevel(World w, int d, int x, int y, int z);

	/**
	 * Determines whether this block can send a signal to a block with the given
	 * displacement.
	 * 
	 * This method is used to establish connections between active powered
	 * components. It can be assumed that dx, dy, and dz are all between -1 and
	 * 1, bit it's a good idea to check nevertheless.
	 * 
	 * @param dx
	 *            The x-displacement of the target to this block
	 * @param dy
	 *            The y-displacement of the target to this block
	 * @param dz
	 *            The z-displacement of the target to this block
	 * @param meta
	 *            The metadata of this block
	 * @return Whether this block can send power to the given block
	 */
	public boolean canConnect(int dx, int dy, int dz, int meta);

	/**
	 * Set the cycle status of this block.
	 * 
	 * This method should not be called directly.
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 * @param shouldMatch
	 *            Whether the set cycle should match the clock cycle
	 */
	public default void setCycle(World w, int d, int x, int y, int z, boolean shouldMatch)
	{
		boolean whichMatch = (shouldMatch == (FastBlockTicker.cycle % 2 == 0));
		w.setblockandmetanonotify(d, x, y, z, w.getblock(d, x, y, z),
				(w.getblockmeta(d, x, y, z) & NOT_CYCLE_MASK) | (whichMatch ? 0 : CYCLE_MASK));
	}

	/**
	 * Returns the block's cycle match status.
	 * 
	 * This method should not be called directly.
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 * @return This block's cycle match status
	 */
	public default boolean getCycle(World w, int d, int x, int y, int z)
	{
		return (w.getblockmeta(d, x, y, z) & CYCLE_MASK) == ((FastBlockTicker.cycle % 2) * CYCLE_MASK);
	}

	/**
	 * Sets a metadata bit indicating whether this component was powered last
	 * tick.
	 * 
	 * This method should not be called directly.
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 */
	public default void setStatus(World w, int d, int x, int y, int z)
	{
		w.setblockandmetanonotify(d, x, y, z, w.getblock(d, x, y, z), (w.getblockmeta(d, x, y, z) & NOT_STATUS_MASK)
				| ((getPowerLevel(w, d, x, y, z) > 0) ? STATUS_MASK : 0));
	}

	/**
	 * Returns whether the block was powered last tick.
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 * @return The status of this block
	 */
	public default boolean getStatus(World w, int d, int x, int y, int z)
	{
		return (w.getblockmeta(d, x, y, z) & STATUS_MASK) == STATUS_MASK;
	}

	/**
	 * Sets the metadata bits of all powered components contiguous to this
	 * component.
	 * 
	 * This method should not be called directly.
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 */
	public default void initStep(World w, int d, int x, int y, int z)
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
						Block input = Blocks.getBlock(w.getblock(d, x + dx, y + dy, z + dz));
						if (input instanceof PoweredComponent && ((canConnect(dx, dy, dz, w.getblockmeta(d, x, y, z)))
								|| (canConnect(-dx, -dy, -dz, w.getblockmeta(d, x + dx, y + dy, z + dz)))))
							((PoweredComponent) input).initStep(w, d, x + dx, y + dy, z + dz); // Update! 
					}
			w.setblockandmetanonotify(d, x, y, z, w.getblock(d, x, y, z),
					(w.getblockmeta(d, x, y, z) & NOT_POWER_MASK) | (this.basePowerLevel(w, d, x, y, z) & POWER_MASK));
		}
	}

	/**
	 * Recursively updates the power levels of all components contiguous to this
	 * component.
	 * 
	 * This method should not be called directly.
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 */
	public default void powerStep(World w, int d, int x, int y, int z)
	{
		if (this.getCycle(w, d, x, y, z))
		{
			int maxpower = getPowerLevel(w, d, x, y, z); //Current power level
			boolean increased = false;
			for (int dx = -1; dx < 2; dx++)
				for (int dy = -1; dy < 2; dy++)
					for (int dz = -1; dz < 2; dz++)
					{
						Block input = Blocks.getBlock(w.getblock(d, x + dx, y + dy, z + dz));
						if (input instanceof PoweredComponent)
							if (((PoweredComponent) input).canConnect(-dx, -dy, -dz,
									w.getblockmeta(d, x + dx, y + dy, z + dz)))
								if (!(dx == 0 && dy == 0 && dz == 0))
									if ((w.getblockmeta(d, x + dx, y + dy, z + dz) & POWER_MASK) - 1 > maxpower)
									{
										maxpower = (w.getblockmeta(d, x + dx, y + dy, z + dz) & POWER_MASK) - 1;
										increased = true;
									}
					}
			w.setblockandmetanonotify(d, x, y, z, w.getblock(d, x, y, z),
					(w.getblockmeta(d, x, y, z) & NOT_POWER_MASK) | (maxpower & POWER_MASK));
			setCycle(w, d, x, y, z, false); //Let's not confuse any other blocks. We've updated now. 
			for (int dx = -1; dx < 2; dx++)
				for (int dy = -1; dy < 2; dy++)
					for (int dz = -1; dz < 2; dz++)
					{
						Block input = Blocks.getBlock(w.getblock(d, x + dx, y + dy, z + dz));
						if (input instanceof PoweredComponent)
							if (canConnect(dx, dy, dz, w.getblockmeta(d, x, y, z)))
								if (!(dx == 0 && dy == 0 && dz == 0))
									if (((PoweredComponent) input).getCycle(w, d, x + dx, y + dy, z + dz) || increased)
										//Either its status is new or we increased power. Either way, bump it!
										((PoweredComponent) input).powerStep(w, d, x + dx, y + dy, z + dz);
					}
		}
	}

	/**
	 * Calls finishStep on all powered components contiguous to this component.
	 * 
	 * This method should not be called directly.
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 */
	public default void closingStep(World w, int d, int x, int y, int z)
	{
		if (!this.getCycle(w, d, x, y, z))
		{
			this.finishStep(w, d, x, y, z); //Shouldn't run more than once each time, but what can I do?
			this.setCycle(w, d, x, y, z, true); // Ding! Set to match (next tick, it won't match any longer)
			for (int dx = -1; dx < 2; dx++)
				for (int dy = -1; dy < 2; dy++)
					for (int dz = -1; dz < 2; dz++)
					{
						Block input = Blocks.getBlock(w.getblock(d, x + dx, y + dy, z + dz));
						if (input instanceof PoweredComponent
								&& ((canConnect(dx, dy, dz, w.getblockmeta(d, x, y, z)))
										|| (canConnect(-dx, -dy, -dz, w.getblockmeta(d, x + dx, y + dy, z + dz))))
								&& (!((PoweredComponent) input).getCycle(w, d, x + dx, y + dy, z + dz)))
							if (!(dx == 0 && dy == 0 && dz == 0))
								((PoweredComponent) input).closingStep(w, d, x + dx, y + dy, z + dz);
					}
		}
	}

	/**
	 * What happens after everything happens? You decide!
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 */
	public void finishStep(World w, int d, int x, int y, int z);

	/**
	 * Causes a full power cycle to be initiated in all powered components that
	 * contiguous to it.
	 * 
	 * This method should be called every fast tick.
	 * 
	 * @param w
	 *            The world
	 * @param d
	 *            The dimension
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param z
	 *            The z-coordinate
	 */
	public default void powerBump(World w, int d, int x, int y, int z)
	{
		this.initStep(w, d, x, y, z);
		this.powerStep(w, d, x, y, z);
		this.closingStep(w, d, x, y, z);
	}

}
