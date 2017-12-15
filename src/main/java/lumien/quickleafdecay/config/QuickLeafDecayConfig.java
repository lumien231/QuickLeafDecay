package lumien.quickleafdecay.config;

import java.util.List;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class QuickLeafDecayConfig
{
	// DecaySpeed
	Property decaySpeedProperty;
	public int decaySpeed;

	// Decay Fuzz
	Property decayFuzzProperty;
	public int decayFuzz;

	// Broken by Player
	Property playerDecayProperty;
	public boolean playerDecay;

	Configuration config;

	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		decaySpeedProperty = config.get("Settings", "DecaySpeed", 7, "The Speed at which Leaves Decay Higher->Slower");
		decayFuzzProperty = config.get("Settings", "DecayFuzz", 7, "How random will the leaf decay be? Higher->More Random");
		playerDecayProperty = config.get("Settings", "PlayerDecay", true, "If set to true only trees broken by players will quickly decay.");

		syncConfig();
	}

	public void syncConfig()
	{
		decaySpeed = decaySpeedProperty.getInt();
		decayFuzz = decayFuzzProperty.getInt();
		playerDecay = playerDecayProperty.getBoolean();

		if (config.hasChanged())
		{
			config.save();
		}
	}

	public String getString()
	{
		return config.toString();
	}

	public List<IConfigElement> getConfigElements()
	{
		return new ConfigElement(config.getCategory("settings")).getChildElements();
	}
}
