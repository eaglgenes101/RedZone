package redzone.blocks;

import dangerzone.blocks.Block;

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
 * All RedZone blocks, listed in one place for convenience. 
 * 
/*/

public class RedZoneBlocks
{
	public static Block STRAIGHT_WIRE = new StraightWire("RedZone:Straight Wire Block");
	public static Block BENT_WIRE = new BentWire("RedZone:Bent Wire Block");
	public static Block JUNCTION_WIRE = new JunctionWire("RedZone:Junction Wire Block");
	public static Block POWER_STICK = new PowerStick("RedZone:Power Stick", "RedZone_res/res/blocks/power_stick_active.png");
	public static Block STRAIGHT_WIRE_ACTIVE = new StraightWireActive("RedZone:Straight Wire Active Block");
	public static Block BENT_WIRE_ACTIVE = new BentWireActive("RedZone:Bent Wire Active Block");
	public static Block JUNCTION_WIRE_ACTIVE = new JunctionWireActive("RedZone:Junction Wire Active Block");
	public static Block POWER_STICK_INACTIVE = new PowerStickInactive("RedZone:Power Stick Inactive", "RedZone_res/res/blocks/power_stick_inactive.png");
	public static Block SMOKE_TESTER = new SmokeTester("RedZone:Smoke Tester", "RedZone_res/res/blocks/smoketester.png");
	public static Block DISPENSER = new Dispenser("RedZone:Dispenser");
	public static Block PRESS_SWITCH = new PressSwitch("RedZone:Press Switch Block");
	public static Block PRESS_SWITCH_ACTIVE = new PressSwitchActive("RedZone:Press Switch Active Block");
	public static Block DETECTOR_SWITCH = new DetectorSwitch("RedZone:Detector Switch Block");
	public static Block STRAIGHT_PIPE = new StraightPipe("RedZone:StraightPipe");
	public static Block CORNER_PIPE = new CornerPipe("RedZone:CornerPipe");
	public static Block PUSHER_STRAIGHT_PIPE = new PusherStraightPipe("RedZone:PusherStraightPipe");
}
