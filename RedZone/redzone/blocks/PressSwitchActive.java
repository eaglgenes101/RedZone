package redzone.blocks;

import redzone.base.RedZoneMain;
import redzone.mechanics.PoweredComponent;
import dangerzone.Player;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.items.ItemPickAxe;

public class PressSwitchActive extends PressSwitch implements PoweredComponent
{

	public PressSwitchActive(String n)
	{
		super(n);

		topname = "RedZone_res/blocks/transparent.png";
		bottomname = "RedZone_res/blocks/switch_on.png";
		leftname = "RedZone_res/blocks/transparent.png";
		rightname = "RedZone_res/blocks/transparent.png";
		frontname = "RedZone_res/blocks/transparent.png";
		backname = "RedZone_res/blocks/transparent.png";
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		return 63;
	}
	
	@Override
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z)
	{
		p.world.setblockandmetanonotify(dimension, x, y, z,
				RedZoneMain.PRESS_SWITCH.blockID, 
				p.world.getblockmeta(dimension, x, y, z) );
		return false;
	}
	
	

}
