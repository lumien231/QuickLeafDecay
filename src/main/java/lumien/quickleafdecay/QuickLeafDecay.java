package lumien.quickleafdecay;

import lumien.quickleafdecay.config.QuickLeafDecayConfig;
import lumien.quickleafdecay.init.ModGlobals;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Mod(ModGlobals.MODID)
public class QuickLeafDecay {

	static Random rng = new Random();
	private static QuickLeafDecay INSTANCE;
	private Cache<BlockPos, Integer> brokenBlockCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).maximumSize(200).build();

	public QuickLeafDecay()
	{
		INSTANCE = this;
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, QuickLeafDecayConfig.COMMON_CONFIG);
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::preInit);

		MinecraftForge.EVENT_BUS.addListener(this::breakBlock);
		MinecraftForge.EVENT_BUS.addListener(this::notifyNeighbors);
		MinecraftForge.EVENT_BUS.addListener(LeafTickScheduler.INSTANCE::tick);

		QuickLeafDecayConfig.loadConfig(QuickLeafDecayConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("quickleafdecay-common.toml"));
	}

	public void preInit(FMLCommonSetupEvent event)
	{

	}

	public void breakBlock(BlockEvent.BreakEvent event)
	{
		if (QuickLeafDecayConfig.playerDecay.get() && !(event.getPlayer() instanceof FakePlayer) && !event.getWorld().isRemote())
		{
			brokenBlockCache.put(event.getPos(), 0);
		}
	}

	public void notifyNeighbors(BlockEvent.NeighborNotifyEvent event)
	{
		if (!event.getWorld().isRemote() && !QuickLeafDecayConfig.playerDecay.get() || brokenBlockCache.getIfPresent(event.getPos()) != null)
		{
			BlockState notifierState = event.getState();
			Block b = notifierState.getBlock();

			if (b.isAir(notifierState, event.getWorld(), event.getPos()))
			{
				if (QuickLeafDecayConfig.playerDecay.get()) brokenBlockCache.invalidate(event.getPos());

				for (Direction facing : event.getNotifiedSides())
				{
					BlockPos offPos = event.getPos().offset(facing);

					if (event.getWorld().isAreaLoaded(offPos,0))
					{
						BlockState state = event.getWorld().getBlockState(offPos);

						if (BlockTags.LEAVES.contains(state.getBlock()))
						{
							if (QuickLeafDecayConfig.playerDecay.get()) brokenBlockCache.put(offPos, 0);
							LeafTickScheduler.INSTANCE.schedule((World) event.getWorld(), offPos, QuickLeafDecayConfig.decaySpeed.get() + (QuickLeafDecayConfig.decayFuzz.get() > 0 ? rng.nextInt(QuickLeafDecayConfig.decayFuzz.get()) : 0));
						}
					}
				}
			}
		}
	}
}
