package blocks;

import java.util.List;
import java.util.ListIterator;

import org.newdawn.slick.opengl.Texture;

import dangerzone.DangerZone;
import dangerzone.StitchedTexture;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityLiving;
import dangerzone.threads.FastBlockTicker;
import entities.EntityPushedBlock;
import mechanics.Orienter;
import mechanics.PoweredComponent;

/**
 * @author eaglgenes101
 *
 */
public class TractorShooter extends Block implements PoweredComponent
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

	public TractorShooter(String n)
	{
		super(n, "");
		topname = "RedZone_res/res/blocks/dispenser_side.png";
		bottomname = "RedZone_res/res/blocks/dispenser_side.png";
		leftname = "RedZone_res/res/blocks/dispenser_side.png";
		rightname = "RedZone_res/res/blocks/dispenser_side.png";
		frontname = "RedZone_res/res/blocks/tractor_front.png";
		backname = "RedZone_res/res/blocks/dispenser_side.png";

		mindamage = 5;
		maxdamage = 80;
		maxstack = 8;
		isStone = true;
		hasFront = true;
		isSolidForRendering = true;
	}

	@Override
	public int basePowerLevel(World w, int d, int x, int y, int z)
	{
		return 0;
	}

	@Override
	public boolean canConnect(int dx, int dy, int dz, int meta)
	{
		return true;
	}

	@Override
	public void finishStep(World w, int d, int x, int y, int z)
	{
		int powerLevel = getPowerLevel(w, d, x, y, z);
		int reach = 1;
		double[] forward = Orienter.getDirection(Orienter.NORTH_VECTOR, w.getblockmeta(d, x, y, z));
		int[] offset = {(int) Math.round(forward[0]), (int) Math.round(forward[1]), (int) Math.round(forward[2])};

		outerLoop:
		while (!Blocks.isSolid(w.getblock(d, x + offset[0] * reach, y + offset[1] * reach, z + offset[2] * reach))
				&& powerLevel > 0)
		{
			
			List<Entity> nearby_list = DangerZone.entityManager.findEntitiesInRange(2.0f, d,
					x + offset[0] * reach + 0.5f, y + offset[1] * reach + 0.5f, z + offset[2] * reach + 0.5f);
			ListIterator<Entity> li;

			if (nearby_list != null)
			{
				li = nearby_list.listIterator();
				Entity e;
				while (li.hasNext())
				{
					e = (Entity) li.next();
					if (!e.canthitme)
					{
						if (x + offset[0] * reach == (int) e.posx && y + offset[1] * reach == (int) e.posy
								&& z + offset[2] * reach == (int) e.posz)
						{
							break outerLoop;
						}
						if (e instanceof EntityLiving)
						{
							EntityLiving el = (EntityLiving) e;
							int intheight = (int) (el.height + 0.995f);
							float dx, dz;
							for (int k = 0; k < intheight; k++)
							{
								if ((int) el.posy + k == y + offset[1] * reach)
								{
									dx = el.posx - ((float) x + offset[0] * reach + 0.5f);
									dz = el.posz - ((float) z + offset[2] * reach + 0.5f);
									if (Math.sqrt((dx * dx) + (dz * dz)) < (0.5f + (el.width / 2.0f)))
									{
										break outerLoop;
									}
								}
							}
						}
					}
				}
			}

			if (w.getblock(d, x + offset[0] * reach, y + offset[1] * reach,
					z + offset[2] * reach) != RedZoneBlocks.TRACTOR_BEAM.blockID)
				w.setblockandmeta(d, x + offset[0] * reach, y + offset[1] * reach, z + offset[2] * reach,
						RedZoneBlocks.TRACTOR_BEAM.blockID, Orienter.getSideForm(offset) << 8);
			else if (w.getblockmeta(d, x + offset[0] * reach, y + offset[1] * reach,
					z + offset[2] * reach) >> 8 != Orienter.getSideForm(offset))
				w.setblockandmeta(d, x + offset[0] * reach, y + offset[1] * reach, z + offset[2] * reach,
						RedZoneBlocks.TRACTOR_BEAM.blockID, Orienter.getSideForm(offset) << 8);
			
			powerLevel--;
			reach++;
		}
		FastBlockTicker.addFastTick(d, x + offset[0], y + offset[1], z + offset[2]);
	}

	@Override
	public void tickMe(World w, int d, int x, int y, int z)
	{
		FastBlockTicker.addFastTick(d, x, y, z);
	}

	public void tickMeFast(World w, int d, int x, int y, int z)
	{
		((PoweredComponent) this).powerBump(w, d, x, y, z);
	}

	// The below methods were copied from DangerZone in accordance with the DangerZone license,
	// reproduced down below for your convenience. Please do follow it.

	/*
	 * This code is copyright Richard H. Clark, TheyCallMeDanger, OreSpawn,
	 * 2015-2020. You may use this code for reference for modding the DangerZone
	 * game program, and are perfectly welcome to cut'n'paste portions for your
	 * mod as well. DO NOT USE THIS CODE FOR ANY PURPOSE OTHER THAN MODDING FOR
	 * THE DANGERZONE GAME. DO NOT REDISTRIBUTE THIS CODE.
	 * 
	 * This copyright remains in effect until January 1st, 2021. At that time,
	 * this code becomes public domain.
	 * 
	 * WARNING: There are bugs. Big bugs. Little bugs. Every size in-between
	 * bugs. This code is NOT suitable for use in anything other than this
	 * particular game. NO GUARANTEES of any sort are given, either express or
	 * implied, and Richard H. Clark, TheyCallMeDanger, OreSpawn are not
	 * responsible for any damages, direct, indirect, or otherwise. You should
	 * have made backups. It's your own fault for not making them.
	 * 
	 * NO ATTEMPT AT SECURITY IS MADE. This code is USE AT YOUR OWN RISK.
	 * Regardless of what you may think, the reality is, that the moment you
	 * connected your computer to the Internet, Uncle Sam, among many others,
	 * hacked it. DO NOT KEEP VALUABLE INFORMATION ON INTERNET-CONNECTED
	 * COMPUTERS. Or your phone...
	 */

	// side 0 = top
	// side 1 = front
	// side 2 = back
	// side 3 = left
	// side 4 = right
	// side 5 = bottom
	public Texture getTexture(int side)
	{

		if (ttop == null)
		{
			ttop = initBlockTexture(topname);
		}
		if (tbottom == null)
		{
			tbottom = initBlockTexture(bottomname);
		}
		if (tleft == null)
		{
			tleft = initBlockTexture(leftname);
		}
		if (tright == null)
		{
			tright = initBlockTexture(rightname);
		}
		if (tfront == null)
		{
			tfront = initBlockTexture(frontname);
		}
		if (tback == null)
		{
			tback = initBlockTexture(backname);
		}

		if (side == 0)
			return ttop;
		if (side == 5)
			return tbottom;
		if (side == 3)
			return tleft;
		if (side == 4)
			return tright;
		if (side == 1)
			return tfront;
		if (side == 2)
			return tback;
		return null;
	}

	public StitchedTexture getStitchedTexture(int side)
	{
		if (side == 0)
			return sttop;
		if (side == 5)
			return stbottom;
		if (side == 3)
			return stleft;
		if (side == 4)
			return stright;
		if (side == 1)
			return stfront;
		return stback;
	}

	public String getStitchedTextureName(int side)
	{
		if (side == 0)
			return topname;
		if (side == 5)
			return bottomname;
		if (side == 3)
			return leftname;
		if (side == 4)
			return rightname;
		if (side == 1)
			return frontname;
		return backname;
	}

}
