package redzone.blocks;

import org.lwjgl.input.Mouse;

import redzone.base.RedZoneMain;
import redzone.mechanics.PoweredComponent;
import dangerzone.Player;
import dangerzone.World;
import dangerzone.items.ItemPickAxe;

public class PressSwitch extends Wire implements PoweredComponent
{

	public PressSwitch(String n)
	{
		super(n);

		isStone = true;

		topname = "RedZone_res/blocks/transparent.png";
		bottomname = "RedZone_res/blocks/switch_off.png";
		leftname = "RedZone_res/blocks/transparent.png";
		rightname = "RedZone_res/blocks/transparent.png";
		frontname = "RedZone_res/blocks/transparent.png";
		backname = "RedZone_res/blocks/transparent.png";
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta)
	{
		return true;
	}

	public int getItemDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return RedZoneMain.PRESS_SWITCH_ITEM.itemID;
	}

	@Override
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z)
	{
		p.world.setblockandmetanonotify(dimension, x, y, z,
				RedZoneMain.PRESS_SWITCH_ACTIVE.blockID, 
				p.world.getblockmeta(dimension, x, y, z) );
		return false;
	}

}
