package lumien.quickleafdecay.config;

import java.util.ArrayList;
import java.util.List;

import lumien.quickleafdecay.QuickLeafDecay;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class QuickLeafDecayConfigGui extends GuiConfig
{
	public QuickLeafDecayConfigGui(GuiScreen parent)
	{
		super(parent, getConfigElements(), QuickLeafDecay.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(QuickLeafDecay.INSTANCE.config.getString()));
	}

	private static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.addAll(QuickLeafDecay.INSTANCE.config.getConfigElements());
		return list;
	}
}
