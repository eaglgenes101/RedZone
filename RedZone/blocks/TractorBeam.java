package blocks;

import java.util.List;
import java.util.ListIterator;

import dangerzone.DangerZone;
import dangerzone.Player;
import dangerzone.World;
import dangerzone.blocks.Block;
import dangerzone.blocks.Blocks;
import dangerzone.blocks.LightStick;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityBlockItem;
import dangerzone.entities.EntityLiving;
import dangerzone.threads.FastBlockTicker;
import entities.EntityPushedBlock;
import mechanics.Orienter;

public class TractorBeam extends LightStick
{

	public TractorBeam(String n, String txt)
	{
		super(n, txt);
		showInInventory = false;
		brightness = 0.6f;
		alwaystick = true;
		isSolid = false;
		maxdamage = 10000;
		mindamage = 1000000;
	}

	public void tickMe(World w, int d, int x, int y, int z)
	{
		FastBlockTicker.addFastTick(d, x, y, z);
		//Nothing else :)
	}

	public void notifyNeighborChanged(World w, int d, int x, int y, int z)
	{
		this.tickMeFast(w, d, x, y, z);
	}

	@Override
	public void tickMeFast(World w, int d, int x, int y, int z)
	{
		double[] itsvec;
		switch (w.getblockmeta(d, x, y, z) >> 8)
		{
			case 0:
				itsvec = Orienter.UP_VECTOR;
				break;
			case 1:
				itsvec = Orienter.NORTH_VECTOR;
				break;
			case 2:
				itsvec = Orienter.SOUTH_VECTOR;
				break;
			case 3:
				itsvec = Orienter.EAST_VECTOR;
				break;
			case 4:
				itsvec = Orienter.WEST_VECTOR;
				break;
			case 5:
				itsvec = Orienter.DOWN_VECTOR;
				break;
			default:
				itsvec = Orienter.UP_VECTOR;
				break;
		}
		int[] rounded = {(int) Math.round(itsvec[0]), (int) Math.round(itsvec[1]), (int) Math.round(itsvec[2])};

		if (w.isServer)
		{
			List<Entity> nearby_list = DangerZone.server.entityManager.findEntitiesInRange(2.0f, d, x + 0.5f, y + 0.5f,
					z + 0.5f);
			ListIterator<Entity> li;

			boolean hitBlockEntity = false;

			if (nearby_list != null)
			{
				li = nearby_list.listIterator();
				Entity e;
				while (li.hasNext())
				{
					e = (Entity) li.next();
					if (!e.canthitme)
					{
						boolean shouldPush = false;
						if (x + rounded[0] == (int) e.posx && y + rounded[1] == (int) e.posy
								&& z + rounded[2] == (int) e.posz)
						{
							shouldPush = true;
						}
						if (x == (int) e.posx && y == (int) e.posy && z == (int) e.posz)
						{
							shouldPush = true;
						}
						if (e instanceof EntityLiving)
						{
							EntityLiving el = (EntityLiving) e;
							int intheight = (int) (el.height + 0.995f);
							float dx, dz;
							for (int k = 0; k < intheight; k++)
							{
								if ((int) el.posy + k == y)
								{
									dx = el.posx - ((float) x + 0.5f);
									dz = el.posz - ((float) z + 0.5f);
									if (Math.sqrt((dx * dx) + (dz * dz)) < (0.5f + (el.width / 2.0f)))
									{
										shouldPush = true;
									}
								}
							}
						}

						if (shouldPush)
						{
							e.motionx -= rounded[0];
							e.motiony -= rounded[1];
							e.motionz -= rounded[2];
						}
						if (e instanceof EntityBlockItem)
							e.deadflag = true;
						if (e instanceof EntityPushedBlock)
						{
							hitBlockEntity = true;
						}
					}
				}
			}
			if (w.getblock(d, x + rounded[0], y + rounded[1], z + rounded[2]) != RedZoneBlocks.TRACTOR_BEAM.blockID
					&& !hitBlockEntity)
			{
				if (Blocks.isSolid(w.getblock(d, x + rounded[0], y + rounded[1], z + rounded[2])))
				{
					Entity e = w.createEntityByName("RedZone:EntityPushedBlock", d, x + rounded[0] + 0.5f,
							y + rounded[1] + 0.5f, z + rounded[2] + 0.5f);

					int bid = w.getblock(d, x + rounded[0], y + rounded[1], z + rounded[2]);
					int meta = w.getblockmeta(d, x + rounded[0], y + rounded[1], z + rounded[2]);
					w.setblock(d, x + rounded[0], y + rounded[1], z + rounded[2], 0);

					e.init();
					e.setBID(bid);
					e.setIID(meta);
					w.spawnEntityInWorld(e);
				}
			}
		}
		
		if (w.getblock(d, x - rounded[0], y - rounded[1], z - rounded[2]) == RedZoneBlocks.TRACTOR_BEAM.blockID)
		{
			if (w.getblockmeta(d, x - rounded[0], y - rounded[1], z - rounded[2]) >> 8 == w.getblockmeta(d, x, y,
					z) >> 8)
				return;
		}
		else if (w.getblock(d, x - rounded[0], y - rounded[1], z - rounded[2]) == RedZoneBlocks.TRACTOR_SHOOTER.blockID)
		{
			double[] otherBlockDir = Orienter.getDirection(Orienter.NORTH_VECTOR,
					w.getblockmeta(d, x - rounded[0], y - rounded[1], z - rounded[2]));
			int[] roundedDir = {(int) Math.round(otherBlockDir[0]), (int) Math.round(otherBlockDir[1]),
					(int) Math.round(otherBlockDir[2])};
			if (Orienter.getSideForm(roundedDir) == (w.getblockmeta(d, x, y, z) >> 8))
			{
				TractorShooter ts = (TractorShooter) RedZoneBlocks.TRACTOR_SHOOTER;
				if (ts != null && ts.getPowerLevel(w, d, x - rounded[0], y - rounded[1], z - rounded[2]) > 0)
					return;
			}
		}
		w.setblocknonotify(d, x, y, z, 0);
		Block toTick = Blocks.getBlock(w.getblock(d, x + rounded[0], y + rounded[1], z + rounded[2]));
		if (toTick != null)
			toTick.tickMeFast(w, d, x + rounded[0], y + rounded[1], z + rounded[2]);
	}

	@Override
	public int getBlockDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return 0;
	}

	@Override
	public int getItemDrop(Player p, World w, int dimension, int x, int y, int z)
	{
		return 0;
	}

}
