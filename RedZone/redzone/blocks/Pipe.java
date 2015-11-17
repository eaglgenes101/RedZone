package redzone.blocks;

import org.newdawn.slick.opengl.Texture;

import redzone.mechanics.PoweredComponent;
import dangerzone.StitchedTexture;
import dangerzone.World;
import dangerzone.blocks.Block;

public class Pipe extends Block implements PoweredComponent
{

	Texture ttop = null;
	Texture tbottom = null;
	Texture tleft = null;
	Texture tright = null;
	Texture tfront = null;
	Texture tback = null;

	String topname;
	String bottomname;
	String leftname;
	String rightname;
	String frontname;
	String backname;

	StitchedTexture sttop = new StitchedTexture();
	StitchedTexture stbottom = new StitchedTexture();
	StitchedTexture stleft = new StitchedTexture();
	StitchedTexture stright = new StitchedTexture();
	StitchedTexture stfront = new StitchedTexture();
	StitchedTexture stback = new StitchedTexture();
	
	public Pipe(String n, String txt)
	{
		super(n, "");

		topname = "RedZone_res/blocks/pipe_end.png";
		bottomname = "RedZone_res/blocks/pipe_end.png";
		leftname = "RedZone_res/blocks/pipe_edge.png";
		rightname = "RedZone_res/blocks/pipe_edge.png";
		frontname = "RedZone_res/blocks/pipe_edge.png";
		backname = "RedZone_res/blocks/pipe_edge.png";

		mindamage = 5;
		maxdamage = 80;
		maxstack = 8;
		isStone = true;
		hasFront = true;
		isSolidForRendering = false;
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
