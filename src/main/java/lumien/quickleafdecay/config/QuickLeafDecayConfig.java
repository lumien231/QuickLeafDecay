package lumien.quickleafdecay.config;

import static net.minecraftforge.fml.Logging.CORE;
import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfig.Server;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class QuickLeafDecayConfig
{

	// DecaySpeed
	public static IntValue decaySpeed;

	// Decay Fuzz
	public static IntValue decayFuzz;

	// Broken by Player
	public static BooleanValue playerDecay;

	public QuickLeafDecayConfig(ForgeConfigSpec.Builder builder)
	{
		decaySpeed = builder.comment("The Speed at which Leaves Decay, Higher -> Slower").defineInRange("decaySpeed", 7, 0, Integer.MAX_VALUE);
		decayFuzz = builder.comment("How random will the leaf decay be? Higher -> More Random").defineInRange("decayFuzz", 7, 0, Integer.MAX_VALUE);
	
		playerDecay = builder.comment("If set to true only trees broken by players will quickly decay.").define("playerDecay", true);
	}

	public void preInit(FMLCommonSetupEvent event)
	{
		
	}
	
    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) 
    {

    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.ConfigReloading configEvent) 
    {

    }
	
	public static final ForgeConfigSpec spec;
    public static final QuickLeafDecayConfig CONFIG;
    static {
        final Pair<QuickLeafDecayConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(QuickLeafDecayConfig::new);
        spec = specPair.getRight();
        CONFIG = specPair.getLeft();
    }
}
