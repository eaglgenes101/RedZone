package blocks;

import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.threads.FastBlockTicker;
import mechanics.PoweredComponent;

public class ItemCollector extends Block implements PoweredComponent
{

	public ItemCollector(String n, String txt)
	{
		super(n, txt);
		// TODO Auto-generated constructor stub
	}
	
	public void tickMeFast(World w, int d, int x, int y, int z)
	{
		FastBlockTicker.addFastTick(d, x, y, z);
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void finishStep(World w, int d, int x, int y, int z)
	{
		// TODO Auto-generated method stub

	}

}
