package lumien.quickleafdecay.config;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class QuickLeafDecayConfig
{
	// DecaySpeed
	public int decaySpeed = 5;

	// Decay Fuzz
	public int decayFuzz;

	// Broken by Player
	public boolean playerDecay = true;

	public void preInit(FMLCommonSetupEvent event)
	{
		syncConfig();
	}

	public void syncConfig()
	{
		
	}
}
