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

public class RepulsorBeam extends TractorBeam
{

	public RepulsorBeam(String n, String txt)
	{
		super(n, txt);
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
			List<Entity> nearby_list = DangerZone.server.entityManager.findALLEntitiesInRange(2.0f, d, x + 0.5, y + 0.5,
					z + 0.5);
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
							int intheight = (int) (el.height + 0.995);
							double dx, dz;
							for (int k = 0; k < intheight; k++)
							{
								if ((int) el.posy + k == y)
								{
									dx = el.posx - (x + 0.5);
									dz = el.posz - (z + 0.5);
									if (Math.sqrt((dx * dx) + (dz * dz)) < (0.5 + (el.width / 2.0)))
									{
										shouldPush = true;
									}
								}
							}
						}

						if (shouldPush)
						{
							e.motionx += rounded[0]*0.05;
							e.motiony += rounded[1]*0.05;
							e.motionz += rounded[2]*0.05;
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
			if (w.getblock(d, x + rounded[0], y + rounded[1], z + rounded[2]) != RedZoneBlocks.REPULSOR_BEAM.blockID
					&& !hitBlockEntity)
			{
				if (Blocks.isSolid(w.getblock(d, x + rounded[0], y + rounded[1], z + rounded[2])))
				{
					Entity e = w.createEntityByName("RedZone:EntityPushedBlock", d, x + rounded[0] + 0.5,
							y + rounded[1] + 0.5, z + rounded[2] + 0.5);
					e.init();
					w.spawnEntityInWorld(e);
				}
			}
		}
		
		if (w.getblock(d, x - rounded[0], y - rounded[1], z - rounded[2]) == RedZoneBlocks.REPULSOR_BEAM.blockID)
		{
			if (w.getblockmeta(d, x - rounded[0], y - rounded[1], z - rounded[2]) >> 8 == w.getblockmeta(d, x, y,
					z) >> 8)
				return;
		}
		else if (w.getblock(d, x - rounded[0], y - rounded[1], z - rounded[2]) == RedZoneBlocks.REPULSOR_SHOOTER.blockID)
		{
			double[] otherBlockDir = Orienter.getDirection(Orienter.NORTH_VECTOR,
					w.getblockmeta(d, x - rounded[0], y - rounded[1], z - rounded[2]));
			int[] roundedDir = {(int) Math.round(otherBlockDir[0]), (int) Math.round(otherBlockDir[1]),
					(int) Math.round(otherBlockDir[2])};
			if (Orienter.getSideForm(roundedDir) == (w.getblockmeta(d, x, y, z) >> 8))
			{
				RepulsorShooter ts = (RepulsorShooter) RedZoneBlocks.REPULSOR_SHOOTER;
				if (ts != null && ts.getPowerLevel(w, d, x - rounded[0], y - rounded[1], z - rounded[2]) > 0)
					return;
			}
		}
		w.setblocknonotify(d, x, y, z, 0);
		Block toTick = Blocks.getBlock(w.getblock(d, x + rounded[0], y + rounded[1], z + rounded[2]));
		if (toTick != null)
			toTick.tickMeFast(w, d, x + rounded[0], y + rounded[1], z + rounded[2]);
	}

}
