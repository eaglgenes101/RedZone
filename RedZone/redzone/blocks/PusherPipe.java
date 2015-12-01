package redzone.blocks;

import dangerzone.World;
import redzone.mechanics.PoweredComponent;

public class PusherPipe extends Pipe implements PoweredComponent
{

	public PusherPipe(String n)
	{
		super(n);
		// TODO Auto-generated constructor stub
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
