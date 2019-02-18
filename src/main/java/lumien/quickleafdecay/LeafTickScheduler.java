package lumien.quickleafdecay;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LeafTickScheduler
{
	public static LeafTickScheduler INSTANCE = new LeafTickScheduler();

	List<ScheduledTick> planned;
	List<ScheduledTick> scheduled;

	public LeafTickScheduler()
	{
		planned = new ArrayList<ScheduledTick>();
		scheduled = new ArrayList<ScheduledTick>();
	}

	public void schedule(World world, BlockPos pos, int delay)
	{
		this.planned.add(new ScheduledTick(world, pos, delay));
	}

	public void tick(TickEvent.ServerTickEvent event)
	{
		if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END)
		{
			if (!this.planned.isEmpty())
			{
				this.scheduled.addAll(planned);
				this.planned.clear();
			}

			Iterator<ScheduledTick> iterator = scheduled.iterator();

			while (iterator.hasNext())
			{
				ScheduledTick st = iterator.next();

				if (--st.tick <= 0)
				{
					iterator.remove();

					World worldObj = st.worldReference.get();
					if (worldObj != null && worldObj.isBlockLoaded(st.pos))
					{
						IBlockState state = worldObj.getBlockState(st.pos);

						if (BlockTags.LEAVES.contains(state.getBlock()))
						{
							state.tick(worldObj, st.pos, worldObj.getRandom());
							state.randomTick(worldObj, st.pos, worldObj.getRandom());
						}
					}
				}
			}
		}
	}

	class ScheduledTick
	{
		WeakReference<World> worldReference;
		BlockPos pos;

		int tick;

		public ScheduledTick(World worldObj, BlockPos pos, int tick)
		{
			super();
			this.worldReference = new WeakReference<World>(worldObj);
			this.pos = pos;
			this.tick = tick;
		}
	}
}
