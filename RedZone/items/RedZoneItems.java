package items;

import blocks.RedZoneBlocks;
import dangerzone.items.Item;

public class RedZoneItems
{
	public static Item STRAIGHT_WIRE_ITEM = new WireItem("RedZone:Straight Wire", "RedZone_res/res/blocks/straight_inactive.png", RedZoneBlocks.STRAIGHT_WIRE);
	public static Item BENT_WIRE_ITEM = new WireItem("RedZone:Bent Wire", "RedZone_res/res/blocks/bent_inactive.png", RedZoneBlocks.BENT_WIRE);
	public static Item JUNCTION_WIRE_ITEM = new WireItem("RedZone:Junction Wire", "RedZone_res/res/blocks/junction_inactive.png", RedZoneBlocks.JUNCTION_WIRE);
	public static Item PRESS_SWITCH_ITEM = new WireItem("RedZone:Press Switch", "RedZone_res/res/blocks/switch_off.png", RedZoneBlocks.PRESS_SWITCH);

}
