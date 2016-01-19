package blocks;

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
 * Active straight wires are the active form of straight wires.
 * <p>
 * A straight wire becomes active when supplied with at least 1 power. As soon
 * as power is exhausted, a straight wire will become inactive again. This glow
 * effect is entirely aesthetic, and supplies no illumination.
 * <p>
 * Active straight wires, as technical blocks, are not obtainable in-game
 * through any means.
 * 
 * @author eaglgenes101
 * @see StraightWire
 */

public class StraightWireActive extends StraightWire
{

	public StraightWireActive(String n)
	{
		super(n);
		topname = "RedZone_res/res/blocks/transparent.png";
		bottomname = "RedZone_res/res/blocks/straight_active.png";
		leftname = "RedZone_res/res/blocks/transparent.png";
		rightname = "RedZone_res/res/blocks/transparent.png";
		frontname = "RedZone_res/res/blocks/transparent.png";
		backname = "RedZone_res/res/blocks/transparent.png";
	}

}
