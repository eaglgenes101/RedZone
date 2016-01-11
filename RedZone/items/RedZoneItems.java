package items;

import blocks.RedZoneBlocks;
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
 * All RedZone items, listed in one place for convenience. 
 * 
/*/

public class RedZoneItems
{
	public static Item STRAIGHT_WIRE_ITEM = new WireItem("RedZone:Straight Wire", "RedZone_res/res/blocks/straight_inactive.png", RedZoneBlocks.STRAIGHT_WIRE);
	public static Item BENT_WIRE_ITEM = new WireItem("RedZone:Bent Wire", "RedZone_res/res/blocks/bent_inactive.png", RedZoneBlocks.BENT_WIRE);
	public static Item JUNCTION_WIRE_ITEM = new WireItem("RedZone:Junction Wire", "RedZone_res/res/blocks/junction_inactive.png", RedZoneBlocks.JUNCTION_WIRE);
	public static Item END_WIRE_ITEM = new WireItem("RedZone:End Wire", "RedZone_res/res/blocks/end_inactive.png", RedZoneBlocks.END_WIRE);
	public static Item PRESS_SWITCH_ITEM = new WireItem("RedZone:Press Switch", "RedZone_res/res/blocks/switch_off.png", RedZoneBlocks.PRESS_SWITCH);

}
