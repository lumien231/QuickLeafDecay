package lumien.quickleafdecay.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class QuickLeafDecayConfig
{
    public static final String CATEGORY_GENERAL = "general";
    public static final String SUBCATEGORY_FASTLEAFDECAY = "fastleafdecay";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.IntValue decaySpeed; // DecaySpeed
    public static ForgeConfigSpec.IntValue decayFuzz; // Decay Fuzz
    public static ForgeConfigSpec.BooleanValue playerDecay; // Broken by Player

    private static final int MAX_VALUE = 2147483647;

    static
    {
        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        setupConfig();
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void setupConfig()
    {
        COMMON_BUILDER.comment("Fast Leaf Decay Settings").push(SUBCATEGORY_FASTLEAFDECAY);

        decaySpeed = COMMON_BUILDER
                .comment("The Speed at which Leaves Decay, Higher -> Slower")
                .defineInRange("decaySpeed", 7, 0, MAX_VALUE);
        decayFuzz = COMMON_BUILDER
                .comment("How random will the leaf decay be? Higher -> More Random")
                .defineInRange("decayFuzz", 7, 0, MAX_VALUE);
        playerDecay = COMMON_BUILDER
                .comment("If set to true only trees broken by players will quickly decay.")
                .define("playerDecay", true);

        COMMON_BUILDER.pop();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path)
    {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent)
    {}

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configEvent)
    {}
}
