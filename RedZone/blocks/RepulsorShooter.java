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
					z + offset[2] * reach) != RedZoneBlocks.REPULSOR_BEAM.blockID)
				w.setblockandmeta(d, x + offset[0] * reach, y + offset[1] * reach, z + offset[2] * reach,
						RedZoneBlocks.REPULSOR_BEAM.blockID, Orienter.getSideForm(offset) << 8);
			else if (w.getblockmeta(d, x + offset[0] * reach, y + offset[1] * reach,
					z + offset[2] * reach) >> 8 != Orienter.getSideForm(offset))
				w.setblockandmeta(d, x + offset[0] * reach, y + offset[1] * reach, z + offset[2] * reach,
						RedZoneBlocks.REPULSOR_BEAM.blockID, Orienter.getSideForm(offset) << 8);
			
			powerLevel--;
			reach++;
		}
		FastBlockTicker.addFastTick(d, x + offset[0], y + offset[1], z + offset[2]);
	}

}
