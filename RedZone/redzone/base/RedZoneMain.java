package redzone.base;

import redzone.blocks.BentWire;
import redzone.blocks.BentWireActive;
import redzone.blocks.JunctionWire;
import redzone.blocks.JunctionWireActive;
import redzone.blocks.PowerStick;
import redzone.blocks.PowerStickInactive;
import redzone.blocks.SmokeTester;
import redzone.blocks.StraightWire;
import redzone.blocks.StraightWireActive;
import redzone.items.WireItem;
import dangerzone.BaseMod;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.items.Item;
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
 * Mod starter class. 
 * 
/*/

public class RedZoneMain extends BaseMod
{
	
	public static Block STRAIGHT_WIRE = new StraightWire("RedZone:Straight Wire Block");
	public static Block BENT_WIRE = new BentWire("RedZone:Bent Wire Block");
	public static Block JUNCTION_WIRE = new JunctionWire("RedZone:Junction Wire Block");
	public static Block POWER_STICK = new PowerStick("RedZone:Power Stick", "RedZone_res/blocks/power_stick_active.png");
	public static Block STRAIGHT_WIRE_ACTIVE = new StraightWireActive("RedZone:Straight Wire Active Block");
	public static Block BENT_WIRE_ACTIVE = new BentWireActive("RedZone:Bent Wire Active Block");
	public static Block JUNCTION_WIRE_ACTIVE = new JunctionWireActive("RedZone:Junction Wire Active Block");
	public static Block POWER_STICK_INACTIVE = new PowerStickInactive("RedZone:Power Stick Inactive", "RedZone_res/blocks/power_stick_inactive.png");
	public static Block SMOKE_TESTER = new SmokeTester("RedZone:Smoke Tester", "RedZone_res/blocks/smoketester.png");
	public static Item STRAIGHT_WIRE_ITEM = new WireItem("RedZone:Straight Wire", "RedZone_res/blocks/straight_inactive.png", STRAIGHT_WIRE);
	public static Item BENT_WIRE_ITEM = new WireItem("RedZone:Bent Wire", "RedZone_res/blocks/bent_inactive.png", BENT_WIRE);
	public static Item JUNCTION_WIRE_ITEM = new WireItem("RedZone:Junction Wire", "RedZone_res/blocks/junction_inactive.png", JUNCTION_WIRE);
	
	
	public RedZoneMain (){
		super();
	}
	
	@Override
	public String getModName()
	{
		return "RedZone v0.0";
	}
	
	@Override
	public String versionBuiltWith()
	{
		return "0.8";
	}
	
	public void registerThings()
	{
		Blocks.registerBlock(STRAIGHT_WIRE);
		Blocks.registerBlock(BENT_WIRE);
		Blocks.registerBlock(JUNCTION_WIRE);
		Blocks.registerBlock(POWER_STICK);
		Blocks.registerBlock(STRAIGHT_WIRE_ACTIVE);
		Blocks.registerBlock(BENT_WIRE_ACTIVE);
		Blocks.registerBlock(JUNCTION_WIRE_ACTIVE);
		Blocks.registerBlock(POWER_STICK_INACTIVE);
		Blocks.registerBlock(SMOKE_TESTER);
		Items.registerItem(STRAIGHT_WIRE_ITEM);
		Items.registerItem(BENT_WIRE_ITEM);
		Items.registerItem(JUNCTION_WIRE_ITEM);
	}
	
	
}
