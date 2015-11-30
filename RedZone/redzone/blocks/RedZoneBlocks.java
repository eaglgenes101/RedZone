package redzone.blocks;

import dangerzone.blocks.Block;

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
	public static Block PIPE = new Pipe("RedZone:Pipe");
}
