package blocks;

import java.util.List;
import java.util.ListIterator;

import dangerzone.DangerZone;
import dangerzone.World;
import dangerzone.blocks.Blocks;
import dangerzone.entities.Entity;
import dangerzone.entities.EntityLiving;
import dangerzone.threads.FastBlockTicker;
import entities.EntityPushedBlock;
import mechanics.Orienter;

/**
 * Repulsor shooters shoot out repulsor beams, which can move entities and
 * blocks around.
 * <p>
 * Repulsor shooters, when powered, shoot a repulsor beam out of their front.
 * How far this beam goes is determined by the closest entity or block in that
 * direction, or the power supplied, whichever is smaller. If the repulsor beam
 * is not obstructed, it will cause the first block or entity it hits to move
 * away from the repulsor shooter.
 * 
 * @author eaglgenes101
 * @see TractorShooter
 * @see RepulsorBeam
 * @see EntityPushedBlock
 */
public class RepulsorShooter extends TractorShooter
{

	public RepulsorShooter(String n)
	{
		super(n);
		topname = "RedZone_res/res/blocks/dispenser_side.png";
		bottomname = "RedZone_res/res/blocks/dispenser_side.png";
		leftname = "RedZone_res/res/blocks/dispenser_side.png";
		rightname = "RedZone_res/res/blocks/dispenser_side.png";
		frontname = "RedZone_res/res/blocks/repulsor_front.png";
		backname = "RedZone_res/res/blocks/dispenser_side.png";
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
			if (w.getblock(d, x + offset[0] * reach, y + offset[1] * reach,
					z + offset[2] * reach) != RedZoneBlocks.REPULSOR_BEAM.blockID)
				w.setblockandmeta(d, x + offset[0] * reach, y + offset[1] * reach, z + offset[2] * reach,
						RedZoneBlocks.REPULSOR_BEAM.blockID, (Orienter.getSideForm(offset) << 8)|(powerLevel));
			else if (w.getblockmeta(d, x + offset[0] * reach, y + offset[1] * reach,
					z + offset[2] * reach) >> 8 != Orienter.getSideForm(offset))
				w.setblockandmeta(d, x + offset[0] * reach, y + offset[1] * reach, z + offset[2] * reach,
						RedZoneBlocks.REPULSOR_BEAM.blockID, (Orienter.getSideForm(offset) << 8)|(powerLevel));

			List<Entity> nearby_list = DangerZone.entityManager.findEntitiesInRange(2.0f, d,
					x + offset[0] * reach + 0.5, y + offset[1] * reach + 0.5, z + offset[2] * reach + 0.5);
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
						if (x + offset[0] * (reach+1) == (int) e.posx && y + offset[1] * (reach+1) == (int) e.posy
								&& z + offset[2] * (reach+1) == (int) e.posz)
							break outerLoop;
						if (e instanceof EntityLiving)
						{
							EntityLiving el = (EntityLiving) e;
							int intheight = (int) (el.height + 0.995);
							double dx, dz;
							for (int k = 0; k < intheight; k++)
							{
								if ((int) el.posy + k == y + offset[1] * reach)
								{
									dx = el.posx - (x + offset[0] * reach + 0.5);
									dz = el.posz - (z + offset[2] * reach + 0.5);
									if (Math.sqrt((dx * dx) + (dz * dz)) < (0.5 + (el.width / 2.0)))
										break outerLoop;
								}
							}
						}
					}
				}
			}
			powerLevel--;
			reach++;
		}
		FastBlockTicker.addFastTick(d, x + offset[0], y + offset[1], z + offset[2]);
	}

}
