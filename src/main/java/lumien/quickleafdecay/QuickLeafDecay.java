package lumien.quickleafdecay;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lumien.quickleafdecay.config.QuickLeafDecayConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = QuickLeafDecay.MOD_ID, name = QuickLeafDecay.MOD_NAME, version = QuickLeafDecay.MOD_VERSION, acceptableRemoteVersions = "*", guiFactory = "lumien.quickleafdecay.config.QuickLeafDecayGuiFactory", acceptedMinecraftVersions = "[1.12,1.13)")
public class QuickLeafDecay
{
	final public static String MOD_ID = "quickleafdecay";
	final static String MOD_NAME = "Quick Leaf Decay";
	final static String MOD_VERSION = "@VERSION@";

	static Random rng = new Random();

	@Instance
	public static QuickLeafDecay INSTANCE;

	public QuickLeafDecayConfig config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new QuickLeafDecayConfig();
		config.preInit(event);

		MinecraftForge.EVENT_BUS.register(this);
	}

	Cache<BlockPos, Integer> brokenBlockCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).maximumSize(200).build();

	@SubscribeEvent
	public void breakBlock(BreakEvent event)
	{
		if (config.playerDecay && !(event.getPlayer() instanceof FakePlayer) && !event.getWorld().isRemote)
		{
			brokenBlockCache.put(event.getPos(), 0);
		}
	}

	@SubscribeEvent
	public void notifyNeighbors(NeighborNotifyEvent event)
	{
		if (!config.playerDecay || brokenBlockCache.getIfPresent(event.getPos()) != null)
		{
			if (config.playerDecay)
				brokenBlockCache.invalidate(event.getPos());

			IBlockState notifierState = event.getState();
			Block b = notifierState.getBlock();

			if (b.isAir(notifierState, event.getWorld(), event.getPos()))
			{
				for (EnumFacing facing : event.getNotifiedSides())
				{
					BlockPos pos = event.getPos().offset(facing);

					if (event.getWorld().isBlockLoaded(pos))
					{
						IBlockState state = event.getWorld().getBlockState(pos);

						if (state.getBlock().isLeaves(state, event.getWorld(), pos))
						{
							if (!(event.getWorld().isBlockTickPending(pos, state.getBlock()) || event.getWorld().isUpdateScheduled(pos, state.getBlock())))
							{
								if (config.playerDecay)
									brokenBlockCache.put(pos, 0);

								event.getWorld().scheduleUpdate(pos, state.getBlock(), config.decaySpeed + (config.decayFuzz > 0 ? rng.nextInt(config.decayFuzz) : 0));
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
	{
		if (event.getModID().equals(MOD_ID))
		{
			config.syncConfig();
		}
	}
}
