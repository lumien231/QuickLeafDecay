package lumien.quickleafdecay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.state.IBlockState;
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

				if (--st.tick == 0)
				{
					iterator.remove();

					IBlockState state = st.worldObj.getBlockState(st.pos);

					state.tick(st.worldObj, st.pos, st.worldObj.getRandom());
					state.randomTick(st.worldObj, st.pos, st.worldObj.getRandom());
				}
			}
		}
	}

	class ScheduledTick
	{
		World worldObj;
		BlockPos pos;

		int tick;

		public ScheduledTick(World worldObj, BlockPos pos, int tick)
		{
			super();
			this.worldObj = worldObj;
			this.pos = pos;
			this.tick = tick;
		}
	}
}
